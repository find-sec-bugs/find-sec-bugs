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

/**
 * This object is used in the AbstractInstanceTrackingDetector to track specific method calls.
 *
 * It stores the textual representation of the Invoke instruction for the call, its expected value
 * and the reporting behavior of the detector when this call is encountered.
 */
public class TrackedCall {

    public TrackedCall(String invokeInstruction, Object expectedValue, int parameterIndex, String bugType) {
        this.invokeInstruction = invokeInstruction;
        this.expectedValue = expectedValue;
        this.parameterIndex = parameterIndex;
        this.bugType = bugType;
    }

    private String invokeInstruction;
    public String getInvokeInstruction() { return invokeInstruction; }

    private Object expectedValue;
    public Object getExpectedValue() { return expectedValue; }

    private int parameterIndex;
    public int getParameterIndex() { return parameterIndex; }

    private String bugType;
    public String getBugType() { return bugType; }

    private boolean reportBugWhenNotLastCall;
    public boolean getReportBugWhenNotLastCall() { return reportBugWhenNotLastCall; }
    public TrackedCall reportBugWhenNotLastCall(boolean shouldReport) {
        reportBugWhenNotLastCall = shouldReport;
        return this;
    }

    private boolean reportBugWhenCalled;
    public boolean getReportBugWhenCalled() { return reportBugWhenCalled; }
    public TrackedCall reportBugWhenCalled(boolean shouldReport) {
        reportBugWhenCalled = shouldReport;
        return this;
    }
}
