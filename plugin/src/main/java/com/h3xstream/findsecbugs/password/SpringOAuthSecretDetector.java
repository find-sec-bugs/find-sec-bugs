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
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class SpringOAuthSecretDetector extends OpcodeStackDetector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";

    private BugReporter bugReporter;

    public SpringOAuthSecretDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        printOpCode(seen);

        //  SpringOAuthSecretDetector: [0069]  invokevirtual   org/springframework/security/oauth2/config/annotation/builders/ClientDetailsServiceBuilder$ClientBuilder.secret (Ljava/lang/String;)Lorg/springframework/security/oauth2/config/annotation/builders/ClientDetailsServiceBuilder$ClientBuilder;

        if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("org/springframework/security/oauth2/config/annotation/builders/ClientDetailsServiceBuilder$ClientBuilder") &&
                getNameConstantOperand().equals("secret") && getSigConstantOperand().equals("(Ljava/lang/String;)Lorg/springframework/security/oauth2/config/annotation/builders/ClientDetailsServiceBuilder$ClientBuilder;")) {
            OpcodeStack.Item item0 = stack.getStackItem(0); //The last argument (on the top of the stack)
            if (StackUtils.isConstantString(item0)) {
                bugReporter.reportBug(new BugInstance(this, HARD_CODE_PASSWORD_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)); //
            }
        }
    }
}
