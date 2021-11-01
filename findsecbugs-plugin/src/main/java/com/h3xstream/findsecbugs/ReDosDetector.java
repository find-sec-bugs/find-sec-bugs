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
package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.StackUtils;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.apache.bcel.Const;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * This detector does minimal effort to find potential REDOS.
 * <p>
 * It will identify pattern similar to : <code>(( )+)+</code>
 * </p>
 * <p>
 * It will not identify pattern of equivalence (such as<code>(aa|a)</code>).
 * It is far more complex to identify.
 * </p>
 * <p>
 * For more advanced Regex analysis: <a href="http://code.google.com/p/saferegex/">Safe Regex</a>
 * </p>
 */
public class ReDosDetector extends OpcodeStackDetector {

    private static final String REDOS_TYPE = "REDOS";

    private static final InvokeMatcherBuilder PATTERN_COMPILE = invokeInstruction().atClass("java/util/regex/Pattern")
            .atMethod("compile").withArgs("(Ljava/lang/String;)Ljava/util/regex/Pattern;");
    private static final InvokeMatcherBuilder STRING_MATCHES = invokeInstruction().atClass("java/lang/String")
            .atMethod("matches").withArgs("(Ljava/lang/String;)Z");

    private BugReporter bugReporter;

    public ReDosDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {


        //printOpCode(seen);
        //IMPORTANT: Both API are regroup in the same condition because they both have one input string parameter.
        if (seen == Const.INVOKESTATIC && PATTERN_COMPILE.matches(this)
                || seen == Const.INVOKEVIRTUAL && STRING_MATCHES.matches(this)) {

            OpcodeStack.Item item = stack.getStackItem(0);



            if (!StackUtils.isVariableString(item)) {
                String regex = (String) item.getConstant();

                RegexRedosAnalyzer analyzer = new RegexRedosAnalyzer();
                analyzer.analyseRegexString(regex);

                //Reporting the issue
                if(analyzer.isVulnerable()) {
                    MethodDescriptor md = this.getMethodDescriptor();
                    FieldDescriptor fd = this.getFieldDescriptor();

                    BugInstance bug = new BugInstance(this, REDOS_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addString(regex).addClass(this);
                    if (md != null)
                        bug.addMethod(md);
                    if (fd != null)
                        bug.addField(fd);

                    try {
                        bug.addSourceLine(this);
                    } catch (IllegalStateException e) {
                    }

                    bugReporter.reportBug(bug);
                }
            }

        }
    }


}
