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
package com.h3xstream.findsecbugs.scala;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

import static org.apache.bcel.Const.GETSTATIC;
import static org.apache.bcel.Const.INVOKEVIRTUAL;

public class SslDisablerDetector extends OpcodeStackDetector {

    private static final String WEAK_TRUST_MANAGER_TYPE = "WEAK_TRUST_MANAGER";
    private static final String WEAK_HOSTNAME_VERIFIER_TYPE = "WEAK_HOSTNAME_VERIFIER";

    private BugReporter bugReporter;

    public SslDisablerDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
//        printOpCode(seen);

//        SslDisablerDetector: [0000]  getstatic
//        SslDisablerDetector: [0003]  invokevirtual   SecurityBypasser$.destroyAllSSLSecurityForTheEntireVMForever ()V

        if(seen == INVOKEVIRTUAL && getClassConstantOperand().endsWith("SecurityBypasser$") && getNameConstantOperand().equals("destroyAllSSLSecurityForTheEntireVMForever")) {
            bugReporter.reportBug(new BugInstance(this, WEAK_TRUST_MANAGER_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }

        if(seen == GETSTATIC) {
            XField field = this.getXFieldOperand();
            if(field == null) return;
            //It is expected that developers either build the project or copy-paste the code in a random package..

            //Scala code:
            //1. HttpsURLConnection.setDefaultHostnameVerifier(SecurityBypasser.AllHosts);
            if(field.getClassName().endsWith("SecurityBypasser$AllHosts$") && field.getName().equals("MODULE$")) {
                bugReporter.reportBug(new BugInstance(this, WEAK_HOSTNAME_VERIFIER_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
            //Scala code:
            //2. val trustAllCerts = Array[TrustManager](SecurityBypasser.AllTM)
            else if(field.getClassName().endsWith("SecurityBypasser$AllTM$") && field.getName().equals("MODULE$")) {
                bugReporter.reportBug(new BugInstance(this, WEAK_TRUST_MANAGER_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
    }

}
