package edu.umd.cs.findbugs.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import edu.umd.cs.findbugs.*;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;
import edu.umd.cs.findbugs.test.service.ClassFileLocator;
import edu.umd.cs.findbugs.test.service.FindBugsLauncher;
import org.apache.commons.io.IOUtils;

import edu.umd.cs.findbugs.config.UserPreferences;

/**
 * Aggregate useful utilities for unit test on detector.
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
