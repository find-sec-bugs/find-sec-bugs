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
 * Summary of information about a class field related to taint analysis,
 * allows to configure default behavior for class fields.
 *
 * Default configuration is mutable class with null taint state.
 *
 * @author Tomas Polesovsky (Liferay, Inc.)
 */
public class TaintFieldConfig implements TaintTypeConfig {
    public static final Taint.State DEFAULT_TAINT_STATE = Taint.State.NULL;
    private Taint.State taintState = DEFAULT_TAINT_STATE;
    private String typeSignature;
    private static final Pattern typePattern;
    private static final Pattern taintConfigPattern;

    static {
        String javaIdentifierRegex = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
        String classWithPackageRegex = javaIdentifierRegex+"(\\/"+javaIdentifierRegex+")*";
        String typeRegex = classWithPackageRegex + "\\." + javaIdentifierRegex;
        typePattern = Pattern.compile(typeRegex);

        String taintConfigRegex = "([A-Z_]+)";
        taintConfigPattern = Pattern.compile(taintConfigRegex);
    }

    public static boolean accepts(String typeSignature, String taintConfig) {
        return typePattern.matcher(typeSignature).matches() && taintConfigPattern.matcher(taintConfig).matches();
    }

    /**
     * Loads class field summary from String<br/>
     * <br/>
     * The summary should have the following syntax:<br />
     * <code>defaultTaintState #IMMUTABLE</code>, where <ol>
     *     <li><code>defaultTaintState</code> means the Taint state for type casting and return types. Usually <code>SAFE</code> is used to specify classes that cannot contain injection escape characters</li>
     *     <li><code>#IMMUTABLE</code> flags is used for classes that cannot be subject to taint state mutation during taint analysis</li>
     *     <li>at least one of two above are required</li>
     * </ol>
     *
     * Example: <br/>
     * <code>Ljava/lang/Boolean;:SAFE#IMMUTABLE</code><br />
     * <ul>
     *     <li>Here the summary is: <code>SAFE#IMMUTABLE</code></li>
     *     <li>When a object is casted to Boolean or Boolean is a method result type, the taint state will be always SAFE</li>
     *     <li>When applying taint mutation to method arguments, Boolean arguments cannot change taint state</li>
     *     <li>Practically, Booleans cannot transfer characters that could cause injections and thus are SAFE as return types and casts</li>
     * </ul>
     *
     * Example: <br/>
     * <code>Ljava/lang/String;:#IMMUTABLE</code><br />
     * <ul>
     *     <li>String is immutable class and therefore String method arguments cannot change taint state</li>
     *     <li>Practically, String can carry injection sensitive characters but is always immutable</li>
     * </ul>
     *
     * Example: <br/>
     * <code>Ljava/util/concurrent/atomic/AtomicBoolean;:SAFE</code><br />
     * <ul>
     *     <li>AtomicBoolean value can be changed but cannot carry injection sensitive value</li>
     * </ul>
     *
     * @param taintConfig <code>state#IMMUTABLE</code>, where state is one of Taint.STATE or empty
     * @return initialized object with taint class summary
     * @throws IOException for bad format of parameter
     * @throws NullPointerException if argument is null
     */
    @Override
    public TaintFieldConfig load(String taintConfig) throws IOException {
        if (taintConfig == null) {
            throw new NullPointerException("Taint config is null");
        }
        taintConfig = taintConfig.trim();
        if (taintConfig.isEmpty()) {
            throw new IOException("No taint class config specified");
        }

        if (!taintConfig.isEmpty()) {
            taintState = Taint.State.valueOf(taintConfig);
        }

        return this;
    }

    public Taint.State getTaintState() {
        return taintState;
    }

    public Taint.State getTaintState(Taint.State defaultState) {
        if (taintState.equals(DEFAULT_TAINT_STATE)) {
            return defaultState;
        }

        return taintState;
    }

    /**
     * Set full class and method signature for the analyzed method
     *
     * @param typeSignature method signature
     */
    public void setTypeSignature(String typeSignature) {
        this.typeSignature = typeSignature;
    }

    /**
     * Returns the analyzed method full signature
     *
     * @return signature of the method
     */
    public String getTypeSignature() {
        return typeSignature;
    }
}
