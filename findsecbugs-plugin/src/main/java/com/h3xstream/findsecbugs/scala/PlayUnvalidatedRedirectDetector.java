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
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import org.apache.bcel.Const;

import java.util.Arrays;
import java.util.List;

public class PlayUnvalidatedRedirectDetector extends OpcodeStackDetector {


    private static final String PLAY_UNVALIDATED_REDIRECT_TYPE = "PLAY_UNVALIDATED_REDIRECT";

    private static List<String> REDIRECT_METHODS = Arrays.asList("Redirect","SeeOther","MovedPermanently","TemporaryRedirect");

    private BugReporter bugReporter;

    public PlayUnvalidatedRedirectDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        try {
            if(seen == Const.INVOKEVIRTUAL && REDIRECT_METHODS.contains(getNameConstantOperand())) {
                if("scala/runtime/AbstractFunction0".equals(getClassDescriptor().getXClass().getSuperclassDescriptor().getClassName())) {
                    bugReporter.reportBug(new BugInstance(this, PLAY_UNVALIDATED_REDIRECT_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this).addString(getNameConstantOperand())); //
                }
            }
        } catch (CheckedAnalysisException e) {
        }
    }
}
