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
import com.h3xstream.findsecbugs.common.TaintUtil;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.*;

import java.util.List;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * <p>
 *     Detect:
 * </p>
 *
 * <pre>
 * if(password.equals("SuperSecr3t!1")) {
 *     ....
 * }
 * </pre>
 */
public class HardcodedPasswordEqualsDetector extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";

    private static final InvokeMatcherBuilder STRING_EQUALS_METHOD = invokeInstruction() //
            .atClass("java/lang/String").atMethod("equals").withArgs("(Ljava/lang/Object;)Z");
    private static final boolean DEBUG = false;

    public HardcodedPasswordEqualsDetector(BugReporter bugReporter) {
        super(bugReporter);
        registerVisitor(this);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset) throws DataflowAnalysisException {

        Taint rightValue = fact.getStackValue(offset);
        Taint leftValue = fact.getStackValue(offset == 0 ? 1 : 0);

        boolean passwordVariableLeft = leftValue.isUnknown() && leftValue.hasTag(Taint.Tag.PASSWORD_VARIABLE);
        boolean passwordVariableRight = rightValue.isUnknown() && rightValue.hasTag(Taint.Tag.PASSWORD_VARIABLE);

        //If a constant value is compare with the variable
        //Empty constant are ignored.. it is most likely a validation to make sure it is not empty
        boolean valueHardcodedLeft = TaintUtil.isConstantValueAndNotEmpty(leftValue);
        boolean valueHardcodedRight = TaintUtil.isConstantValueAndNotEmpty(rightValue);

        //Is a constant value that was tag because the value was place in a variable name "password" at some point.
        if ((passwordVariableLeft && valueHardcodedRight) ||
                (passwordVariableRight && valueHardcodedLeft)) {
            return Priorities.NORMAL_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
    }


    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        if(STRING_EQUALS_METHOD.matches(invoke,cpg)) {
            return new InjectionPoint(new int[]{0,1}, HARD_CODE_PASSWORD_TYPE);
        }
        return InjectionPoint.NONE;
    }

    @Override
    public void visitInvoke(InvokeInstruction instruction, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters) {
        //ByteCode.printOpCode(instruction, cpg);
    }

    @Override
    public void visitReturn(InvokeInstruction invoke, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType) throws Exception {

    }

    @Override
    public void visitLoad(LoadInstruction instruction, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType, int numProduced) {
        //Extract the name of the variable
        int index = instruction.getIndex();
        LocalVariableGen var = StackUtils.getLocalVariable(methodGen, index);
        if(var == null) {
            if(DEBUG) System.out.println("Unable to get field name for index "+ index + " in "+methodGen);
            return;
        }
        String fieldName = var.getName();

        boolean isPasswordVariable = false;
        String fieldNameLower = fieldName.toLowerCase();
        for (String password : IntuitiveHardcodePasswordDetector.PASSWORD_WORDS) {
            if (fieldNameLower.contains(password)) {
                isPasswordVariable = true;
            }
        }

        if(!isPasswordVariable) {return;}

        //Mark local variable
        Taint passwordValue = frameType.getValue(index);
        passwordValue.addTag(Taint.Tag.PASSWORD_VARIABLE);

        if(numProduced <= 0) {
            if(DEBUG) System.out.println("Unexpected number of stack variable produced");
            return;
        }

        //Mark the stack value
        try {
            for(int indexStack=0;indexStack<numProduced;indexStack++) {
                Taint value = frameType.getStackValue(indexStack);
                value.addTag(Taint.Tag.PASSWORD_VARIABLE);
            }
        } catch (DataflowAnalysisException e) {
        }

    }

    @Override
    public void visitField(FieldInstruction put, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType, int numProduced) throws Exception {

    }


}
