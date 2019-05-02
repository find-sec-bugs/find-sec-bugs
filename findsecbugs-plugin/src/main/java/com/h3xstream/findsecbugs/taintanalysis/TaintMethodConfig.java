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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Summary of information about a method related to taint analysis.<br />
 *<br />
 * For loading sinks files please see {@link com.h3xstream.findsecbugs.injection.SinksLoader}
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintMethodConfig implements TaintTypeConfig {

    private Taint outputTaint = null;
    private Map<Integer, Taint> parametersOutputTaints = new HashMap<>();
    private final Set<Integer> mutableStackIndices;
    private final boolean isConfigured;
    private String typeSignature;
    public static final TaintMethodConfig SAFE_CONFIG;
    protected static final Pattern fullMethodPattern;
    protected static final Pattern configPattern;

    static {
        SAFE_CONFIG = new TaintMethodConfig(false);
        SAFE_CONFIG.outputTaint = new Taint(Taint.State.SAFE);

        String javaIdentifierRegex = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
        String classWithPackageRegex = javaIdentifierRegex+"(\\/"+javaIdentifierRegex+")*";

        String typeRegex = "(\\[)*((L" + classWithPackageRegex + ";)|B|C|D|F|I|J|S|Z)";
        String returnRegex = "(V|(" + typeRegex + "))";
        String methodRegex = "(("+javaIdentifierRegex+"(\\$extension)?)|(<init>))";
        String signatureRegex = "\\((" + typeRegex + ")*\\)" + returnRegex;
        String fullMathodNameRegex = classWithPackageRegex + "\\." + methodRegex + signatureRegex;
        fullMethodPattern = Pattern.compile(fullMathodNameRegex);

        String taintStateNameRegex = "[A-Z_]+";
        String stackIndexRegex = "[0-9]+";
        String resultStateOrStackIndexRegex = "("+ taintStateNameRegex + "|" + stackIndexRegex + ")";
        String commaSeparatedTaintResultsRegex = "("+resultStateOrStackIndexRegex+",)*"+resultStateOrStackIndexRegex;

        String taintTagNameRegex = taintStateNameRegex;
        String taintTagModificationRegex = "[+-]" + taintTagNameRegex;

        String commaSeparatedTaintTagModificationRegex = "("+taintTagModificationRegex+",)*"+taintTagModificationRegex;
        String commaSeparatedStackMutationIndexesRegex = "("+stackIndexRegex+",)*" + stackIndexRegex;

        String configRegex = commaSeparatedTaintResultsRegex;
        configRegex += "(\\|" + commaSeparatedTaintTagModificationRegex + ")?";
        configRegex += "(#" + commaSeparatedStackMutationIndexesRegex+ ")?";
        configPattern = Pattern.compile(configRegex);
    }

    private boolean parametersOutputTaintsProcessed;

    /**
     * Constructs an empty summary
     *
     * @param isConfigured true for configured summaries, false for derived
     */
    public TaintMethodConfig(boolean isConfigured) {
        outputTaint = null;
        mutableStackIndices = new HashSet<Integer>();
        this.isConfigured = isConfigured;
    }

    /**
     * Creates a copy of the summary (output taint and output parameters taint not copied)
     *
     * @param config Original taint config to copy
     */
    public TaintMethodConfig(TaintMethodConfig config) {
        this.mutableStackIndices = config.mutableStackIndices;
        this.isConfigured = config.isConfigured;
        this.typeSignature = config.typeSignature;
    }

    /**
     * Returns all stack indices modified by method if there are any
     *
     * @return unmodifiable collection of indices
     * @throws IllegalStateException if there are not indices set
     */
    public Collection<Integer> getMutableStackIndices() {
        if (!hasMutableStackIndices()) {
            throw new IllegalStateException("stack indices not set");
        }
        return Collections.unmodifiableCollection(mutableStackIndices);
    }

    /**
     * Checks if there are any indices modified by method
     *
     * @return true if some index is set, false otherwise
     */
    public boolean hasMutableStackIndices() {
        assert mutableStackIndices != null;
        return !mutableStackIndices.isEmpty();
    }

    /**
     * Adds a stack index modified by method
     *
     * @param mutableStackIndex index to add
     * @throws IllegalArgumentException if index is negative
     */
    public void addMutableStackIndex(int mutableStackIndex) {
        if (mutableStackIndex < 0) {
            throw new IllegalArgumentException("negative index");
        }
        mutableStackIndices.add(mutableStackIndex);
    }

    /**
     * Returns the output taint of the method describing the taint transfer
     *
     * @return a copy of the output taint or null if not set
     */
    public Taint getOutputTaint() {
        if (outputTaint == null) {
            return null;
        }
        return new Taint(outputTaint);
    }

    /**
     * Sets the output taint of the method describing the taint transfer,
     * copy of the parameter is made and variable index is invalidated
     *
     * @param taint output taint to set
     */
    public void setOuputTaint(Taint taint) {
        if (taint == null) {
            this.outputTaint = null;
            return;
        }
        Taint taintCopy = new Taint(taint);
        taintCopy.invalidateVariableIndex();
        this.outputTaint = taintCopy;
    }

    /**
     * Constructs a default constructor summary
     * (modifies 2 stack items with UNKNOWN taint state)
     *
     * @param stackSize size of the parameter stack (including instance)
     * @return new instance of default summary
     * @throws IllegalArgumentException for stackSize &lt; 1
     */
    public static TaintMethodConfig getDefaultConstructorConfig(int stackSize) {
        if (stackSize < 1) {
            throw new IllegalArgumentException("stack size less than 1");
        }
        TaintMethodConfig config = new TaintMethodConfig(false);
        config.outputTaint = new Taint(Taint.State.UNKNOWN);
        config.mutableStackIndices.add(stackSize - 1);
        config.mutableStackIndices.add(stackSize);
        return config;
    }

    /**
     * Checks if the summary needs to be saved or has no information value
     *
     * @return true if summary should be saved, false otherwise
     */
    public boolean isInformative() {
        if (this == SAFE_CONFIG) {
            // these are loaded automatically, do not need to store them
            return false;
        }
        if (outputTaint != null && outputTaint.isInformative()) {
            return true;
        }
        if (parametersOutputTaintsProcessed) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the summary is configured or derived
     *
     * @return true if configured, false if derived
     */
    public boolean isConfigured() {
        return isConfigured;
    }

    @Override
    public String toString() {
        if (outputTaint == null) {
            return typeSignature != null ? typeSignature : "";
        }
        StringBuilder sb = new StringBuilder();
        if (typeSignature != null) {
            sb.append(typeSignature);
            sb.append(":");
        }
        if (outputTaint.isUnknown() && outputTaint.hasParameters()) {
            appendJoined(sb, outputTaint.getParameters());
            Taint.State nonParametricState = outputTaint.getNonParametricState();
            assert nonParametricState != null;
            if (nonParametricState != Taint.State.INVALID) {
                sb.append(",").append(nonParametricState.name());
            }
        } else {
            sb.append(outputTaint.getState().name());
        }
        if (outputTaint.hasTags()) {
            sb.append('|');
            boolean isFirst = true;
            for (Taint.Tag tag : outputTaint.getTags()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(',');
                }
                sb.append('+');
                sb.append(tag.name());
            }
            if (outputTaint.isRemovingTags()) {
                sb.append(',');
            }
        }
        if (outputTaint.isRemovingTags()) {
            sb.append('|');
            boolean isFirst = true;
            for (Taint.Tag tag : outputTaint.getTagsToRemove()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(',');
                }
                sb.append('-');
                sb.append(tag.name());
            }
        }
        if (hasMutableStackIndices()) {
            sb.append("#");
            appendJoined(sb, mutableStackIndices);
        }
        String realInstanceClassName = outputTaint.getRealInstanceClassName();
        if (realInstanceClassName != null) {
            sb.append(" (").append(realInstanceClassName).append(")");
        }
        return sb.toString();
    }

    private static void appendJoined(StringBuilder sb, Collection<Integer> objects) {
        assert sb != null && objects != null;
        int count = objects.size();
        Integer[] array = objects.toArray(new Integer[count]);
        sb.append(array[0]);
        for (int i = 1; i < count; i++) {
            sb.append(",").append(array[i]);
        }
    }

    public static boolean accepts(String typeSignature, String config) {
        return fullMethodPattern.matcher(typeSignature).matches() && configPattern.matcher(config).matches();
    }

    /**
     * Loads method summary from String. <br />
     * <br />
     * The summary should have the following syntax:<br />
     * <code>resultTaintState |resultTaintTags #stackMutationIndexes</code>, where <ul>
     *     <li><code>resultTaintState</code> are stack indexes or {@link Taint.State} enums separated by comma, e.g. <code>1,2</code> or <code>TAINTED</code></li>
     *     <li><code>resultTaintTags</code> are {@link Taint.Tag} enums separated by comma, started with plus or minus sign, e.g. <code>+CR_ENCODED,-XSS_SAFE</code></li>
     *     <li><code>stackMutationIndexes</code> are  stack indexes separated by comma, e.g. <code>3,4</code></li>
     * </ul>
     *
     * Example: <br/>
     * <code>org/owasp/esapi/Encoder.encodeForHTML(Ljava/lang/String;)Ljava/lang/String;:0|+XSS_SAFE,+CR_ENCODED,+LF_ENCODED</code><br />
     * <ul>
     *     <li>Here the summary is: <code>0|+XSS_SAFE,+CR_ENCODED,+LF_ENCODED</code></li>
     *     <li>The result taint will be merged with the first method argument, index 0</li>
     *     <li>The result taint will have <code>XSS_SAFE</code>, <code>CR_ENCODED</code> and <code>CR_ENCODED</code> tags set</li>
     *     <li>Practically, the result string will keep the taint but will receive XSS_SAFE tags which are processed by XssJspDetector</li>
     * </ul>
     *
     * Example: <br/>
     * <code>org/owasp/esapi/Encoder.decodeForHTML(Ljava/lang/String;)Ljava/lang/String;:0|-XSS_SAFE,-CR_ENCODED,-LF_ENCODED</code><br />
     * <ul>
     *     <li>Here the result taint will be merged with the first method argument, index 0</li>
     *     <li>The framework removes <code>XSS_SAFE</code>, <code>CR_ENCODED</code> and <code>CR_ENCODED</code> tags</li>
     *     <li>Practically, the result string will keep the taint but XSS_SAFE tag is removed again</li>
     * </ul>
     *
     * Example: <br/>
     * <code>java/lang/StringBuilder.<init>(Ljava/lang/String;)V:0#1,2</code>
     * <ul>
     *     <li>Here the result taint will be merged with the first constructor argument, index 0</li>
     *     <li>Framework also mutates taint of the StringBuilder object itself with the result taint, index 1</li>
     *     <li>Because we are in a constructor, we need to specify one more taint index => 2</li>
     *     <li>Practically, when the original String is tainted then StringBuilder will be tainted too</li>
     * </ul>
     *
     * Example: <br/>
     * <code>java/lang/StringBuilder.append(Ljava/lang/String;)Ljava/lang/StringBuilder;:0,1#1</code>
     * <ul>
     *     <li>Here the result taint will be merged with the method argument and the taint of the StringBuilder, index 0 and 1</li>
     *     <li>Framework also mutates taint of the StringBuilder object itself with the result taint, index 1</li>
     *     <li>Practically, the result taint is a merge of the String argument and previous taint of StringBuilder, on top propagates the result into StringBuilder's taint state again</li>
     * </ul>
     * Important notes about stack indexes:<ul>
     *     <li>long and double types take two slots on stack and need two subsequent indexes, i.e. index of the String parameter in <code>method(Ljava/lang/String;D)</code> is 2, not 1 as one would expect</li>
     *     <li>taint analysis adds two Taint objects on stack for constructors, don't forget to specify both</li>
     * </ul>
     *
     * @param taintConfig (state or parameter indices to merge separated by comma)#mutable position
     * @return initialized object with taint method summary
     * @throws java.io.IOException for bad format of parameter
     * @throws NullPointerException if argument is null
     */
    @Override
    public TaintMethodConfig load(String taintConfig) throws IOException {
        if (taintConfig == null) {
            throw new NullPointerException("string is null");
        }
        taintConfig = taintConfig.trim();
        if (taintConfig.isEmpty()) {
            throw new IOException("No taint method config specified");
        }
        taintConfig = loadMutableStackIndeces(taintConfig);
        String[] tuple = taintConfig.split("\\|");
        if (tuple.length == 2) {
            taintConfig = tuple[0];
        } else if (tuple.length != 1) {
            throw new IOException("Bad format: only one '|' expected");
        }
        loadStatesAndParameters(taintConfig);
        if (tuple.length == 2) {
            loadTags(tuple[1]);
        }
        return this;
    }

    private String loadMutableStackIndeces(String str) throws IOException {
        String[] tuple = str.split("#");
        if (tuple.length == 2) {
            str = tuple[0];
            try {
                String[] indices = tuple[1].split(",");
                for (String index : indices) {
                    addMutableStackIndex(Integer.parseInt(index.trim()));
                }
            } catch (NumberFormatException ex) {
                throw new IOException("Cannot parse mutable stack offsets", ex);
            }
        } else if (tuple.length != 1) {
            throw new IOException("Bad format: only one '#' expected");
        }
        return str;
    }

    private void loadStatesAndParameters(String str) throws IOException {
        if (str.isEmpty()) {
            throw new IOException("No taint information set");
        } else if (isTaintStateValue(str)) {
            setOuputTaint(Taint.valueOf(str));
        } else {
            String[] tuple = str.split(",");
            int count = tuple.length;
            Taint taint = new Taint(Taint.State.UNKNOWN);
            for (int i = 0; i < count; i++) {
                String indexOrState = tuple[i].trim();
                if (isTaintStateValue(indexOrState)) {
                    taint.setNonParametricState(Taint.State.valueOf(indexOrState));
                } else {
                    try {
                        taint.addParameter(Integer.parseInt(indexOrState));
                    } catch (NumberFormatException ex) {
                        throw new IOException("Cannot parse parameter offset " + i, ex);
                    }
                }
            }
            setOuputTaint(taint);
        }
    }

    private void loadTags(String tagInfo) throws IOException {
        if (tagInfo.isEmpty()) {
            throw new IOException("No taint tags specified");
        }
        for (String tagName : tagInfo.split(",")) {
            char sign = tagName.charAt(0);
            tagName = tagName.substring(1);
            if (!isTaintTagValue(tagName)) {
                throw new IOException("Bad format: unknown taint tag " + tagName);
            }
            Taint.Tag tag = Taint.Tag.valueOf(tagName);
            if (outputTaint.hasTag(tag) || outputTaint.getTagsToRemove().contains(tag)) {
                throw new IOException("Bad format: tag " + tag + " already present");
            }
            switch (sign) {
                case '+':
                    outputTaint.addTag(tag);
                    break;
                case '-':
                    outputTaint.removeTag(tag);
                    break;
                default:
                    throw new IOException("Bad format: taint tag sign must be + or - but is " + sign);
            }
        }
    }

    private boolean isTaintTagValue(String value) {
        assert value != null && !value.isEmpty();
        for (Taint.Tag tag : Taint.Tag.values()) {
            if (tag.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTaintStateValue(String value) {
        assert value != null && !value.isEmpty();
        Taint.State[] states = Taint.State.values();
        for (Taint.State state : states) {
            if (state.name().equals(value)) {
                return true;
            }
        }
        return false;
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

    /**
     * Stores output taint for method parameters to be used for back-propagation.<br />
     * <br />
     * Please note the stackIndex is in reverse order compared to the method parameters (and frame local variables),
     * i.e. the last method parameter has index 0.
     *
     * @param stackIndex Index of the parameter on the stack
     * @param taint Output taint of the parameter
     */
    public void setParameterOutputTaint(int stackIndex, Taint taint) {
        parametersOutputTaints.compute(
                stackIndex, (__, existingTaint) -> Taint.merge(existingTaint, taint));
    }

    /**
     * Returns computed output taints for method parameters for back-propagation.<br />
     * <br />
     * Please note the stackIndex is in reverse order compared to the method parameters (and frame local variables),
     * i.e. the last parameter has index 0.
     *
     * @return Unmodifiable copy of parameters' taints, indexed by parameter position on the stack
     */
    public Map<Integer, Taint> getParametersOutputTaints() {
        return Collections.unmodifiableMap(parametersOutputTaints);
    }

    public void setParametersOutputTaintsProcessed(boolean parametersOutputTaintsProcessed) {
        this.parametersOutputTaintsProcessed = parametersOutputTaintsProcessed;
    }

    public boolean isParametersOutputTaintsProcessed() {
        return parametersOutputTaintsProcessed;
    }
}
