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
package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.ClassContext;

public class OgnlInjectionDetector extends BasicInjectionDetector {

    /**
     * The utility class from Struts2 are skip to avoid false positive.
     */
    private static final String[] STRUTS_UTILITY_CLASSES = {
            "com.opensymphony.xwork2.util.TextParseUtil",
            "com.opensymphony.xwork2.util.TextParser",
            "com.opensymphony.xwork2.util.OgnlTextParser",
            "com.opensymphony.xwork2.ognl.OgnlReflectionProvider",
            "com.opensymphony.xwork2.util.reflection.ReflectionProvider",
            "com.opensymphony.xwork2.ognl.OgnlUtil",
            "org.apache.struts2.util.VelocityStrutsUtil",
            "org.apache.struts2.util.StrutsUtil",
            "org.apache.struts2.views.jsp.ui.OgnlTool"
    };

    public OgnlInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("struts2.txt", "OGNL_INJECTION");
    }

    @Override
    public boolean shouldAnalyzeClass(ClassContext classContext) {
        String className = classContext.getClassDescriptor().getDottedClassName();
        return !InterfaceUtils.isSubtype(className, STRUTS_UTILITY_CLASSES);
    }
}
