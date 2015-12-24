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
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.MethodGen;

/**
 * Various flavor of XSSRequestWrapper exist to do some debatable prevention. It can be considered as a Web Application
 * Firewall.
 * <br>
 * Some implementations to detect:
 * http://java.dzone.com/articles/stronger-anti-cross-site
 * http://www.javacodegeeks.com/2012/07/anti-cross-site-scripting-xss-filter.html
 * http://ricardozuasti.com/2012/stronger-anti-cross-site-scripting-xss-filter-for-java-web-apps/
 */
public class XSSRequestWrapperDetector implements Detector {

    private static final String XSS_REQUEST_WRAPPER_TYPE = "XSS_REQUEST_WRAPPER";

    private BugReporter bugReporter;

    public XSSRequestWrapperDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        //The class extends HttpServletRequestWrapper
        boolean isRequestWrapper = InterfaceUtils.isSubtype(javaClass, "javax.servlet.http.HttpServletRequestWrapper");

        //Not the target of this detector
        if (!isRequestWrapper) return;

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {
            if (m.getName().equals("stripXSS")) {
                bugReporter.reportBug(new BugInstance(this, XSS_REQUEST_WRAPPER_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClassAndMethod(javaClass, m));
                return;
            }
        }

    }

    @Override
    public void report() {

    }
}
