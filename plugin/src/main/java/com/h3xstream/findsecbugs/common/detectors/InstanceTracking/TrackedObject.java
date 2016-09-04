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
package com.h3xstream.findsecbugs.common.detectors.InstanceTracking;

import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import java.util.ArrayList;
import java.util.List;
import org.apache.bcel.classfile.JavaClass;

public class TrackedObject {
    public TrackedObject(String initInstruction) {
        initInstructions.add(initInstruction);
    }

    private List<String> initInstructions = new ArrayList<String>();
    public List<String> getInitInstructions() { return initInstructions; }
    public TrackedObject addInitInstruction(String initInstruction) {
        initInstructions.add(initInstruction);
        return this;
    }

    private List<TrackedCall> trackedCalls = new ArrayList<TrackedCall>();
    public List<TrackedCall> getTrackedCalls() { return trackedCalls; }
    public TrackedObject addTrackedCallForObject(TrackedCall newCall) {
        trackedCalls.add(newCall);
        return this;
    }

    private List<TrackedObjectInstance> trackedObjectInstances = new ArrayList<TrackedObjectInstance>();
    public List<TrackedObjectInstance> getTrackedObjectInstances() { return trackedObjectInstances; }
    public TrackedObjectInstance addTrackedObjectInstance(JavaClass initJavaClass, MethodDescriptor initMethodDescriptor, SourceLineAnnotation initSourceLine) {
        TrackedObjectInstance instance = new TrackedObjectInstance(initJavaClass, initMethodDescriptor, initSourceLine);
        trackedObjectInstances.add(instance);

        return instance;
    }
}
