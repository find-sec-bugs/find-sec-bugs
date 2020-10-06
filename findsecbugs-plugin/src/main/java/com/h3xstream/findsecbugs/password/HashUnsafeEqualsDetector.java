/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.password;

import com.h3xstream.findsecbugs.common.StackUtils;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.*;

import java.util.ArrayList;
import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * <p>
 *     Detect hash value that are compare with the equals method.
 *     The equals implementation is stopping the comparison of both value once a first difference is found.
 *     This comparison is susceptible to timing attack.
 * </p>
 *
 * <pre>
 * if(hashInput.equals(actualHash)) {
 *     ....
 * }
 * </pre>
 */
public class HashUnsafeEqualsDetector extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final String UNSAFE_HASH_EQUALS_TYPE = "UNSAFE_HASH_EQUALS";

    private static final InvokeMatcherBuilder STRING_EQUALS_METHOD = invokeInstruction() //
            .atClass("java/lang/String").atMethod("equals").withArgs("(Ljava/lang/Object;)Z");

    private static final InvokeMatcherBuilder ARRAYS_EQUALS_METHOD = invokeInstruction() //
            .atClass("java/util/Arrays").atMethod("equals").withArgs("([B[B)Z");
    private static final boolean DEBUG = false;

    /**
     * Keyword that describe variable that are likely to be hashed value.
     */
    private static final List<String> HASH_WORDS = new ArrayList<String>();
    static {
        HASH_WORDS.add("hash");
        HASH_WORDS.add("md5");
        HASH_WORDS.add("sha");
        HASH_WORDS.add("digest");
    }
    /**
     * The keyword "SHA" will catch many word that are unrelated to hashing.
     * For example: sharedLink, shallBeRemoved, ...
     * When new false positive are encounter, this list can be extended.
     */
    private static final List<String> ALLOWED_WORDS = new ArrayList<String>();
    static {
        ALLOWED_WORDS.add("share"); //share shared
        ALLOWED_WORDS.add("shall");
        ALLOWED_WORDS.add("shad"); //shade shadow
        ALLOWED_WORDS.add("sharp");
        ALLOWED_WORDS.add("shap"); //shape
    }

    public HashUnsafeEqualsDetector(BugReporter bugReporter) {
        super(bugReporter);
        registerVisitor(this);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset) throws DataflowAnalysisException {

        Taint rightValue = fact.getStackValue(offset);
        Taint leftValue = fact.getStackValue(offset == 0 ? 1 : 0);

        boolean passwordVariableLeft = leftValue.isUnknown() && leftValue.hasTag(Taint.Tag.HASH_VARIABLE);
        boolean passwordVariableRight = rightValue.isUnknown() && rightValue.hasTag(Taint.Tag.HASH_VARIABLE);

        //Is a constant value that was tag because the value was place in a variable name "password" at some point.
        if (passwordVariableLeft  || passwordVariableRight) {
            return Priorities.NORMAL_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        if(STRING_EQUALS_METHOD.matches(invoke,cpg) || ARRAYS_EQUALS_METHOD.matches(invoke,cpg)) {
            return new InjectionPoint(new int[]{0,1}, UNSAFE_HASH_EQUALS_TYPE);
        }
        return InjectionPoint.NONE;
    }

    @Override
    public void visitInvoke(InvokeInstruction invoke, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters, ConstantPoolGen cpg) {

    }

    @Override
    public void visitReturn(MethodGen methodGen, Taint returnValue, ConstantPoolGen cpg) throws Exception {

    }

    @Override
    public void visitLoad(LoadInstruction instruction, MethodGen methodGen, TaintFrame frameType, int numProduced, ConstantPoolGen cpg) {
        //Extract the name of the variable
        int index = instruction.getIndex();
        LocalVariableGen var = StackUtils.getLocalVariable(methodGen, index);
        if(var == null) {
            if(DEBUG) System.out.println("Unable to get field name for index "+ index + " in "+methodGen);
            return;
        }
        String fieldName = var.getName();

        boolean isHashVariable = false;
        String fieldNameLower = fieldName.toLowerCase();
        likelyHash : for (String password : HASH_WORDS) {
            if (fieldNameLower.contains(password)) {
                for (String  allowedWord: ALLOWED_WORDS) {
                    if (fieldNameLower.contains(allowedWord)) {
                        break likelyHash;
                    }
                }
                isHashVariable = true;
            }
        }

        if(!isHashVariable) {return;}

        //Mark local variable
        Taint passwordValue = frameType.getValue(index);
        passwordValue.addTag(Taint.Tag.HASH_VARIABLE);

        if(numProduced <= 0) {return;}

        //Mark the stack value
        try {
            for(int indexStack=0;indexStack<numProduced;indexStack++) {
                Taint value = frameType.getStackValue(indexStack);
                value.addTag(Taint.Tag.HASH_VARIABLE);
            }
        } catch (DataflowAnalysisException e) {
        }
    }

    @Override
    public void visitField(FieldInstruction put, MethodGen methodGen, TaintFrame frameType, Taint taint, int numProduced, ConstantPoolGen cpg) throws Exception {

    }
}
