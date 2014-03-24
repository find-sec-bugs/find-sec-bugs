/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findsecbugs.xxe;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

/**
 * 3 equivalent APIs are covered by this detector.
 *
 * <li>SAXParser.parse()</li>
 * <li>XMLReader.parse()</li>
 * <li>DocumentBuilder.parse()</li>
 * <br/>
 * References:
 * https://www.securecoding.cert.org/confluence/pages/viewpage.action?pageId=61702260
 */
public class SaxParserXxeDetector extends OpcodeStackDetector {
    private static final boolean DEBUG = false;
    private static final String XXE_TYPE = "XXE";

    private BugReporter bugReporter;

    public SaxParserXxeDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if(seen != Constants.INVOKEVIRTUAL && seen != INVOKEINTERFACE) return;

        String fullClassName = getClassConstantOperand();
        String method = getNameConstantOperand();

        //The method call is doing XML parsing (see class javadoc)
        if ((seen == Constants.INVOKEVIRTUAL &&
                fullClassName.equals("javax/xml/parsers/SAXParser") &&
                method.equals("parse")) ||
                (seen == Constants.INVOKEINTERFACE &&
                        fullClassName.equals("org/xml/sax/XMLReader") &&
                        method.equals("parse")) ||
                (seen == Constants.INVOKEVIRTUAL &&
                        getClassConstantOperand().equals("javax/xml/parsers/DocumentBuilder") &&
                        method.equals("parse"))) {

            JavaClass javaClass = getThisClass();

            //(1rst solution for secure parsing) Sandbox in an action with limited privileges
            if (InterfaceUtils.classImplements(javaClass, "java.security.PrivilegedExceptionAction")) {
                return; //Assuming the proper right are apply to the sandbox
            }

            ClassContext classCtx = getClassContext();
            ConstantPoolGen cpg = classCtx.getConstantPoolGen();
            MethodGen methodGen = classCtx.getMethodGen(getMethod());

            //(2nd solution for secure parsing) Look for entity custom resolver
            for (Instruction inst : methodGen.getInstructionList().getInstructions()) {
                if (DEBUG) {
                    ByteCode.printOpCode(inst, cpg);
                }

                if (inst instanceof INVOKEINTERFACE) { //XMLReader.setEntityResolver is called
                    INVOKEINTERFACE invoke = (INVOKEINTERFACE) inst;
                    if ("setEntityResolver".equals(invoke.getMethodName(cpg))) {
                        return;
                    }
                }
            }

            String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('/'));
            //Raise a bug
            bugReporter.reportBug(new BugInstance(this, XXE_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString(simpleClassName+"."+method+"(...)"));

        }
    }
}
