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
package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

public class UnencryptedSocketDetector extends OpcodeStackDetector {

    private static final boolean DEBUG = false;
    private static final String UNENCRYPTED_SOCKET_TYPE = "UNENCRYPTED_SOCKET";
    private BugReporter bugReporter;

    public UnencryptedSocketDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

        if (seen == Const.INVOKESPECIAL && getClassConstantOperand().equals("java/net/Socket") &&
                getNameConstantOperand().equals("<init>")) {

            int depth = stack.getStackDepth();
            String host = "remote host"; //Bonus information for the message
            if (depth != 0) {
                int hostIndex;
                if (getSigConstantOperand().equals("(Ljava/lang/String;I)V")) {
                    hostIndex = 1;
                } else if (getSigConstantOperand().equals("(Ljava/lang/String;IZ)V")) {
                    hostIndex = 2;
                } else if (getSigConstantOperand().equals("(Ljava/lang/String;ILjava/net/InetAddress;I)V")) {
                    hostIndex = 3;
                } else {
                    hostIndex = -1;
                }

                if (hostIndex != -1) {
                    //Extract the value from the constant string
                    OpcodeStack.Item top = stack.getStackItem(hostIndex);
                    if (top.getConstant() != null) {
                        host = (String) top.getConstant();
                    }
                }
            }
            if (DEBUG) {
                System.out.println("host:" + host);
            }
            bugReporter.reportBug(new BugInstance(this, UNENCRYPTED_SOCKET_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString(host));
        }
    }
}
