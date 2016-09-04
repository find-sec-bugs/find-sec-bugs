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

public class TrackedObjectInstance {

    public TrackedObjectInstance(JavaClass initJavaClass, MethodDescriptor initMethodDescriptor, SourceLineAnnotation initLocation) {
        this.initJavaClass = initJavaClass;
        this.initMethodDescriptor = initMethodDescriptor;
        this.initLocation = initLocation;
    }

    private JavaClass initJavaClass;
    public JavaClass getInitJavaClass() { return initJavaClass; }

    private MethodDescriptor initMethodDescriptor;
    public MethodDescriptor getInitMethodDescriptor() { return initMethodDescriptor; }

    private SourceLineAnnotation initLocation;
    public SourceLineAnnotation getInitLocation() { return initLocation; }

    private List<String> bugsFound = new ArrayList<String>();
    public List<String> getBugsFound() { return bugsFound; }
    public void removeBug(String bugType) { bugsFound.remove(bugType); }
    public void addBug(String bugType) {
        if (!bugsFound.contains(bugType)) {
            bugsFound.add(bugType);
        }
    }
}
