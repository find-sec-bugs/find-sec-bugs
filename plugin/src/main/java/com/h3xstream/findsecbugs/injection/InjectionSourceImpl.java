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
package com.h3xstream.findsecbugs.injection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class InjectionSourceImpl implements InjectionSource {

    private static final Logger LOG = Logger.getLogger(InjectionSourceImpl.class.getName());
    private static final String INJECTION_SOURCE_RESOURCE_NAME = "InjectionSource.properties";
    private static final Map<String, InjectionSource> instanceMap = new HashMap<String, InjectionSource>();

    public static InjectionSource getInstance(String resourceBaseName) {
        if (instanceMap.containsKey(resourceBaseName)) {
            return instanceMap.get(resourceBaseName);
        }

        Properties properties = getInjectionSourceProperties(resourceBaseName);
        InjectionSourceImpl injectionSource = new InjectionSourceImpl(properties);
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
        URL url = InjectionSourceImpl.class.getClassLoader().getResource(resourceName);
        if (url == null) {
            LOG.severe(resourceName + " not found.");
        }
        return loadProperties(urls.toArray(new URL[urls.size()]), loadProperties(url, null));
    }

    private static Properties loadProperties(URL url, Properties defaults) {
        Properties properties = new Properties(defaults);
        try {
            InputStream input = url.openStream();
            try {
                properties.load(input);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, url + " did not load.", e);
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, url + " did not close.", e);
                }
            }
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
            String[] value = properties.getProperty(propertyName).split(";");
            String[] values = value[0].split(",");
            int[] parameters = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                parameters[i] = Integer.parseInt(values[i].trim());
            }
            String bugType = value.length < 2 ? null : value[1].trim();
            map.put(InvokeIdentifier.valueOf(propertyName), new InjectionPoint(parameters, bugType));
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

    InjectionSourceImpl(Set<String> candidates, Map<InvokeIdentifier, InjectionPoint> injectableParametersMap) {
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine(String.format("candidates = %s", Arrays.toString(candidates.toArray())));
            LOG.fine(String.format("injectableParametersMap.keySet = %s", Arrays.toString(injectableParametersMap.keySet().toArray())));
        }
        this.candidates = candidates;
        this.injectableParametersMap = injectableParametersMap;
    }

    public InjectionSourceImpl(Properties properties) {
        this(toInjectableParametersMap(properties == null ? new Properties() : properties));
    }

    public InjectionSourceImpl(Map<InvokeIdentifier, InjectionPoint> injectableParametersMap) {
        this.candidates = toCandidates(injectableParametersMap);
        this.injectableParametersMap = injectableParametersMap;
    }

    InjectionSourceImpl() {
        this(Collections.EMPTY_SET, Collections.EMPTY_MAP);
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

            static Kind valueOf(short kind) {
                for (Kind value : Kind.values()) {
                    if (kind == value.kind) {
                        return value;
                    }
                }
                throw new IllegalArgumentException();
            }

            static Kind valueOf(InvokeInstruction ins) {
                return Kind.valueOf(ins.getClass().getSimpleName());
            }
        }

        public static InvokeIdentifier valueOf(String value) {
            String[] parts = value.split("(\\s|\\.|:)", 4);
            return new InvokeIdentifier(parts[1], parts[2], parts[3], Kind.valueOf(parts[0]));
        }

        @Override
        public String toString() {
            return kind + " " + className + "." + methodName + ":" + methodSignature;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + (this.methodName != null ? this.methodName.hashCode() : 0);
            hash = 97 * hash + (this.methodSignature != null ? this.methodSignature.hashCode() : 0);
            hash = 97 * hash + (this.className != null ? this.className.hashCode() : 0);
            hash = 97 * hash + (this.kind != null ? this.kind.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InvokeIdentifier other = (InvokeIdentifier) obj;
            if ((this.methodName == null) ? (other.methodName != null) : !this.methodName.equals(other.methodName)) {
                return false;
            }
            if ((this.methodSignature == null) ? (other.methodSignature != null) : !this.methodSignature.equals(other.methodSignature)) {
                return false;
            }
            if ((this.className == null) ? (other.className != null) : !this.className.equals(other.className)) {
                return false;
            }
            if ((this.kind == null) ? (other.kind != null) : !this.kind.equals(other.kind)) {
                return false;
            }
            return true;
        }
    }

}
