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
import java.util.ArrayList;
import java.util.Collection;

/**
 * Summary of information about a method related to taint analysis
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintMethodSummary {

    private Collection<Integer> transferParameters = null;
    private Taint outputTaint = null;
    private static final int INVALID_INDEX = -1;
    private int mutableStackIndex = INVALID_INDEX;
    private static final TaintMethodSummary defaultToStringSummary = new TaintMethodSummary();
    
    static {
        ArrayList<Integer> params = new ArrayList<Integer>(1);
        params.add(0);
        defaultToStringSummary.setTransferParameters(params);
    }
    
    public TaintMethodSummary() {
    }
    
    public Collection<Integer> getTransferParameters() {
        if (!hasTransferParameters()) {
            throw new IllegalStateException("transfer parameters not set");
        }
        return transferParameters;
    }

    public boolean hasTransferParameters() {
        return transferParameters != null && !transferParameters.isEmpty();
    }
    
    public void setTransferParameters(Collection<Integer> transferParameters) {
        this.transferParameters = transferParameters;
    }

    public int getMutableStackIndex() {
        if (!hasMutableStackIndex()) {
            throw new IllegalStateException("stack index not set");
        }
        return mutableStackIndex;
    }

    public boolean hasMutableStackIndex() {
        return mutableStackIndex != INVALID_INDEX;
    }
    
    public void setMutableStackIndex(int mutableStackIndex) {
        this.mutableStackIndex = mutableStackIndex;
    }
    
    public Taint getOutputTaint() {
        if (!hasConstantOutputTaint()) {
            throw new IllegalStateException("output taint is not set");
        }
        return outputTaint;
    }
    
    public boolean hasConstantOutputTaint() {
        return outputTaint != null;
    }
    
    public void setOuputTaint(Taint outputTaint) {
        this.outputTaint = outputTaint;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (hasConstantOutputTaint()) {
            sb.append(outputTaint.getState().name());
        } else if (hasTransferParameters()) {
            int count = transferParameters.size();
            Integer[] array = transferParameters.toArray(new Integer[count]);
            sb.append(array[0]);
            for (int i = 1; i < count; i++) {
                sb.append(",");
                sb.append(array[i]);
            }
        } else {
            throw new IllegalStateException("output taint nor parameters not set");
        }
        if (hasMutableStackIndex()) {
            sb.append("#");
            sb.append(mutableStackIndex);
        }
        return sb.toString();
    }
    
    public static TaintMethodSummary getDefaultToStringSummary() {
        return defaultToStringSummary;
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
                summary.setMutableStackIndex(Integer.parseInt(tuple[1]));
            } catch (NumberFormatException ex) {
                throw new IOException("Cannot parse mutable stack offset", ex);
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
            Collection<Integer> parameters = new ArrayList<Integer>(count);
            for (int i = 0; i < count; i++) {
                try {
                    parameters.add(Integer.parseInt(tuple[i].trim()));
                } catch (NumberFormatException ex) {
                    throw new IOException("Cannot parse parameter offset " + i, ex);
                }
            }
            summary.setTransferParameters(parameters);
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
