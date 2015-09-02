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
import java.util.HashSet;
import java.util.Set;

/**
 * Summary of information about a method related to taint analysis
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintMethodSummary {

    private Taint outputTaint = null;
    private final Set<Integer> mutableStackIndices = new HashSet<Integer>();
    public static final TaintMethodSummary DEFAULT_TOSTRING_SUMMARY;
    public static final TaintMethodSummary SAFE_SUMMARY;
    
    static {
        DEFAULT_TOSTRING_SUMMARY = new TaintMethodSummary();
        DEFAULT_TOSTRING_SUMMARY.outputTaint = new Taint(Taint.State.UNKNOWN);
        DEFAULT_TOSTRING_SUMMARY.outputTaint.addTaintParameter(0);
        SAFE_SUMMARY = new TaintMethodSummary();
        SAFE_SUMMARY.outputTaint = new Taint(Taint.State.SAFE);
    }
    
    public TaintMethodSummary() {
    }
    
    public Collection<Integer> getMutableStackIndeces() {
        if (!hasMutableStackIndeces()) {
            throw new IllegalStateException("stack indeces not set");
        }
        return mutableStackIndices;
    }

    public boolean hasMutableStackIndeces() {
        assert mutableStackIndices != null;
        return !mutableStackIndices.isEmpty();
    }
    
    public void addMutableStackIndex(int mutableStackIndex) {
        mutableStackIndices.add(mutableStackIndex);
    }
    
    public Taint getOutputTaint() {
        return outputTaint;
    }
    
    public void setOuputTaint(Taint outputTaint) {
        this.outputTaint = outputTaint;
    }

    public static TaintMethodSummary getDefaultConstructorSummary(int stackSize) {
        TaintMethodSummary summary = new TaintMethodSummary();
        summary.outputTaint = new Taint(Taint.State.UNKNOWN);
        summary.mutableStackIndices.add(stackSize);
        return summary;
    }
    
    /*public static TaintMethodSummary getUnknownMethodSummary(Collection<Integer> indices) {
        TaintMethodSummary summary = new TaintMethodSummary();
        summary.outputTaint = new Taint(Taint.State.UNKNOWN);
        summary.mutableStackIndices.addAll(indices);
        return summary;
    }*/
    
    public boolean isInformative() {
        if (this == DEFAULT_TOSTRING_SUMMARY || this == SAFE_SUMMARY) {
            // these are loaded automatically, do not need to store them
            return false;
        }
        if (outputTaint == null) {
            return false;
        }
        if (!outputTaint.isUnknown()) {
            return true;
        }
        return outputTaint.hasTaintParameters();
    }
    
    @Override
    public String toString() {
        if (outputTaint == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (outputTaint.isUnknown() && outputTaint.hasTaintParameters()) {
            appendJoined(sb, outputTaint.getTaintParameters());
            Taint nonParametricTaint = outputTaint.getNonParametricTaint();
            if (nonParametricTaint != null) {
                sb.append(",").append(nonParametricTaint.getState().name());
            }
        } else {
            sb.append(outputTaint.getState().name());
        }
        if (hasMutableStackIndeces()) {
            sb.append("#");
            appendJoined(sb, mutableStackIndices);
        }
        return sb.toString();
    }
    
    private static void appendJoined(StringBuilder sb, Collection<Integer> objects) {
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
     * @param str (state or parameter indeces to merge separated by comma)#mutable position
     * @return initialized object with taint method summary
     * @throws java.io.IOException for bad format of paramter
     */
    public static TaintMethodSummary load(String str) throws IOException {
        str = str.trim();
        String[] tuple = str.split("#");
        TaintMethodSummary summary = new TaintMethodSummary();
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
        if (str.isEmpty()) {
            throw new IOException("No taint information set");
        } else if (isTaintStateValue(str)) {
            summary.setOuputTaint(new Taint(Taint.State.valueOf(str)));
        } else {
            tuple = str.split(",");
            int count = tuple.length;
            Taint taint = new Taint(Taint.State.UNKNOWN);
            for (int i = 0; i < count; i++) {
                try {
                    taint.addTaintParameter(Integer.parseInt(tuple[i].trim()));
                } catch (NumberFormatException ex) {
                    throw new IOException("Cannot parse parameter offset " + i, ex);
                }
            }
            summary.setOuputTaint(taint);
        }
        return summary;
    }
    
    private static boolean isTaintStateValue(String value) {
        Taint.State[] states = Taint.State.values();
        for (Taint.State state : states) {
            if (state.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
