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
package com.h3xstream.findsecbugs.android;

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class GeolocationDetector implements Detector {

    private static final boolean DEBUG = false;
    private static final String ANDROID_GEOLOCATION_TYPE = "ANDROID_GEOLOCATION";
    private final BugReporter bugReporter;

    public GeolocationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        
        //The class extends WebChromeClient
        boolean isWebChromeClient = InterfaceUtils.isSubtype(javaClass, "android.webkit.WebChromeClient");
        
        //Not the target of this detector
        if (!isWebChromeClient) {
            return;
        }
        Method[] methodList = javaClass.getMethods();
        for (Method m : methodList) {
            if (DEBUG) {
                System.out.println(">>> Method: " + m.getName());
            }
            //The presence of onGeolocationPermissionsShowPrompt is not enforce for the moment
            if (!m.getName().equals("onGeolocationPermissionsShowPrompt")) {
                continue;
            }
            //Since the logic implemented need to be analyze by a human, all implementation will be flagged.
            bugReporter.reportBug(new BugInstance(this, ANDROID_GEOLOCATION_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClassAndMethod(javaClass, m));
        }
    }

    @Override
    public void report() {
    }
}
