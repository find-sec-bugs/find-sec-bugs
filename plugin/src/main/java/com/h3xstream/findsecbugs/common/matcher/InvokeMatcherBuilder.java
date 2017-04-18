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
package com.h3xstream.findsecbugs.common.matcher;

import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;

import java.util.ArrayList;
import java.util.List;

public class InvokeMatcherBuilder {

    private List<String> classesNames = new ArrayList<String>();
    private List<String> methodNames = new ArrayList<String>();
    private List<String> argSignatures = new ArrayList<String>();


    public InvokeMatcherBuilder atClass(String... classesNames) {
        for(String clz : classesNames) {
            this.classesNames.add(clz.replace('/','.'));
        }
        return this;
    }

    public InvokeMatcherBuilder atMethod(String... methodNames) {
        for(String method : methodNames) {
            this.methodNames.add(method);
        }
        return this;
    }

    public InvokeMatcherBuilder withArgs(String... argSignatures) {
        for(String method : argSignatures) {
            this.argSignatures.add(method);
        }
        return this;
    }

    public boolean matches(OpcodeStackDetector opcodeStackDetector) {
        if(classesNames.size() != 0 && !classesNames.contains(opcodeStackDetector.getClassConstantOperand().replace('/','.'))) {
            return false;
        }
        if(methodNames.size() != 0 && !methodNames.contains(opcodeStackDetector.getNameConstantOperand())) {
            return false;
        }
        if(argSignatures.size() != 0 && !argSignatures.contains(opcodeStackDetector.getSigConstantOperand())) {
            return false;
        }
        return true;
    }


    public boolean matches(Instruction instruction, ConstantPoolGen cpg) {
        if(instruction != null && instruction instanceof InvokeInstruction) {
            InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
            if (classesNames.size() != 0 && !classesNames.contains(invokeInstruction.getClassName(cpg))) {
                return false;
            }
            else if (methodNames.size() != 0 && !methodNames.contains(invokeInstruction.getMethodName(cpg))) {
                return false;
            }
            else if (argSignatures.size() != 0 && !argSignatures.contains(invokeInstruction.getSignature(cpg))) {
                return false;
            }
            return true;
        }
        return false;
    }
}
