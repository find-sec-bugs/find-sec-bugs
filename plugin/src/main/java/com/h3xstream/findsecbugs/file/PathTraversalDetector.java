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
package com.h3xstream.findsecbugs.file;

import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;

public class PathTraversalDetector extends BasicInjectionDetector {

    private static final String PATH_TRAVERSAL_IN_TYPE = "PATH_TRAVERSAL_IN";
    private static final String PATH_TRAVERSAL_OUT_TYPE = "PATH_TRAVERSAL_OUT";

    private static final String SCALA_PATH_TRAVERSAL_IN_TYPE = "SCALA_PATH_TRAVERSAL_IN";

    public PathTraversalDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("path-traversal-in.txt", PATH_TRAVERSAL_IN_TYPE);
        loadConfiguredSinks("path-traversal-out.txt", PATH_TRAVERSAL_OUT_TYPE);
        loadConfiguredSinks("scala-path-traversal-in.txt", SCALA_PATH_TRAVERSAL_IN_TYPE);
        loadConfiguredSinks("kotlin-path-traversal-in.txt", PATH_TRAVERSAL_IN_TYPE);

        // We are not using a Scala-specific message because it doesn't have an embed code example
        loadConfiguredSinks("scala-path-traversal-out.txt", PATH_TRAVERSAL_OUT_TYPE);
    }


    @Override
    protected int getPriority(Taint taint) {
        if (!taint.isSafe() && taint.hasTag(Taint.Tag.PATH_TRAVERSAL_SAFE)) {
            return Priorities.IGNORE_PRIORITY;
        } else {
            return super.getPriority(taint);
        }
    }
}
