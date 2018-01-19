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
package com.h3xstream.findsecbugs.graph;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector2;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;

/**
 * Why this trigger to shutdown is needed.
 *
 * SpotBugs Maven Plugin will launch a scan for every sub-project in a different classloader (AntClassLoader)
 * This will effectively isolate every scan states.
 * The graph being built reuse the same database. Therefore we need to shutdown the database in order to reload it in
 * the next scan.
 */
public class GraphGarbageCollector implements Detector2 {

    public GraphGarbageCollector(BugReporter bugReporter) {

    }

    @Override
    public void visitClass(ClassDescriptor classDescriptor) throws CheckedAnalysisException {

    }

    @Override
    public void finishPass() {
        if(GraphInstance.mustShutdownDatabase) {
            System.out.println("Shuting down database");
            GraphInstance.getInstance().shutdownDatabases();
        }
    }

    @Override
    public String getDetectorClassName() {
        return "GraphGarbageCollector";
    }
}
