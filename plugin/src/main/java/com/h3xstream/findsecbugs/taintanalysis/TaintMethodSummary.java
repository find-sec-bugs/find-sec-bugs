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
import java.util.HashSet;
import java.util.Set;

/**
 * Summary of information about a method related to taint analysis
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintMethodSummary {

    private Taint outputTaint = null;
    private final Set<Integer> mutableStackIndices;
    private final boolean isConfigured;
    public static final TaintMethodSummary SAFE_SUMMARY;

    static {
        SAFE_SUMMARY = new TaintMethodSummary(false);
        SAFE_SUMMARY.outputTaint = new Taint(Taint.State.SAFE);
    }

    /**
     * Constructs an emty summary
     * 
     * @param isConfigured true for configured summaries, false for derived
     */
    public TaintMethodSummary(boolean isConfigured) {
        outputTaint = null;
        mutableStackIndices = new HashSet<Integer>();
        this.isConfigured = isConfigured;
    }

    /**
     * Creates a copy of the summary (output taint not copied)
     * 
     * @param summary original summary to copy
     */
    public TaintMethodSummary(TaintMethodSummary summary) {
        this.mutableStackIndices = summary.mutableStackIndices;
        this.isConfigured = summary.isConfigured;
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
    public static TaintMethodSummary getDefaultConstructorSummary(int stackSize) {
        if (stackSize < 1) {
            throw new IllegalArgumentException("stack size less than 1");
        }
        TaintMethodSummary summary = new TaintMethodSummary(false);
        summary.outputTaint = new Taint(Taint.State.UNKNOWN);
        summary.mutableStackIndices.add(stackSize - 1);
        summary.mutableStackIndices.add(stackSize);
        return summary;
    }

    /**
     * Checks if the summary needs to be saved or has no information value
     * 
     * @return true if summary should be saved, false otherwise
     */
    public boolean isInformative() {
        if (this == SAFE_SUMMARY) {
            // these are loaded automatically, do not need to store them
            return false;
        }
        if (outputTaint == null) {
            return false;
        }
        if (!outputTaint.isUnknown()) {
            return true;
        }
        if (outputTaint.hasParameters()) {
            return true;
        }
        if (outputTaint.getRealInstanceClass() != null) {
            return true;
        }
        if (outputTaint.hasTags() || outputTaint.isRemovingTags()) {
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
            return "";
        }
        StringBuilder sb = new StringBuilder();
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

    /**
     * Loads method summary from String
     * 
     * @param str (state or parameter indices to merge separated by comma)#mutable position
     * @return initialized object with taint method summary
     * @throws java.io.IOException for bad format of paramter
     * @throws NullPointerException if argument is null
     */
    public static TaintMethodSummary load(String str) throws IOException {
        if (str == null) {
            throw new NullPointerException("string is null");
        }
        str = str.trim();
        if (str.isEmpty()) {
            throw new IOException("No taint method summary specified");
        }
        TaintMethodSummary summary = new TaintMethodSummary(true);
        str = loadMutableStackIndeces(str, summary);
        String[] tuple = str.split("\\|");
        if (tuple.length == 2) {
            str = tuple[0];
        } else if (tuple.length != 1) {
            throw new IOException("Bad format: only one '|' expected");
        }
        loadStatesAndParameters(str, summary);
        if (tuple.length == 2) {
            loadTags(tuple[1], summary);
        }
        return summary;
    }

    private static String loadMutableStackIndeces(String str, TaintMethodSummary summary) throws IOException {
        String[] tuple = str.split("#");
        if (tuple.length == 2) {
            str = tuple[0];
            try {
                String[] indices = tuple[1].split(",");
                for (String index : indices) {
                    summary.addMutableStackIndex(Integer.parseInt(index.trim()));
                }
            } catch (NumberFormatException ex) {
                throw new IOException("Cannot parse mutable stack offsets", ex);
            }
        } else if (tuple.length != 1) {
            throw new IOException("Bad format: only one '#' expected");
        }
        return str;
    }
    
    private static void loadStatesAndParameters(String str, TaintMethodSummary summary) throws IOException {
        if (str.isEmpty()) {
            throw new IOException("No taint information set");
        } else if (isTaintStateValue(str)) {
            summary.setOuputTaint(Taint.valueOf(str));
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
            summary.setOuputTaint(taint);
        }
    }

    private static void loadTags(String tagInfo, TaintMethodSummary summary) throws IOException {
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
            if (summary.outputTaint.hasTag(tag) || summary.outputTaint.getTagsToRemove().contains(tag)) {
                throw new IOException("Bad format: tag " + tag + " already present");
            }
            switch (sign) {
                case '+':
                    summary.outputTaint.addTag(tag);
                    break;
                case '-':
                    summary.outputTaint.removeTag(tag);
                    break;
                default:
                    throw new IOException("Bad format: taint tag sign must be + or - but is " + sign);
            }
        }
    }
    
    private static boolean isTaintTagValue(String value) {
        assert value != null && !value.isEmpty();
        for (Taint.Tag tag : Taint.Tag.values()) {
            if (tag.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isTaintStateValue(String value) {
        assert value != null && !value.isEmpty();
        Taint.State[] states = Taint.State.values();
        for (Taint.State state : states) {
            if (state.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
