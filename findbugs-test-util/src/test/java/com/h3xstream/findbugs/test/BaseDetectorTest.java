package com.h3xstream.findbugs.test;

import edu.umd.cs.findbugs.BugReporter;
import com.h3xstream.findbugs.test.matcher.BugInstanceMatcherBuilder;
import com.h3xstream.findbugs.test.service.ClassFileLocator;
import com.h3xstream.findbugs.test.service.FindBugsLauncher;

/**
 * Aggregate useful utilities for unit com.h3xstream.findbugs.test on detector.
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
}
