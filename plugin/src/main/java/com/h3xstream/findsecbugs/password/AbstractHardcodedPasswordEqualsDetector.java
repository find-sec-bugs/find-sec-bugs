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
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.*;

import java.util.List;

public abstract class AbstractHardcodedPasswordEqualsDetector extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final boolean DEBUG = false;

    protected AbstractHardcodedPasswordEqualsDetector(BugReporter bugReporter) {
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
    public void visitInvoke(InvokeInstruction instruction, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters, ConstantPoolGen cpg) {
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
    public void visitField(FieldInstruction put, MethodGen methodGen, TaintFrame frameType, Taint taint, int numProduced, ConstantPoolGen cpg) throws Exception {

    }
}
