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
package com.h3xstream.findbugs.test;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import com.h3xstream.findbugs.test.matcher.BugInstanceMatcherBuilder;
import com.h3xstream.findbugs.test.service.ClassFileLocator;
import com.h3xstream.findbugs.test.service.FindBugsLauncher;
import org.mockito.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.reset;

/**
 * Aggregate useful utilities for unit tests on detector.
 */
public class BaseDetectorTest {
    private static final Logger log = LoggerFactory.getLogger(BaseDetectorTest.class);
    private static final boolean DEBUG = true;

    private ClassFileLocator classFileLocator;
    private FindBugsLauncher findBugsLauncher;

//    private List<Object> mocksToReset = new ArrayList<Object>();

    public BaseDetectorTest() {
        classFileLocator = new ClassFileLocator();
        findBugsLauncher = new FindBugsLauncher();
    }

    public String getClassFilePath(String path) {
        return classFileLocator.getClassFilePath(path);
    }

    public String getJspFilePath(String path) {
        return classFileLocator.getJspFilePath(path);
    }
    
    public String getJarFilePath(String path) {
        return classFileLocator.getJarFilePath(path);
    }

    public void analyze(String[] classFiles, BugReporter bugReporter) throws Exception {
//        mocksToReset.add(bugReporter);
        findBugsLauncher.analyze(classFiles, bugReporter);
    }

    public void analyze(String[] classFiles, String[] classPaths, BugReporter bugReporter) throws Exception {
//        mocksToReset.add(bugReporter);
        findBugsLauncher.analyze(classFiles, classPaths, bugReporter);
    }

    public BugInstanceMatcherBuilder bugDefinition() {
        return new BugInstanceMatcherBuilder();
    }

    public static BugInstance anyBugs() {
        return Matchers.<BugInstance>any();
    }

    public static List<Integer> range(int from, int to) {
        List<Integer> rangeList = new ArrayList<Integer>();
        for(int i=from;i<Math.max(from,to);i++) {
            rangeList.add(i);
        }
        return rangeList;
    }

    @AfterClass
    public void after() {
        System.gc();
        if(DEBUG) {
            Runtime rt = Runtime.getRuntime();
            long inMb = 1024 * 1024;
            log.info("=== Memory info (Process " + ManagementFactory.getRuntimeMXBean().getName() + ") ===");
            log.info("Total memory : " + rt.totalMemory() / inMb);
            log.info("Free memory  : " + rt.freeMemory() / inMb);
            log.info("Memory usage : " + (rt.totalMemory() - rt.freeMemory()) / inMb);
            log.info("===================");
        }

//        for(Object mock : mocksToReset) {
//            reset(mock);
//        }
//        mocksToReset.clear();
    }

    public class SecurityReporter extends EasyBugReporter {

        public SecurityReporter(){
            getIncludeCategories().add("S");
        }

    }
}
