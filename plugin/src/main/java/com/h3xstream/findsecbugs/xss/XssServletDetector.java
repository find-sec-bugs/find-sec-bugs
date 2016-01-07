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
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Hierarchy;

public class XssServletDetector extends BasicInjectionDetector {

    //private static final String XSS_JSP_PRINT_TYPE = "XSS_JSP_PRINT";
    private static final String XSS_SERVLET_TYPE = "XSS_SERVLET";

    public XssServletDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("xss-servlet.txt", XSS_SERVLET_TYPE);
    }

    @Override
    public boolean shouldAnalyzeClass(ClassContext classContext) {

        String className = classContext.getClassDescriptor().getDottedClassName();
        return InterfaceUtils.isSubtype(className, "javax.servlet.http.HttpServlet")  && !InterfaceUtils.isSubtype(className, XssJspDetector.JSP_PARENT_CLASSES);
    }
}
