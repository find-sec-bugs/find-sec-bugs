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

import edu.umd.cs.findbugs.ba.AnalysisContext;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Map of taint summaries for all known methods and classes
 *
 * This class extends HashMap:
 * <ul>
 *  <li>The key is the method signature (ie :
 org/hibernate/Session.createQuery(Ljava/lang/String;)Lorg/hibernate/Query;)</li>
 *  <li>The value is the behavior of the method
 *  ("0" for param index 0 is tainted,
 *  "UNKNOWN" if the method does not become tainted base on the value,
 *  "TAINTED" if the result must be consider unsafe)</li>
 * </ul>
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintConfig extends HashMap<String, TaintMethodConfig> {
    
    private static final long serialVersionUID = 1L;
    private final Map<String, TaintClassConfig> taintClassSummaryMap = new HashMap<String, TaintClassConfig>();

    /**
     * Dumps all the summaries for debugging
     * 
     * @param output stream where to output the summaries
     */
    public void dump(PrintStream output) {
        TreeSet<String> keys = new TreeSet<String>(keySet());
        for (String key : keys) {
            output.println(key + ":" + get(key));
        }
    }

    /**
     * Loads summaries from stream checking the format
     * 
     * @param input input stream of configured summaries
     * @param checkRewrite whether to check duplicit summaries
     * @throws IOException if cannot read the stream or the format is bad
     * @throws IllegalArgumentException for bad method format
     * @throws IllegalStateException if there are duplicit configurations
     */
    public void load(InputStream input, final boolean checkRewrite) throws IOException {
        new TaintConfigLoader().load(input, new TaintConfigLoader.TaintConfigReceiver() {
            @Override
            public void receiveTaintConfigSummary(String typeSignature, String summary) throws IOException {
                if (TaintMethodConfig.accepts(typeSignature, summary)) {
                    if (checkRewrite && containsKey(typeSignature)) {
                        throw new IllegalStateException("Summary for " + typeSignature + " already loaded");
                    }
                    TaintMethodConfig taintMethodSummary = new TaintMethodConfig(true).load(summary);
                    put(typeSignature, taintMethodSummary);
                    return;
                }

                if (TaintClassConfig.accepts(typeSignature, summary)) {
                    if (checkRewrite && taintClassSummaryMap.containsKey(typeSignature)) {
                        throw new IllegalStateException("Summary for " + typeSignature + " already loaded");
                    }
                    TaintClassConfig taintClassSummary = new TaintClassConfig().load(summary);
                    taintClassSummaryMap.put(typeSignature, taintClassSummary);
                    return;
                }

                throw new IllegalArgumentException("Invalid full method name " + typeSignature + " configured");
            }
        });
    }


    public boolean isClassImmutable(String typeSignature) {
        if (!isClassType(typeSignature)) {
            return false;
        }

        TaintClassConfig summary = taintClassSummaryMap.get(typeSignature);
        if (summary == null) {
            return false;
        }

        return summary.isImmutable();
    }

    public boolean isClassTaintSafe(String typeSignature) {
        if (!isClassType(typeSignature)) {
            return false;
        }

        TaintClassConfig taintClassSummary = getClassSummary(typeSignature);
        if (taintClassSummary == null) {
            return false;
        }

        return taintClassSummary.getTaintState().equals(Taint.State.SAFE);
    }

    public Taint.State getClassTaintState(String typeSignature, Taint.State defaultState) {
        if (!isClassType(typeSignature)) {
            return defaultState;
        }

        TaintClassConfig taintClassSummary = getClassSummary(typeSignature);

        if (taintClassSummary == null) {
            return defaultState;
        }

        Taint.State classSummaryTaintState = taintClassSummary.getTaintState();

        if (classSummaryTaintState.equals(TaintClassConfig.DEFAULT_TAINT_STATE)) {
            return defaultState;
        }

        return classSummaryTaintState;
    }

    public TaintClassConfig getClassSummary(String typeSignature) {
        if (!isClassType(typeSignature)) {
            return null;
        }

        return taintClassSummaryMap.get(typeSignature);
    }

    private boolean isClassType(String typeSignature) {
        return typeSignature != null && typeSignature.length() > 2 && typeSignature.charAt(0) == 'L';
    }

    public TaintMethodConfig getMethodSummary(String className, String methodId) {
        TaintMethodConfig taintMethodSummary = get(className.concat(methodId));

        if (taintMethodSummary == null) {
            taintMethodSummary = getSuperMethodSummary(className, methodId);
        }

        return taintMethodSummary;
    }

    public TaintMethodConfig getSuperMethodSummary(String className, String methodId) {
        try {
            if (className.endsWith("]")) {
                // not a real class
                return null;
            }
            JavaClass javaClass = Repository.lookupClass(className);
            assert javaClass != null;
            TaintMethodConfig summary = getSuperMethodSummary(javaClass.getSuperClasses(), methodId);
            if (summary != null) {
                return summary;
            }
            return getSuperMethodSummary(javaClass.getAllInterfaces(), methodId);
        } catch (ClassNotFoundException ex) {
            AnalysisContext.reportMissingClass(ex);
            return null;
        }
    }

    private TaintMethodConfig getSuperMethodSummary(JavaClass[] javaClasses, String method) {
        assert javaClasses != null;
        for (JavaClass classOrInterface : javaClasses) {
            String fullMethodName = classOrInterface.getClassName().replace('.', '/').concat(method);
            TaintMethodConfig summary = get(fullMethodName);
            if (summary != null) {
                return summary;
            }
        }
        return null;
    }
}
