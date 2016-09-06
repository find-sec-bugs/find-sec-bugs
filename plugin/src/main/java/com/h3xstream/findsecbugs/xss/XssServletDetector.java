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

import org.apache.bcel.generic.ConstantPoolGen;

import com.h3xstream.findsecbugs.common.InterfaceUtils;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.ClassContext;

public class XssServletDetector extends BasicXssInjectionDetector {

    private static final String XSS_SERVLET_TYPE = "XSS_SERVLET";
    private static final String[] REQUIRED_CLASSES = {
        "Ljavax/servlet/http/ServletResponse;",
        "Ljavax/servlet/http/ServletResponseWrapper;",
        "Ljavax/servlet/http/HttpServletResponse;",
        "Ljavax/servlet/http/HttpServletResponseWrapper;"
    };

    public XssServletDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("xss-servlet.txt", XSS_SERVLET_TYPE);
    }
    
    @Override
    public boolean shouldAnalyzeClass(ClassContext classContext) {
        ConstantPoolGen constantPoolGen = classContext.getConstantPoolGen();
        for (String requiredClass : REQUIRED_CLASSES) {
            if (constantPoolGen.lookupUtf8(requiredClass) != -1) {
                String className = classContext.getClassDescriptor().getDottedClassName();
                return !InterfaceUtils.isSubtype(className, XssJspDetector.JSP_PARENT_CLASSES);
            }
        }
        return false;
    }
}
