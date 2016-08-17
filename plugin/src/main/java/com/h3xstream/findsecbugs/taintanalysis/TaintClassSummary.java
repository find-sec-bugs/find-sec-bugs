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
 * Summary of information about a class related to taint analysis,
 * allows to configure default behavior for return types and type casts.
 *
 * Default configuration is mutable class with null taint state.
 *
 * @author Tomas Polesovsky (Liferay, Inc.)
 */
public class TaintClassSummary {
    private static final String IMMUTABLE = "#IMMUTABLE";
    private Taint.State taintState = Taint.State.NULL;
    private boolean immutable;
    private static final Pattern typePattern;

    static {
        String classWithPackageRegex = "([a-z][a-z0-9]*\\/)*[A-Z][a-zA-Z0-9\\$]*";
        String typeRegex = "(\\[)*((L" + classWithPackageRegex + ";)|B|C|D|F|I|J|S|Z)";
        typePattern = Pattern.compile(typeRegex);
    }

    public static boolean accepts(String typeSignature) {
        return typePattern.matcher(typeSignature).matches();
    }

    /**
     * Loads class summary from String
     *
     * @param summary <code>state#IMMUTABLE</code>, where state is one of Taint.STATE or empty
     * @return initialized object with taint class summary
     * @throws java.io.IOException for bad format of parameter
     * @throws NullPointerException if argument is null
     */
    public static TaintClassSummary load(String summary) throws IOException {
        if (summary == null) {
            throw new NullPointerException("Summary is null");
        }
        summary = summary.trim();
        if (summary.isEmpty()) {
            throw new IOException("No taint class summary specified");
        }
        TaintClassSummary taintClassSummary = new TaintClassSummary();
        if (summary.endsWith(IMMUTABLE)) {
            taintClassSummary.immutable = true;
            summary = summary.substring(0, summary.length() - IMMUTABLE.length());
        }

        if (!summary.isEmpty()) {
            taintClassSummary.taintState = Taint.State.valueOf(summary);
        }

        return taintClassSummary;
    }

    public Taint.State getTaintState() {
        return taintState;
    }

    public boolean isImmutable() {
        return immutable;
    }
}
