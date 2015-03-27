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
package com.h3xstream.findsecbugs.injection.custom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.h3xstream.findsecbugs.injection.InjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 *
 * @author naokikimura
 */
public class CustomInjectionSource implements InjectionSource {

    private static final Logger LOG = Logger.getLogger(CustomInjectionSource.class.getName());
    private static final String INJECTION_SOURCE_RESOURCE_NAME = "CustomInjectionSource.properties";
    private static final Map<String, InjectionSource> instanceMap = new HashMap<String, InjectionSource>();

    private static final String PROPERTY_SEPARATOR = "\\Q|\\E";
    private static final String PARAM_IDX_SEPARATOR = ",";

    private static final String CUSTOM_INJECTION_TYPE = "CUSTOM_INJECTION";

    static String toResourceBaseName(Class<? extends InjectionDetector> that) {
        return that.getPackage().getName().replaceAll("\\.", "/");
    }

    public static InjectionSource getInstance(Class<? extends InjectionDetector> that) {
        return getInstance(toResourceBaseName(that));
    }

    public static InjectionSource getInstance(String resourceBaseName) {
        if (instanceMap.containsKey(resourceBaseName)) {
            return instanceMap.get(resourceBaseName);
        }

        Properties properties = getInjectionSourceProperties(resourceBaseName);
        CustomInjectionSource injectionSource = new CustomInjectionSource(properties);
        instanceMap.put(resourceBaseName, injectionSource);
        return injectionSource;
    }

    static Properties getInjectionSourceProperties(String resourceBaseName) {
        String propertyName = resourceBaseName.replaceAll("/", ".") + ".sources.files";
        String propertyValue = System.getProperty(propertyName);
        String[] resourcePaths = propertyValue == null ? new String[0] : propertyValue.split("\\Q" + File.pathSeparatorChar + "\\E");
        List<URL> urls = new ArrayList<URL>(resourcePaths.length);
        for (String resourcePath : resourcePaths) {
            File file = new File(resourcePath);
            if (!file.exists()) {
                LOG.warning(file + " not found.");
                continue;
            }
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                LOG.log(Level.SEVERE, file + " did not load.", e);
            }
        }
        String resourceName = resourceBaseName + "/" + INJECTION_SOURCE_RESOURCE_NAME;
        URL url = CustomInjectionSource.class.getClassLoader().getResource(resourceName);
        if (url == null) {
            LOG.severe(resourceName + " not found.");
        }
        return loadProperties(urls.toArray(new URL[urls.size()]), loadProperties(url, null));
    }

    private static Properties loadProperties(URL url, Properties defaults) {
        Properties properties = new Properties(defaults);
        if (url == null) {
            return null;
        }

        try {
            InputStream input = url.openStream();
            properties.load(input);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, url + " did not open.", e);
        }

        return properties;
    }

    private static Properties loadProperties(URL[] urls, Properties defaults) {
        return loadProperties(urls, defaults, urls.length);
    }

    private static Properties loadProperties(URL[] urls, Properties previous, int index) {
        return index == 0 ? previous : loadProperties(urls, loadProperties(urls[--index], previous), index);
    }

    private static Map<InvokeIdentifier, InjectionPoint>  toInjectableParametersMap(Properties properties) {
        Map<InvokeIdentifier, InjectionPoint> map = new HashMap<InvokeIdentifier, InjectionPoint>(properties.size());
        for (String propertyName : properties.stringPropertyNames()) {
            //
            String[] value = properties.getProperty(propertyName).split(PROPERTY_SEPARATOR);
            String[] values = value[0].split(PARAM_IDX_SEPARATOR);
            int[] parameters = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                parameters[i] = Integer.parseInt(values[i].trim());
            }
            String bugType = value.length < 2 ? CUSTOM_INJECTION_TYPE : value[1].trim();

            InjectionPoint ip = new InjectionPoint(parameters, bugType);

            InvokeIdentifier invoke = InvokeIdentifier.valueOf(propertyName);
            ip.setInjectableMethod(invoke.className + "." + invoke.methodName);

            map.put(invoke,ip);
        }
        return map;
    }

    private static Set<String> toCandidates(Map<InvokeIdentifier, InjectionPoint> injectableParametersMap) {
        Set<String> candidates = new HashSet<String>(injectableParametersMap.keySet().size());
        for (InvokeIdentifier identifier : injectableParametersMap.keySet()) {
            candidates.add(identifier.className);
        }
        return candidates;
    }

    private final Set<String> candidates;
    private final Map<InvokeIdentifier, InjectionPoint> injectableParametersMap;

    public CustomInjectionSource(Properties properties) {
        this(toInjectableParametersMap(properties == null ? new Properties() : properties));
    }

    public CustomInjectionSource(Map<InvokeIdentifier, InjectionPoint> injectableParametersMap) {
        this.candidates = toCandidates(injectableParametersMap);
        this.injectableParametersMap = injectableParametersMap;
    }

    @Override
    public boolean isCandidate(ConstantPoolGen cpg) {
        for (int i = 0; i < cpg.getSize(); i++) {
            Constant cnt = cpg.getConstant(i);
            if (cnt instanceof ConstantUtf8) {
                String utf8String = ((ConstantUtf8) cnt).getBytes();
                if (candidates.contains(utf8String)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
        //ByteCode.printOpCode(ins, cpg);
        InvokeIdentifier identifier = new InvokeIdentifier(ins, cpg, insHandle);
        InjectionPoint injectionPoint = injectableParametersMap.get(identifier);
        return injectionPoint == null ? InjectionPoint.NONE : injectionPoint;
    }

    public static class InvokeIdentifier {

        private final String methodName;
        private final String methodSignature;
        private final String className;
        private final Kind kind;

        InvokeIdentifier(String className, String methodName, String methodSignature, Kind kind) {
            this.methodName = methodName;
            this.methodSignature = methodSignature;
            this.className = className;
            this.kind = kind;
        }

        InvokeIdentifier(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
            this.methodName = ins.getMethodName(cpg);
            this.methodSignature = ins.getSignature(cpg);
            this.className = ins.getClassName(cpg).replaceAll("\\.", "/");
            this.kind = Kind.valueOf(ins);
        }

        enum Kind {

            INVOKEVIRTUAL(Constants.INVOKEVIRTUAL),
            INVOKESPECIAL(Constants.INVOKESPECIAL),
            INVOKESTATIC(Constants.INVOKESTATIC),
            INVOKEINTERFACE(Constants.INVOKEINTERFACE);

            private final short kind;

            private Kind(short kind) {
                this.kind = kind;
            }

            static Kind valueOf(InvokeInstruction ins) {
                return Kind.valueOf(ins.getClass().getSimpleName());
            }
        }

        public static InvokeIdentifier valueOf(String value) {
            String[] parts = value.split(PROPERTY_SEPARATOR, 4);
            return new InvokeIdentifier(parts[1], parts[2], parts[3], Kind.valueOf(parts[0]));
        }

        @Override
        public String toString() {
            return kind + " " + className + "." + methodName + ":" + methodSignature;
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return toString().equals(obj.toString());
        }
    }

}
