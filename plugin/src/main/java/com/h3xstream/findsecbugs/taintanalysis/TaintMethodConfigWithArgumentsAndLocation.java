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
        String javaIdentifierRegex = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
        String classNameRegex = javaIdentifierRegex+"(\\/"+javaIdentifierRegex+")*";
        String methodRegex = "(("+javaIdentifierRegex+"(\\$extension)?)|(<init>))";

        // javax/servlet/http/HttpServletRequest.getAttribute("applicationConstant"):SAFE@org/apache/jsp/edit_jsp
        // javax/servlet/http/HttpServletRequest.getAttribute(UNKNOWN):SAFE@org/apache/jsp/constants_jsp
        String stringConstantRegex = "\"[^\"]*\"";
        String enumNameRegex = "[A-Z_]+";
        String methodArguments = "(" + stringConstantRegex + ",?|" + enumNameRegex + ",?)*";
        String methodWithStringConstantOrEnumRegex = classNameRegex + "\\." + methodRegex + "\\(" + methodArguments + "\\)";
        methodWithStringConstantOrEnumPattern = Pattern.compile(methodWithStringConstantOrEnumRegex);
    }

    /**
     * Constructs an empty configured summary
     */
    public TaintMethodConfigWithArgumentsAndLocation() {
        super(true);
    }

    public static boolean accepts(String typeSignature, String config) {
        int pos = config.lastIndexOf('@');
        if (pos < 0) {
            return false;
        }
        config = config.substring(0, pos);

        return methodWithStringConstantOrEnumPattern.matcher(typeSignature).matches() &&
                TaintMethodConfig.configPattern.matcher(config).matches();
    }

    /**
     * Loads method config from String, the method config contains a current class as the context<br />
     * <br />
     * The method accepts syntax similar to {@link TaintMethodConfig#load(String)} with small difference.<br />
     * The summary must ends with '@' character followed by class name<br />
     * @param taintConfig method summary with syntax described above
     * @return initialized object with taint method summary
     * @throws IOException for bad format of parameter
     * @throws NullPointerException if argument is null
     */
    @Override
    public TaintMethodConfigWithArgumentsAndLocation load(String taintConfig) throws IOException {
        if (taintConfig == null) {
            throw new NullPointerException("String is null");
        }
        taintConfig = taintConfig.trim();
        if (taintConfig.isEmpty()) {
            throw new IOException("No taint method config specified");
        }

        int locationPos = taintConfig.lastIndexOf('@');
        if (locationPos < 0) {
            throw new IOException("Bad format: @ expected");
        }

        location = taintConfig.substring(locationPos + 1).trim();
        taintConfig = taintConfig.substring(0, locationPos);

        super.load(taintConfig);

        return this;
    }

    public String getLocation() {
        return location;
    }
}
