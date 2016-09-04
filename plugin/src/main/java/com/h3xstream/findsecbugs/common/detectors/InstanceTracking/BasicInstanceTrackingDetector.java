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

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;

public abstract class BasicInstanceTrackingDetector extends AbstractInstanceTrackingDetector {

    public BasicInstanceTrackingDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected void foundObjectInitCall(TrackedObject trackedObject, TrackedObjectInstance instance) {

        for (TrackedCall trackedCall : trackedObject.getTrackedCalls()) {
            // If we report on missing calls or when the tracked call is not the last
            //  we flag this object until we find the call.
            if (trackedCall.getReportBugWhenNotLastCall()) {
                instance.addBug(trackedCall.getBugType());
            }
        }
    }

    @Override
    protected void foundTrackedObjectCall(TrackedObject trackedObject, TrackedObjectInstance instance, String call, OpcodeStack stack) {

        for (TrackedCall trackedCall : trackedObject.getTrackedCalls()) {
            if (trackedCall.getInvokeInstruction().equals(call)) {

                if (stack.getStackItem(trackedCall.getParameterIndex()).getConstant().equals(trackedCall.getExpectedValue())) {
                    if (trackedCall.getReportBugWhenCalled()) {
                        bugReporter.reportBug(new BugInstance(this, trackedCall.getBugType(), Priorities.LOW_PRIORITY)
                                .addClass(instance.getInitJavaClass()).addMethod(instance.getInitMethodDescriptor())
                                .addSourceLine(instance.getInitLocation()));
                    } else {
                        if (trackedCall.getReportBugWhenNotLastCall()) {
                            instance.removeBug(trackedCall.getBugType());
                        }
                    }
                } else if (trackedCall.getReportBugWhenNotLastCall()) {
                    instance.addBug(trackedCall.getBugType());
                }
            }
        }
    }

    @Override
    public void report() {
        for (TrackedObject trackedObject : getTrackedObjects()) {

            for (TrackedObjectInstance trackedInstance : trackedObject.getTrackedObjectInstances()) {
                for (String reportedBugs : trackedInstance.getBugsFound()) {
                    bugReporter.reportBug(new BugInstance(this, reportedBugs, Priorities.LOW_PRIORITY)
                            .addClass(trackedInstance.getInitJavaClass()).addMethod(trackedInstance.getInitMethodDescriptor())
                            .addSourceLine(trackedInstance.getInitLocation()));
                }
            }
        }

        super.report();
    }
}
