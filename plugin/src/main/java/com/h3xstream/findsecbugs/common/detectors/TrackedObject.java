package com.h3xstream.findsecbugs.common.detectors;

import java.util.ArrayList;
import java.util.List;

public class TrackedObject {
    private String objectInitCall;
    public String getObjectInitCall() { return objectInitCall; }
    public void setObjectInitCall(String objectInitCall) { this.objectInitCall = objectInitCall; }

    public final List<TrackedCall> trackedCalls = new ArrayList<TrackedCall>();

    public TrackedObject(String objectInitCall) {
        this.objectInitCall = objectInitCall;
    }
}
