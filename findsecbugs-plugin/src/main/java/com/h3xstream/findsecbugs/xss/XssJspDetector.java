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
package com.h3xstream.findsecbugs.xss;

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import com.h3xstream.findsecbugs.common.InterfaceUtils;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.ba.ClassContext;

public class XssJspDetector extends BasicInjectionDetector {

    private static final String XSS_JSP_PRINT_TYPE = "XSS_JSP_PRINT";

    @SuppressFBWarnings(value = "MS_MUTABLE_COLLECTION_PKGPROTECT",
            justification = "It is intended to be shared with XssServletDetector. Accidental modification of this list is unlikely.")
    protected static final String[] JSP_PARENT_CLASSES = {
        "org.apache.jasper.runtime.HttpJspBase",
        "weblogic.servlet.jsp.JspBase"
    };

    public XssJspDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("xss-jsp.txt", XSS_JSP_PRINT_TYPE);
    }

    @Override
    protected int getPriority(Taint taint) {
        if (!taint.isSafe() && taint.hasTag(Taint.Tag.XSS_SAFE)) {
            if(FindSecBugsGlobalConfig.getInstance().isReportPotentialXssWrongContext()) {
                return Priorities.LOW_PRIORITY;
            }
            else {
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

    @Override
    public boolean shouldAnalyzeClass(ClassContext classContext) {
        String className = classContext.getClassDescriptor().getDottedClassName();
        return InterfaceUtils.isSubtype(className, JSP_PARENT_CLASSES);
    }
}
