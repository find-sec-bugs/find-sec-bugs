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

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import java.util.ArrayList;
import java.util.List;


/**
 * Detector designed for extension to track calls on specific instances of objects.
 *
 * @author Maxime Nadeau
 */
public abstract class AbstractInstanceTrackingDetector extends OpcodeStackDetector {

    protected BugReporter bugReporter;
    public AbstractInstanceTrackingDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    private List<TrackedObject> trackedObjects = new ArrayList<TrackedObject>();
    protected List<TrackedObject> getTrackedObjects() { return trackedObjects; }

    public void addTrackedObject(TrackedObject trackedObject) {
        trackedObjects.add(trackedObject);
    }

    @Override
    public void sawOpcode(int seen) {
        if (isInvokeInstruction(seen)) {
            String fullOperand = getFullOperand();

            TrackedObject trackedObject = findTrackedObjectForCall(fullOperand);

            if (trackedObject != null) {
                // Found object initialization call.
                // We save its index on the stack and keep going

                SourceLineAnnotation objectCreationLocation = SourceLineAnnotation.fromVisitedInstruction(this, getPC());

                TrackedObjectInstance instance = trackedObject.addTrackedObjectInstance(getClassContext().getJavaClass(), getMethodDescriptor(), objectCreationLocation);
                foundObjectInitCall(trackedObject, instance);
            } else {
                OpcodeStack.Item currentItem = getStack().getItemMethodInvokedOn(this);
                SourceLineAnnotation objectCreationLocation = SourceLineAnnotation.fromVisitedInstruction(this, currentItem.getPC());

                for (TrackedObject currentObject : trackedObjects) {
                    for (TrackedObjectInstance instance : currentObject.getTrackedObjectInstances()) {
                        if (instance.getInitLocation().equals(objectCreationLocation)) {
                            for (TrackedCall currentCall : currentObject.getTrackedCalls()) {
                                if (currentCall.getInvokeInstruction().equals(fullOperand)) {
                                    foundTrackedObjectCall(currentObject, instance, getFullOperand(), getStack());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private TrackedObject findTrackedObjectForCall(String fullOperand) {
        for (TrackedObject currentObject : trackedObjects) {
            for (String initInstruction : currentObject.getInitInstructions()) {
                if (initInstruction.equals(fullOperand)) {
                    return currentObject;
                }
            }
        }

        return null;
    }

    private String getFullOperand() {
        return getClassConstantOperand() + "." + getNameConstantOperand();
    }

    private static boolean isInvokeInstruction(int seen) {
        return seen >= INVOKEVIRTUAL && seen <= INVOKEINTERFACE;
    }

    abstract protected void foundObjectInitCall(TrackedObject trackedObject, TrackedObjectInstance instance);
    abstract protected void foundTrackedObjectCall(TrackedObject trackedObject, TrackedObjectInstance instance, String call, OpcodeStack stack);
}
