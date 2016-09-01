package com.h3xstream.findsecbugs.common.detectors.InstanceTracking;

import javafx.util.Pair;

public class TrackedCall {
    public TrackedCall(String invokeInstruction, Object expectedValue, int parameterIndex) {
        this.invokeInstruction = invokeInstruction;
        this.expectedValue = expectedValue;
        this.parameterIndex = parameterIndex;
    }

    private String invokeInstruction;
    public String getInvokeInstruction() { return invokeInstruction; }

    private Object expectedValue;
    public Object getExpectedValue() { return expectedValue; }

    private int parameterIndex;
    public int getParameterIndex() { return parameterIndex; }

    private String bugType;
    public String getBugType() { return bugType; }
    public TrackedCall setBugType(String bugType) {
        this.bugType = bugType;
        return this;
    }

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
