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
package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

/**
 * This detector cover the Servlet/HttpServlet API which give access to user input.
 * The developer should not have expectation on those value and should apply validation
 * in most cases.
 */
public class ServletEndpointDetector extends OpcodeStackDetector {

    private static final String GET_PARAMETER_TYPE = "SERVLET_PARAMETER";
    private static final String CONTENT_TYPE = "SERVLET_CONTENT_TYPE";
    private static final String SERVER_NAME_TYPE = "SERVLET_SERVER_NAME";

    private static final String SESSION_ID_TYPE = "SERVLET_SESSION_ID";
    private static final String QUERY_STRING_TYPE = "SERVLET_QUERY_STRING";
    private static final String HEADER_TYPE = "SERVLET_HEADER";
    private static final String HEADER_REFERER_TYPE = "SERVLET_HEADER_REFERER";
    private static final String HEADER_USER_AGENT_TYPE = "SERVLET_HEADER_USER_AGENT";

    private BugReporter bugReporter;

    public ServletEndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        //All call to ServletRequest
        if (seen == Const.INVOKEINTERFACE && (getClassConstantOperand().equals("javax/servlet/ServletRequest") ||
                getClassConstantOperand().equals("javax/servlet/http/HttpServletRequest"))) {

            //ServletRequest

            if (getNameConstantOperand().equals("getParameter") ||
                    getNameConstantOperand().equals("getParameterValues") ||
                    getNameConstantOperand().equals("getParameterMap") ||
                    getNameConstantOperand().equals("getParameterNames")) {

                bugReporter.reportBug(new BugInstance(this, GET_PARAMETER_TYPE, Priorities.LOW_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)
                        .addString(getNameConstantOperand())); //Passing the method name
            } else if (getNameConstantOperand().equals("getContentType")) {

                bugReporter.reportBug(new BugInstance(this, CONTENT_TYPE, Priorities.LOW_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            } else if (getNameConstantOperand().equals("getServerName")) {

                bugReporter.reportBug(new BugInstance(this, SERVER_NAME_TYPE, Priorities.LOW_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }

            //HttpServletRequest

            else if (getNameConstantOperand().equals("getRequestedSessionId")) {
                bugReporter.reportBug(new BugInstance(this, SESSION_ID_TYPE, Priorities.LOW_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            } else if (getNameConstantOperand().equals("getQueryString")) {

                bugReporter.reportBug(new BugInstance(this, QUERY_STRING_TYPE, Priorities.LOW_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            } else if (getNameConstantOperand().equals("getHeader")) {
                //Extract the value being push..
                OpcodeStack.Item top = stack.getStackItem(0);
                String value = (String) top.getConstant();//Safe see if condition
                if ("Host".equals(value)) {

                    bugReporter.reportBug(new BugInstance(this, SERVER_NAME_TYPE, Priorities.LOW_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this));
                } else if ("Referer".equalsIgnoreCase(value)) {

                    bugReporter.reportBug(new BugInstance(this, HEADER_REFERER_TYPE, Priorities.LOW_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this));
                } else if ("User-Agent".equalsIgnoreCase(value)) {

                    bugReporter.reportBug(new BugInstance(this, HEADER_USER_AGENT_TYPE, Priorities.LOW_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this));
                } else {

                    bugReporter.reportBug(new BugInstance(this, HEADER_TYPE, Priorities.LOW_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this));
                }
            }
        }
    }
}
