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

import com.h3xstream.findsecbugs.common.InterfaceUtils;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.ClassContext;

public class XssJspDetector extends BasicXssInjectionDetector {

    private static final String XSS_JSP_PRINT_TYPE = "XSS_JSP_PRINT";

    public static final String[] JSP_PARENT_CLASSES = {
        "org.apache.jasper.runtime.HttpJspBase",
        "weblogic.servlet.jsp.JspBase"
    };

    public XssJspDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("xss-jsp.txt", XSS_JSP_PRINT_TYPE);
    }
    
    @Override
    public boolean shouldAnalyzeClass(ClassContext classContext) {
        String className = classContext.getClassDescriptor().getDottedClassName();
        return InterfaceUtils.isSubtype(className, JSP_PARENT_CLASSES);
    }
}
