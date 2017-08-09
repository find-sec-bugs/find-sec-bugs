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

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;

public class XssMvcApiDetector extends BasicInjectionDetector {

    private static final String SCALA_XSS_MVC_API_TYPE = "SCALA_XSS_MVC_API";

    // This variable is compared with a .toLowerCase() variable. Please keep this const lowercase.
    private static final String VULNERABLE_CONTENT_TYPE = "text/html";

    public XssMvcApiDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("xss-scala-mvc-api.txt", SCALA_XSS_MVC_API_TYPE);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset)
            throws DataflowAnalysisException {
        Taint mvcResultTaint = fact.getStackValue(offset);

        // The MVC Result object was tainted - This could still be safe if the content-type is a safe one
        if (!mvcResultTaint.isSafe()) {
            // Get the value of the content-type parameter
            Taint parameterTaint = fact.getStackValue(0);

            if ( !parameterTaint.isSafe()
                    || VULNERABLE_CONTENT_TYPE.equalsIgnoreCase(parameterTaint.getConstantValue())) {
                return getPriority(mvcResultTaint);
            }
        }

        return Priorities.IGNORE_PRIORITY;
    }

    @Override
    protected int getPriority(Taint taint) {
        if (!taint.isSafe() && taint.hasTag(Taint.Tag.XSS_SAFE)) {
            if (FindSecBugsGlobalConfig.getInstance().isReportPotentialXssWrongContext()) {
                return Priorities.LOW_PRIORITY;
            } else {
                return Priorities.IGNORE_PRIORITY;
            }
        } else if (!taint.isSafe()
                && (taint.hasOneTag(Taint.Tag.QUOTE_ENCODED, Taint.Tag.APOSTROPHE_ENCODED))
                && taint.hasTag(Taint.Tag.LT_ENCODED)) {
            return Priorities.LOW_PRIORITY;
        } else {
            return super.getPriority(taint);
        }
    }
}
