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

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

/**
 * References:
 * https://www.securecoding.cert.org/confluence/pages/viewpage.action?pageId=61702260
 */
public class SaxParserXxeDetector extends OpcodeStackDetector {
    private static final boolean DEBUG = true;
    private static final String XXE_TYPE = "XXE";

    private BugReporter bugReporter;

    public SaxParserXxeDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Constants.INVOKEVIRTUAL &&
                getClassConstantOperand().equals("javax/xml/parsers/SAXParser") &&
                getNameConstantOperand().equals("parse")) {

            JavaClass javaClass = getThisClass();

            if(InterfaceUtils.classImplements(javaClass,"java.security.PrivilegedExceptionAction")) {
                return; //Assuming the proper right are apply to the sandbox
            }



            bugReporter.reportBug(new BugInstance(this, XXE_TYPE, Priorities.NORMAL_PRIORITY) //
                .addClass(this).addMethod(this).addSourceLine(this)
                .addString("SAXParser.parse(...)"));

        }

        if (seen == Constants.INVOKEINTERFACE &&
                        getClassConstantOperand().equals("org/xml/sax/XMLReader") &&
                        getNameConstantOperand().equals("parse")) {

            JavaClass javaClass = getThisClass();

            if(InterfaceUtils.classImplements(javaClass,"java.security.PrivilegedExceptionAction")) {
                return; //Assuming the proper right are apply to the sandbox
            }

            Method m = getMethod();
            MethodGen methodGen = getClassContext().getMethodGen(m);
            ConstantPoolGen cpg = getClassContext().getConstantPoolGen();

            //TODO: Detect when EntityResolver are set for the current instance

//            bugReporter.reportBug(new BugInstance(this, XXE_TYPE, NORMAL_PRIORITY) //
//                            .addClass(this).addMethod(this).addSourceLine(this)
//                            .addString("XMLReader.parse(...)"));
        }
    }
}
