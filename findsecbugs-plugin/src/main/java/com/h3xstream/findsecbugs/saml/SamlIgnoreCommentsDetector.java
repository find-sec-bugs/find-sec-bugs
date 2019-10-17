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
package com.h3xstream.findsecbugs.saml;

import com.h3xstream.findsecbugs.common.StackUtils;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * More information on the vulnerability:
 *
 * https://duo.com/blog/duo-finds-saml-vulnerabilities-affecting-multiple-implementations
 * https://github.com/spring-projects/spring-security-saml/issues/228
 */
public class SamlIgnoreCommentsDetector extends OpcodeStackDetector {

    private static final String SAML_IGNORE_COMMENTS = "SAML_IGNORE_COMMENTS";
    private static final InvokeMatcherBuilder SET_IGNORE_COMMENTS = invokeInstruction()
            .atClass(
                    "org.opensaml.xml.parse.BasicParserPool",
                    "org.opensaml.xml.parse.StaticBasicParserPool"
            )
            .atMethod("setIgnoreComments");

    private BugReporter bugReporter;

    public SamlIgnoreCommentsDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Const.INVOKEVIRTUAL && SET_IGNORE_COMMENTS.matches(this)) {
            final OpcodeStack.Item item = stack.getStackItem(0);
            /* item has signature of Integer, check "instanceof" added to prevent cast from throwing exceptions */

            if (StackUtils.isConstantInteger(item) && (Integer) item.getConstant()  == 0) {
                bugReporter.reportBug(new BugInstance(this, SAML_IGNORE_COMMENTS, Priorities.HIGH_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
    }
}
