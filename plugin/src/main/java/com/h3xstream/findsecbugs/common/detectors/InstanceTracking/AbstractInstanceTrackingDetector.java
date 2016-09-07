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
 * Detector designed for extension to identify calls on specific instances of objects.
 *
 * @author Maxime Nadeau
 */
public abstract class AbstractInstanceTrackingDetector extends OpcodeStackDetector {

    protected BugReporter bugReporter;
    public AbstractInstanceTrackingDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    /**
     * The list of objects this detector is tracking.
     */
    private List<TrackedObject> trackedObjects = new ArrayList<TrackedObject>();
    protected List<TrackedObject> getTrackedObjects() { return trackedObjects; }

    /**
     * Adds a new object that this detector must track.
     *
     * The foundObjectInitCall method will be called when one of its initialization call is found and the
     * foundTrackedObjectCall method is called when the tracked calls are found.
     *
     * @param trackedObject The TrackedObject that we want this detector to follow.
     */
    public void addTrackedObject(TrackedObject trackedObject) {
        trackedObjects.add(trackedObject);
    }

    @Override
    public void sawOpcode(int seen) {
        if (isInvokeInstruction(seen)) {

            String fullOperand = getFullOperand();
            TrackedObject trackedObject = getTrackedObjectForInitCall(fullOperand);

            if (trackedObject != null) {

                // Found an object initialization call.
                // We save the location of the call and call foundObjectInitCall
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
                                    foundTrackedObjectCall(instance, currentCall, getStack());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method is used to check if the operand provided is a constructor call for one of our tracked objects.
     *
     * @param invokeInstruction The full textual representation of the invoke instruction.
     * @return The TrackedObject associated to this initialization call or null if this is not a tracked call.
     */
    private TrackedObject getTrackedObjectForInitCall(String invokeInstruction) {
        for (TrackedObject currentObject : trackedObjects) {
            for (String initInstruction : currentObject.getInitInstructions()) {
                if (initInstruction.equals(invokeInstruction)) {
                    return currentObject;
                }
            }
        }

        return null;
    }

    /**
     * This method is used to identify Invoke instructions using their OpCode integer equivalent.
     *
     * @param seen The integer representation of the OpCode.
     * @return True if the operation is an Invoke instruction. Otherwise, False.
     */
    private static boolean isInvokeInstruction(int seen) {
        return seen == INVOKEVIRTUAL || seen == INVOKEINTERFACE || seen == INVOKESPECIAL;
    }

    /**
     * This method returns the full operand of the call currently inspected by the detector.
     *
     * The operand is formatted with the "/" notation.
     *      Ex. javax/servlet/http/Cookie.<init>
     *
     * @return The full textual representation of the operand currently handled by this detector.
     */
    private String getFullOperand() {
        return getClassConstantOperand() + "." + getNameConstantOperand();
    }

    /**
     * This method is called when the initialization call for a tracked object was found.
     *
     * @param trackedObject The information on the tracked object.
     * @param instance The specific instance of object initialized. This can be used to obtain the object
     *                 initialization call location.
     */
    abstract protected void foundObjectInitCall(TrackedObject trackedObject, TrackedObjectInstance instance);

    /**
     * This method is called when a tracked Invoke instruction is found for one of our objects.
     *
     * @param instance The specific instance of object used for this call. This can be used to obtain the object
     *                 initialization call location.
     * @param callFound The encountered Invoke instruction information.
     * @param stack The state of the stack when the call was encountered.
     */
    abstract protected void foundTrackedObjectCall(TrackedObjectInstance instance, TrackedCall callFound, OpcodeStack stack);
}
