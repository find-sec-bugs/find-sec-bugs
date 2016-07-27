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
package com.h3xstream.findsecbugs.common.detectors;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * Detector designed for extension to track calls on certain objects
 *
 * @author Maxime Nadeau
 */
public abstract class AbstractInstanceTrackingDetector implements Detector {

    private BugReporter bugReporter;
    private final List<TrackedObject> trackedObjects = new ArrayList<TrackedObject>();

    protected AbstractInstanceTrackingDetector(BugReporter bugReporter) { this.bugReporter = bugReporter; }

    public void addTrackedCall(String objectInitInstruction, String trackedCallLocation, String bugType, Integer checkedParameterValue) {
        TrackedObject trackedObject = new TrackedObject(objectInitInstruction);
        trackedObject.trackedCalls.add(new TrackedCall(trackedCallLocation, bugType, checkedParameterValue));
        trackedObjects.add(trackedObject);
    }

    /**
     * Allow any concrete implementation of taint detector to skip the analysis of certain files.
     * The purpose can be for optimisation or to trigger bug in specific context.
     *
     * The default implementation returns true to all classes visited.
     *
     * @param classContext Information about the class that is about to be analyzed
     * @return If the given class should be analyze.
     */
    public boolean shouldAnalyzeClass(ClassContext classContext) {
        return true;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        if(!shouldAnalyzeClass(classContext)) {
            return;
        }
        for (Method method : classContext.getMethodsInCallOrder()) {
            if (classContext.getMethodGen(method) == null) {
                continue;
            }
            try {
                analyzeMethod(classContext, method);
            } catch (CheckedAnalysisException e) {
                logException(classContext, method, e);
            } catch (RuntimeException e) {
                logException(classContext, method, e);
            }
        }
    }

    protected void analyzeMethod(ClassContext classContext, Method method)
            throws CheckedAnalysisException {

        ConstantPoolGen cpg = classContext.getConstantPoolGen();

        for (Iterator<Location> i = getLocationIterator(classContext, method); i.hasNext();) {
            Location location = i.next();
            InstructionHandle handle = location.getHandle();
            Instruction instruction = handle.getInstruction();
            if (!(instruction instanceof InvokeInstruction)) {
                continue;
            }

            InvokeInstruction invoke = (InvokeInstruction)instruction;

            for (TrackedObject trackedObject : trackedObjects) {

                String currentMethod = getFullMethodName(invoke, cpg);
                if (trackedObject.getObjectInitCall().equalsIgnoreCase(currentMethod)) {

                    // The following call should push the cookie onto the stack
                    Instruction objectStoreInstruction = handle.getNext().getInstruction();
                    if (objectStoreInstruction instanceof ASTORE) {

                        // We will use the position of the object on the stack to track it
                        ASTORE storeInstruction = (ASTORE)objectStoreInstruction;

                        for (TrackedCall trackedInvokeInstruction : trackedObject.trackedCalls) {
                            Location callLocation = getTrackedInstructionLocation(cpg, location, storeInstruction.getIndex(),
                                    trackedInvokeInstruction.getInvokeInstruction(), trackedInvokeInstruction.getCheckedParamValue());
                            if (callLocation == null) {

                                JavaClass javaClass = classContext.getJavaClass();

                                bugReporter.reportBug(new BugInstance(this, trackedInvokeInstruction.getBugType(), Priorities.NORMAL_PRIORITY) //
                                        .addClass(javaClass)
                                        .addMethod(javaClass, method)
                                        .addSourceLine(classContext, method, location));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method is used to track calls made on a specific object. For instance, this could be used to track if "setHttpOnly(true)"
     * was executed on a specific cookie object.
     *
     * This allows the detector to find interchanged calls like this
     *
     * Cookie cookie1 = new Cookie("f", "foo");     <- This cookie is unsafe
     * Cookie cookie2 = new Cookie("b", "bar");     <- This cookie is safe
     * cookie1.setHttpOnly(false);
     * cookie2.setHttpOnly(true);
     *
     * @param cpg ConstantPoolGen
     * @param searchStartLocation The Location of the object initialization call.
     * @param objectStackLocation The index of the object on the stack.
     * @param invokeInstruction The instruction we want to detect.
     * @return The location of the tracked invoke instruction or null if the instruction was not found.
     */
    private Location getTrackedInstructionLocation(ConstantPoolGen cpg, Location searchStartLocation,
                                                     int objectStackLocation, String invokeInstruction, Integer checkedParameterValue) {
        InstructionHandle handle = searchStartLocation.getHandle();

        int loadedStackValue = 0;

        // Loop until we find the setSecure call for this cookie
        while (handle.getNext() != null) {
            handle = handle.getNext();
            Instruction nextInst = handle.getInstruction();

            // We check if the index of the cookie used for this invoke is the same as the one provided
            if (nextInst instanceof ALOAD) {
                ALOAD loadInst = (ALOAD)nextInst;
                loadedStackValue = loadInst.getIndex();
            }

            if (nextInst instanceof InvokeInstruction
                    && loadedStackValue == objectStackLocation) {
                InvokeInstruction invoke = (InvokeInstruction) nextInst;

                String methodNameWithSignature = getFullMethodName(invoke, cpg);
                if (methodNameWithSignature.equals(invokeInstruction)) {

                    Integer val = ByteCode.getConstantInt(handle.getPrev());

                    if (val != null && val == checkedParameterValue) {
                        return new Location(handle, searchStartLocation.getBasicBlock());
                    }
                }
            }
        }

        return null;
    }

    protected static Iterator<Location> getLocationIterator(ClassContext classContext, Method method)
            throws CheckedAnalysisException {
        try {
            return classContext.getCFG(method).locationIterator();
        } catch (CFGBuilderException ex) {
            throw new CheckedAnalysisException("cannot get control flow graph", ex);
        }
    }

    protected static String getFullMethodName(InvokeInstruction invoke, ConstantPoolGen cpg) {
        String signature = invoke.getLoadClassType(cpg).getClassName().replace(".", "/");
        String methodName = invoke.getMethodName(cpg);
        return signature + "." + methodName;
    }

    private void logException(ClassContext classContext, Method method, Exception ex) {
        bugReporter.logError("Exception while analyzing "
                + classContext.getFullyQualifiedMethodName(method), ex);
    }

    @Override
    public void report() { }
}
