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
 * <h3>Why this trigger to shutdown is needed.</h3>
 * When running in the context of SpotBugs Maven Plugin. The plugin will launch a scan for every sub-project in a different classloader (AntClassLoader)
 * This will effectively isolate every scan states. The singleton pattern use for GraphInstance class does not provide any guarantee of a single instantiation.
 * <pre>
 * [Maven]
 *  \- Subproject 1 Execution (Classloader 1)
 *  \- Subproject 2 Execution (Classloader 2)
 *  \- ...
 * </pre>
 * It is not possible to connect in embedded mode (in-memory) multiple times to the same graph folder.
 * The graph being built also need to be reuse. Sub-project might have interesting dependencies that need to be represent by the graph.
 * Therefore, we need to shutdown the database in order to reload it in the next scan.
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
