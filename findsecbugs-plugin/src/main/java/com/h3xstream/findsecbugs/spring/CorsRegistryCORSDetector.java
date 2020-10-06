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
package com.h3xstream.findsecbugs.spring;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantString;
import org.apache.commons.codec.Charsets;

import java.io.*;
import java.util.Arrays;

public class CorsRegistryCORSDetector extends OpcodeStackDetector {

    private BugReporter bugReporter;

    public CorsRegistryCORSDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        //printOpCode(seen);
        if (seen == Const.INVOKEVIRTUAL && getNameConstantOperand().equals("allowedOrigins")) {
            if ("org/springframework/web/servlet/config/annotation/CorsRegistration".equals(getClassConstantOperand())) {
                OpcodeStack.Item item = stack.getStackItem(0); //First item on the stack is the last
                if(item.isArray()) {
                    String[] strings=getStringArray(item);
                    String pattern="*";
                    for (String s: strings) {
                        if (s.equals(pattern)) {
                             bugReporter.reportBug(new BugInstance(this, "PERMISSIVE_CORS", HIGH_PRIORITY)
                        .addClassAndMethod(this).addSourceLine(this));
                             break;
                        }
                    }
                }
            }
        }
    }
    public String[] getStringArray(OpcodeStack.Item item){
        Integer argumentsNum = (Integer) item.getConstant();
        String[] strings = new String[argumentsNum];
        for (int i=0; i<argumentsNum; i++) {
            int idx=-5-5*i;
            int stringIdx=getNextCodeByte(idx);
//            System.out.println(stringIdx);
            String s=getStringFromIdx(stringIdx);
//            System.out.println(Arrays.toString(s.toCharArray()));
            strings[i]=s;
        }
        return strings;
    }
    public String getStringFromIdx(int idx) {
        Constant constant= getConstantPool().getConstant(idx);
        int s = ((ConstantString) constant).getStringIndex();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream outputStream=new DataOutputStream(baos);
        try {
            Constant string = getConstantPool().getConstant(s);
            if (string!=null) {
                string.dump(outputStream);
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString().substring(3);
    }
}
