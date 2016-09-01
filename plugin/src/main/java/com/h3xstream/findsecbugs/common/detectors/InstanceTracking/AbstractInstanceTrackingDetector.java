package com.h3xstream.findsecbugs.common.detectors.InstanceTracking;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void report() {
        // Before we send the final report, we call the "finishAnalysis" method to
        // check for missing calls on our tracked object.
        finishAnalysis();

        super.report();
    }

    abstract protected void foundObjectInitCall(TrackedObject trackedObject, TrackedObjectInstance instance);
    abstract protected void foundTrackedObjectCall(TrackedObject trackedObject, TrackedObjectInstance instance, String call, OpcodeStack stack);
    abstract protected void finishAnalysis();
}
