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

public class WeakTLSDetector extends OpcodeStackDetector {

    private static final String DEFAULT_HTTP_CLIENT = "DEFAULT_HTTP_CLIENT";
    private static final String SSL_CONTEXT = "SSL_CONTEXT";

    private BugReporter bugReporter;

    public WeakTLSDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Const.INVOKESPECIAL 
            && getClassConstantOperand().equals("org/apache/http/impl/client/DefaultHttpClient") 
            && getNameConstantOperand().equals("<init>") 
            && getSigConstantOperand().equals("()V")) {
          
            //DefaultHttpClient constructor with no parameter
            bugReporter.reportBug(new BugInstance(this, DEFAULT_HTTP_CLIENT, Priorities.NORMAL_PRIORITY)
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
        
        if (seen == Const.INVOKESTATIC 
            && getClassConstantOperand().equals("javax/net/ssl/SSLContext") 
            && getNameConstantOperand().equals("getInstance")
            && getSigConstantOperand().equals("(Ljava/lang/String;)Ljavax/net/ssl/SSLContext;")) {
          
            //System.out.println("SSLContext.getInstance(" + this.getSigConstantOperand() + ")");
            final OpcodeStack.Item item = stack.getStackItem(0);              
            String sslContextName = (String) item.getConstant(); //Null if the value passed isn't constant
              
            if (sslContextName != null && sslContextName.equalsIgnoreCase("SSL")) {
                bugReporter.reportBug(new BugInstance(this, SSL_CONTEXT, Priorities.NORMAL_PRIORITY)
                           .addClass(this).addMethod(this).addSourceLine(this));
            }

        }
    }
}
