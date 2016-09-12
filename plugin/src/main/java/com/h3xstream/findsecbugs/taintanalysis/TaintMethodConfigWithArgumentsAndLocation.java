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
package com.h3xstream.findsecbugs.taintanalysis;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Summary of information about a taint analysis method with configured arguments and location of the call.<br />
 * <br />
 * Can be used to fine-tune false-positives in specific classes.<br />
 * <br />
 * Examples:<br />
 * <code>javax/servlet/http/HttpServletRequest.getAttribute("applicationConstant"):SAFE@org/apache/jsp/edit_jsp</code><br />
 * <code>javax/servlet/http/HttpServletRequest.getAttribute(UNKNOWN):SAFE@org/apache/jsp/constants_jsp</code><br />
 *
 * @author Tomas Polesovsky (Liferay, Inc.)
 */
public class TaintMethodConfigWithArgumentsAndLocation extends TaintMethodConfig {
    private static final Pattern methodWithStringConstantOrEnumPattern;
    private String location;

    static {
        String classWithPackageRegex = "([a-z][a-z0-9]*\\/)*[A-Z][a-zA-Z0-9\\$]*";
        String methodRegex = "(([a-zA-Z][a-zA-Z0-9]*)|(<init>))";

        // javax/servlet/http/HttpServletRequest.getAttribute("applicationConstant"):SAFE@org/apache/jsp/edit_jsp
        // javax/servlet/http/HttpServletRequest.getAttribute(UNKNOWN):SAFE@org/apache/jsp/constants_jsp
        String stringConstantRegex = "\"[^\"]*\"";
        String enumNameRegex = "[A-Z_]+";
        String methodArguments = "(" + stringConstantRegex + ",?|" + enumNameRegex + ",?)*";
        String methodWithStringConstantOrEnumRegex = classWithPackageRegex + "\\." + methodRegex + "\\(" + methodArguments + "\\)";
        methodWithStringConstantOrEnumPattern = Pattern.compile(methodWithStringConstantOrEnumRegex);
    }

    /**
     * Constructs an empty configured summary
     */
    public TaintMethodConfigWithArgumentsAndLocation() {
        super(true);
    }

    public static boolean accepts(String typeSignature, String summary) {
        int pos = summary.lastIndexOf('@');
        if (pos < 0) {
            return false;
        }
        summary = summary.substring(0, pos);

        return methodWithStringConstantOrEnumPattern.matcher(typeSignature).matches() &&
                TaintMethodConfig.summaryPattern.matcher(summary).matches();
    }

    /**
     * Loads method summary from String, the method summary contains a current class as the context<br />
     * <br />
     * The method accepts syntax similar to {@link TaintMethodConfig#load(String)} with small difference.<br />
     * The summary must ends with '@' character followed by class name<br />
     * @param summary method summary with syntax described above
     * @return initialized object with taint method summary
     * @throws IOException for bad format of parameter
     * @throws NullPointerException if argument is null
     */
    @Override
    public TaintMethodConfigWithArgumentsAndLocation load(String summary) throws IOException {
        if (summary == null) {
            throw new NullPointerException("string is null");
        }
        summary = summary.trim();
        if (summary.isEmpty()) {
            throw new IOException("No taint method summary specified");
        }

        int locationPos = summary.lastIndexOf('@');
        if (locationPos < 0) {
            throw new IOException("Bad format: @ expected");
        }

        location = summary.substring(locationPos + 1).trim();
        summary = summary.substring(0, locationPos);

        super.load(summary);

        return this;
    }

    public String getLocation() {
        return location;
    }
}
