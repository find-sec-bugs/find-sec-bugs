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

import com.h3xstream.findsecbugs.BCELUtil;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.SignatureParser;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    private final Map<String, TaintClassConfig> taintClassConfigMap = new HashMap<String, TaintClassConfig>();
    private final Map<String, TaintFieldConfig> taintFieldConfigMap = new HashMap<String, TaintFieldConfig>();
    private final Map<String, TaintMethodConfigWithArgumentsAndLocation> taintMethodConfigWithArgumentsAndLocationMap =
            new HashMap<String, TaintMethodConfigWithArgumentsAndLocation>();

    private final Map<String, Taint> staticFieldsTaint = new HashMap<String, Taint>();

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
            public void receiveTaintConfig(String typeSignature, String config) throws IOException {
                if (TaintMethodConfig.accepts(typeSignature, config)) {
                    if (checkRewrite && containsKey(typeSignature)) {
                        throw new IllegalStateException("Config for " + typeSignature + " already loaded");
                    }
                    TaintMethodConfig taintMethodConfig = new TaintMethodConfig(true).load(config);
                    taintMethodConfig.setTypeSignature(typeSignature);
                    put(typeSignature, taintMethodConfig);
                    return;
                }

                if (TaintClassConfig.accepts(typeSignature, config)) {
                    if (checkRewrite && taintClassConfigMap.containsKey(typeSignature)) {
                        throw new IllegalStateException("Config for " + typeSignature + " already loaded");
                    }
                    TaintClassConfig taintClassConfig = new TaintClassConfig().load(config);
                    taintClassConfig.setTypeSignature(typeSignature);
                    taintClassConfigMap.put(typeSignature, taintClassConfig);
                    return;
                }

                if (TaintFieldConfig.accepts(typeSignature, config)) {
                    if (checkRewrite && taintFieldConfigMap.containsKey(typeSignature)) {
                        throw new IllegalStateException("Config for " + typeSignature + " already loaded");
                    }
                    TaintFieldConfig taintFieldConfig = new TaintFieldConfig().load(config);
                    taintFieldConfig.setTypeSignature(typeSignature);
                    taintFieldConfigMap.put(typeSignature, taintFieldConfig);
                    return;
                }

                if (TaintMethodConfigWithArgumentsAndLocation.accepts(typeSignature, config)) {
                    if (checkRewrite && taintMethodConfigWithArgumentsAndLocationMap.containsKey(typeSignature)) {
                        throw new IllegalStateException("Config for " + typeSignature + " already loaded");
                    }

                    TaintMethodConfigWithArgumentsAndLocation methodConfig =
                            new TaintMethodConfigWithArgumentsAndLocation().load(config);

                    methodConfig.setTypeSignature(typeSignature);

                    String key = typeSignature + '@' + methodConfig.getLocation();
                    taintMethodConfigWithArgumentsAndLocationMap.put(key, methodConfig);
                    return;
                }

                throw new IllegalArgumentException("Invalid signature " + typeSignature + " configured");
            }
        });
    }


    public boolean isClassImmutable(String typeSignature) {
        if (!isClassType(typeSignature)) {
            return false;
        }

        TaintClassConfig classConfig = taintClassConfigMap.get(typeSignature);
        if (classConfig == null) {
            return false;
        }

        return classConfig.isImmutable();
    }

    public boolean isClassTaintSafe(String typeSignature) {
        if (!isClassType(typeSignature)) {
            return false;
        }

        TaintClassConfig taintClassConfig = getTaintClassConfig(typeSignature);
        if (taintClassConfig == null) {
            return false;
        }

        return taintClassConfig.getTaintState().equals(Taint.State.SAFE);
    }

    public Taint.State getClassTaintState(String typeSignature, Taint.State defaultState) {
        if (!isClassType(typeSignature)) {
            return defaultState;
        }

        TaintClassConfig taintClassConfig = getTaintClassConfig(typeSignature);

        if (taintClassConfig == null) {
            return defaultState;
        }

        Taint.State taintClassConfigState = taintClassConfig.getTaintState();

        if (taintClassConfigState.equals(TaintClassConfig.DEFAULT_TAINT_STATE)) {
            return defaultState;
        }

        return taintClassConfigState;
    }

    public Taint.State getFieldTaintState(String fieldSignature, Taint.State defaultState) {
        if (!isFieldType(fieldSignature)) {
            return defaultState;
        }

        TaintFieldConfig taintFieldConfig = taintFieldConfigMap.get(fieldSignature);

        if (taintFieldConfig == null) {
            return defaultState;
        }

        Taint.State taintFieldConfigState = taintFieldConfig.getTaintState();

        if (taintFieldConfigState.equals(TaintClassConfig.DEFAULT_TAINT_STATE)) {
            return defaultState;
        }

        return taintFieldConfigState;
    }

    public TaintClassConfig getTaintClassConfig(String typeSignature) {
        if (!isClassType(typeSignature)) {
            return null;
        }

        return taintClassConfigMap.get(typeSignature);
    }

    private boolean isClassType(String typeSignature) {
        return typeSignature != null && typeSignature.length() > 2 && typeSignature.charAt(0) == 'L';
    }

    private boolean isFieldType(String typeSignature) {
        return typeSignature != null && typeSignature.length() > 2 && typeSignature.charAt(0) != 'L';
    }

    public TaintMethodConfig getMethodConfig(TaintFrame frame, MethodDescriptor methodDescriptor, String className, String methodId) {
        TaintMethodConfig taintMethodConfig = getTaintMethodConfigWithArgumentsAndLocation(frame, methodDescriptor, className, methodId);

        if (taintMethodConfig == null) {
            taintMethodConfig = get(className.concat(methodId));
        }

        if (taintMethodConfig == null) {
            taintMethodConfig = getSuperMethodConfig(className, methodId);
        }

        return taintMethodConfig;
    }

    public TaintMethodConfig getSuperMethodConfig(String className, String methodId) {
        try {
            if (className.endsWith("]")) {
                // not a real class
                return null;
            }

            JavaClass javaClass = Repository.lookupClass(className);
            assert javaClass != null;

            Set<String> parentClassNames = BCELUtil.getParentClassNames(javaClass);

            for (String parentClassName : parentClassNames) {
                TaintMethodConfig conf = get(parentClassName.concat(methodId));
                if (conf != null) {
                    return conf;
                }
            }

        } catch (ClassNotFoundException ex) {
            AnalysisContext.reportMissingClass(ex);
        }

        return null;
    }

    private TaintMethodConfig getTaintMethodConfigWithArgumentsAndLocation(TaintFrame frame, MethodDescriptor methodDescriptor, String className, String methodId) {
        if (taintMethodConfigWithArgumentsAndLocationMap.isEmpty()) {
            return null;
        }

        String signature = methodId.substring(methodId.indexOf("("), methodId.length());
        int parameters = new SignatureParser(signature).getNumParameters();
        StringBuffer sb = null;
        if (parameters > 0 && frame.getStackDepth() >= parameters) {
            sb = new StringBuffer(parameters);
            for (int i = parameters - 1; i >= 0; i--) {
                try {
                    Taint taint = frame.getStackValue(i);
                    String value = taint.getConstantValue();
                    if (value != null) {
                        sb.append('"' + value + '"');
                    }
                    else {
                        sb.append(taint.getState().name());
                    }
                    if (i > 0) {
                        sb.append(',');
                    }
                }
                catch (DataflowAnalysisException e) {
                    assert false : e.getMessage();
                }
            }
        }

        String arguments = sb != null ? sb.toString() : "";
        String methodName = methodId.substring(1, methodId.indexOf('('));
        String methodDefinition = className + "." + methodName + "(" + arguments + ")";
        String key = methodDefinition + "@" + methodDescriptor.getSlashedClassName();
        return taintMethodConfigWithArgumentsAndLocationMap.get(key);
    }

    public Taint getStaticFieldTaint(String fieldSignature, Taint defaultValue) {
        if (!isFieldType(fieldSignature)) {
            return defaultValue;
        }

        return staticFieldsTaint.getOrDefault(fieldSignature, defaultValue);
    }

    public void putStaticFieldTaint(String fieldSignature, Taint t) {
        if (!isFieldType(fieldSignature)) {
            return;
        }

        staticFieldsTaint.put(fieldSignature, t);
    }


}
