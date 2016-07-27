package com.h3xstream.findsecbugs.common.detectors;

import java.util.ArrayList;
import java.util.List;

public class TrackedObject {
    private List<TrackedCall> trackedCalls = new ArrayList<TrackedCall>();
    public List<TrackedCall> getTrackedCalls() { return trackedCalls; }
    public void addTrackedCall(TrackedCall trackedCall) { this.trackedCalls.add(trackedCall); }
}
