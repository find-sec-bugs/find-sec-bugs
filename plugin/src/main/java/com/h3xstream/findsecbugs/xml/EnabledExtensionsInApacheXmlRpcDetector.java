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
package com.h3xstream.findsecbugs.xml;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;


public class EnabledExtensionsInApacheXmlRpcDetector extends OpcodeStackDetector {

    private static final String RPC_ENABLED_EXTENSIONS = "RPC_ENABLED_EXTENSIONS";
    private static final InvokeMatcherBuilder ENABLE_EXTENSIONS = invokeInstruction()
            .atClass(
                    "org.apache.xmlrpc.client.XmlRpcClientConfigImpl",
                    "org.apache.xmlrpc.server.XmlRpcServerConfigImpl"
            )
            .atMethod("setEnabledForExtensions");

    private BugReporter bugReporter;

    public EnabledExtensionsInApacheXmlRpcDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Const.INVOKEVIRTUAL && ENABLE_EXTENSIONS.matches(this)) {
            final OpcodeStack.Item item = stack.getStackItem(0);
            /* item has signature of Integer, check "instanceof" added to prevent cast from throwing exceptions */
            if ((item.getConstant() == null)
                    || ((item.getConstant() instanceof Integer) && (((Integer) item.getConstant()).intValue()  == 1))) {
                bugReporter.reportBug(new BugInstance(this, RPC_ENABLED_EXTENSIONS, Priorities.HIGH_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
    }
}
