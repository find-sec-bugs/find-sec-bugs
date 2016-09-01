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
        if (bugsFound.contains(bugType)) {
            bugsFound.add(bugType);
        }
    }
}
