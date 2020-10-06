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
package com.h3xstream.findsecbugs.ldap;

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;
import static org.apache.bcel.Const.INVOKESPECIAL;
import static org.apache.bcel.Const.INVOKEVIRTUAL;

/**
 * LDAP Entry Poisoning
 *
 * For more information:
 * https://www.blackhat.com/docs/us-16/materials/us-16-Munoz-A-Journey-From-JNDI-LDAP-Manipulation-To-RCE-wp.pdf
 */
public class LdapEntryPoisoningDetector extends OpcodeStackDetector {

    private String LDAP_ENTRY_POISONING = "LDAP_ENTRY_POISONING";

    private static final InvokeMatcherBuilder PATTERN_SEARCH_CONTROLS_INIT = invokeInstruction() //
            .atClass("javax/naming/directory/SearchControls") //
            .atMethod("<init>").withArgs("(IJI[Ljava/lang/String;ZZ)V");
    private static final InvokeMatcherBuilder PATTERN_SEARCH_CONTROLS_SETTER = invokeInstruction() //
            .atClass("javax/naming/directory/SearchControls") //
            .atMethod("setReturningObjFlag").withArgs("(Z)V");

    private BugReporter bugReporter;

    public LdapEntryPoisoningDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        boolean shouldReportBug = false;

        if(seen == INVOKESPECIAL) {
            if(PATTERN_SEARCH_CONTROLS_INIT.matches(this)) {
                OpcodeStack.Item item = stack.getStackItem(1);
                Object param = item.getConstant();
                shouldReportBug = param instanceof Integer && Integer.valueOf(1).equals(param);
            }
        }
        else if(seen == INVOKEVIRTUAL) {
            if(PATTERN_SEARCH_CONTROLS_SETTER.matches(this)) {
                OpcodeStack.Item item = stack.getStackItem(0);
                Object param = item.getConstant();
                shouldReportBug = param instanceof Integer && Integer.valueOf(1).equals(param);
            }
        }

        if(shouldReportBug) {

            bugReporter.reportBug(new BugInstance(this, LDAP_ENTRY_POISONING, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }

    }
}
