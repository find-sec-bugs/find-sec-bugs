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

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.util.ClassName;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * Detector designed for extension to detect basic injections with a list of
 * full method names with specified injectable arguments as taint sinks
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public abstract class BasicInjectionDetector extends AbstractInjectionDetector {

    private final Map<String, InjectionPoint> injectionMap = new HashMap<String, InjectionPoint>();
    private static final SinksLoader SINKS_LOADER = new SinksLoader();

    public BasicInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
            InstructionHandle handle) {
        assert invoke != null && cpg != null;
        InjectionPoint injectionPoint = injectionMap.get(getFullMethodName(invoke, cpg));
        if (injectionPoint == null) {
            return InjectionPoint.NONE;
        }
        return injectionPoint;
    }

    protected void loadConfiguredSinks(InputStream stream, String bugType) throws IOException {
        SINKS_LOADER.loadSinks(stream, bugType, new SinksLoader.InjectionPointReceiver() {
            @Override
            public void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
                addParsedInjectionPoint(fullMethodName, injectionPoint);
            }
        });
    }

    protected void loadConfiguredSinks(String filename, String bugType) {
        SINKS_LOADER.loadConfiguredSinks(filename, bugType, new SinksLoader.InjectionPointReceiver() {
            @Override
            public void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
                addParsedInjectionPoint(fullMethodName, injectionPoint);
            }
        });
    }

    protected void loadSink(String line, String bugType) {
        SINKS_LOADER.loadSink(line, bugType, new SinksLoader.InjectionPointReceiver() {
            @Override
            public void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
                addParsedInjectionPoint(fullMethodName, injectionPoint);
            }
        });
    }

    public void addParsedInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
        assert !injectionMap.containsKey(fullMethodName);
        injectionMap.put(fullMethodName, injectionPoint);
    }
    
    private String getFullMethodName(InvokeInstruction invoke, ConstantPoolGen cpg) {
        return ClassName.toSlashedClassName(invoke.getReferenceType(cpg).toString())
                + "." + invoke.getMethodName(cpg) + invoke.getSignature(cpg);
    }
}
