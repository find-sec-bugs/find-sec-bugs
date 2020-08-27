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
import org.testng.annotations.BeforeClass;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Aggregate useful utilities for unit tests on detector.
 */
public class BaseDetectorTest {
    private static final Logger log = LoggerFactory.getLogger(BaseDetectorTest.class);
    private static final boolean DEBUG = false;

    private ClassFileLocator classFileLocator;
    private FindBugsLauncher findBugsLauncher;

    public BaseDetectorTest() {
        classFileLocator = new ClassFileLocator();
        findBugsLauncher = new FindBugsLauncher();
    }

    public BaseDetectorTest(String metadataFolder) {
        classFileLocator = new ClassFileLocator();
        findBugsLauncher = new FindBugsLauncher(metadataFolder);
    }

    public String getClassFilePath(String path) {

        if(path.endsWith(".java")) {
            path = path.replaceAll(".java$","");
        }

        //Convert dot class name to path
        if(countMatches(path,'.') > 1 && !path.endsWith(".jar")) {
            path = path.replaceAll("\\.","/");
        }
        return classFileLocator.getClassFilePath(path);
    }

    public String getJspFilePath(String path) {
        return classFileLocator.getJspFilePath(path);
    }
    
    public String getJarFilePath(String path) {
        return classFileLocator.getJarFilePath(path);
    }

    public void analyze(String[] classFiles, BugReporter bugReporter) throws Exception {
        List<String> classPath = new ArrayList<String>();
        classPath.add(getPluginDepsJarPath());
        findBugsLauncher.analyze(classFiles, classPath.toArray(new String[classPath.size()]), bugReporter);
    }

    public void analyze(String[] classFiles, String[] classPathsOrig, BugReporter bugReporter) throws Exception {
        List<String> classPath = new ArrayList<String>(Arrays.asList(classPathsOrig));
        classPath.add(getPluginDepsJarPath());
        findBugsLauncher.analyze(classFiles, classPath.toArray(new String[classPath.size()]), bugReporter);
    }

    /**
     * The test dependencies are added by default to avoid ClassNotFoundException when analyzing the test sample.
     * Those classes are required if the inheritance hierarchy is analyzed.
     * @return The path to either the compiled directory of the project or its jar.
     */
    private String getPluginDepsJarPath() {
        ClassLoader cl = getClass().getClassLoader();
        String url = cl.getResource("PluginDepsClassPathFinder.class").toExternalForm();

        String separateFile = "/target/classes/PluginDepsClassPathFinder.class"; //In the IDE, the compiled directory are used.
        String insideJar = "!/PluginDepsClassPathFinder.class"; //With Maven, the jar will be reference.
        for(String suffix : Arrays.asList(separateFile, insideJar)) {
            if(url.endsWith(suffix)) {
                String filename = url.substring(0,url.length() - suffix.length());

                if(suffix == separateFile) {
                    filename += "/target/classes/"; //This part of the suffix need to be kept
                }

                //FindBugs will open file handle (java.io.File). The protocol file: need to be removed.
                for(String prefix : Arrays.asList("file:", "jar:file:")) {
                    if (filename.startsWith(prefix)) {
                        filename = filename.substring(prefix.length());
                    }
                }
                return filename;
            }
        }

        throw new RuntimeException("Unable to locate the dependencies for test in the classpath.");
    }


    /// Various utility for Hamcrest matcher

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

    @BeforeClass
    public void before() {
        Class concreteClass = this.getClass();
        log.info(">>>> Starting test suite "+concreteClass.getSimpleName()+" <<<<");
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


    /**
     * Simplify version of StringUtils.countMatches()
     * The method was extracted because common-lang is no longer used by SpotBugs.
     *
     * @param str
     * @param ch
     * @return
     */
    public static int countMatches(final CharSequence str, final char ch) {
        int count = 0;
        // We could also call str.toCharArray() for faster look ups but that would generate more garbage.
        for (int i = 0; i < str.length(); i++) {
            if (ch == str.charAt(i)) {
                count++;
            }
        }
        return count;
    }
}
