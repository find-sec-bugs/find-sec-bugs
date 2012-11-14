/**
 * Find Security Bugs
 * Copyright (c) 2012, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findbugs.test;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import com.h3xstream.findbugs.test.matcher.BugInstanceMatcherBuilder;
import com.h3xstream.findbugs.test.service.ClassFileLocator;
import com.h3xstream.findbugs.test.service.FindBugsLauncher;
import org.mockito.Matchers;

/**
 * Aggregate useful utilities for unit tests on detector.
 */
public class BaseDetectorTest {
	private static final boolean DEBUG = true;

    private ClassFileLocator classFileLocator;
    private FindBugsLauncher findBugsLauncher;

    public BaseDetectorTest() {
        classFileLocator = new ClassFileLocator();
        findBugsLauncher = new FindBugsLauncher();
    }

    public String getClassFilePath(String path) {
        return classFileLocator.getClassFilePath(path);
    }

    public void analyze(String[] classFiles, BugReporter bugReporter) throws Exception {
        findBugsLauncher.analyze(classFiles,bugReporter);
    }

    public BugInstanceMatcherBuilder bugDefinition() {
        return new BugInstanceMatcherBuilder();
    }


    public static BugInstance anyBugs() {
        return Matchers.<BugInstance>any();
    }
}
