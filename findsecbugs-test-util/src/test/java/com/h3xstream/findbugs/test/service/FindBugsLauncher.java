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
package com.h3xstream.findbugs.test.service;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.DetectorFactoryCollection;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.FindBugsProgress;
import edu.umd.cs.findbugs.Plugin;
import edu.umd.cs.findbugs.PluginException;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.config.ProjectFilterSettings;
import edu.umd.cs.findbugs.config.UserPreferences;

@NotThreadSafe
public class FindBugsLauncher {
    private static final Logger log = LoggerFactory.getLogger(FindBugsLauncher.class);

    private final String metadataFolder;

    public FindBugsLauncher(String metadataFolder) {
        this.metadataFolder = metadataFolder;
    }

    public FindBugsLauncher() {
        this.metadataFolder = "metadata";
    }

    static Plugin loadedPlugin;

    /**
     * Launch an analysis on the given source files.
     *
     * @param classFiles
     * @param bugReporter
     * @throws java.io.IOException
     * @throws InterruptedException
     * @throws edu.umd.cs.findbugs.PluginException
     * @throws URISyntaxException 
     *
     */
	public void analyze(String[] classFiles, BugReporter bugReporter) throws IOException, InterruptedException,
	        PluginException, NoSuchFieldException, IllegalAccessException, URISyntaxException {
		analyze(classFiles, new String[] {}, bugReporter);
	}

    /**
     * Launch an analysis on the given source files.
     *
     * @param classFiles
     * @param classPaths
     * @param bugReporter
     * @throws java.io.IOException
     * @throws InterruptedException
     * @throws edu.umd.cs.findbugs.PluginException
     * @throws URISyntaxException
     *
     */
    public void analyze(String[] classFiles, String[] classPaths, BugReporter bugReporter) throws IOException, InterruptedException,
    		PluginException, NoSuchFieldException, IllegalAccessException, URISyntaxException {
        Project project = new Project();
        project.setProjectName("automate-test-project");
        for (String file : classFiles) {
            project.addFile(file);
        }

        // Add classpath list to project's auxclasspath
        for (String classpath : classPaths) {
        	project.addAuxClasspathEntry(classpath);
        }

        if (loadedPlugin == null) {
            //Initialize the plugin base on the findbugs.xml
            byte[] archive = buildFakePluginJar();

            File f = new File(System.getProperty("java.io.tmpdir"), "plugin.jar");
            log.info("Writing " + f.getCanonicalPath());
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            out.write(archive);
            out.close();

            loadedPlugin = Plugin.loadCustomPlugin(f.toURI().toURL(), project);
        }

        //FindBugs engine
        FindBugs2 engine = new FindBugs2();
        engine.setNoClassOk(true);
        engine.setMergeSimilarWarnings(false);
        engine.setBugReporter(bugReporter);
        engine.setProject(project);
        engine.setProgressCallback(mock(FindBugsProgress.class));

        engine.setDetectorFactoryCollection(DetectorFactoryCollection.instance());

        //User preferences set to miss no bugs report
        UserPreferences prefs = UserPreferences.createDefaultUserPreferences();

        ProjectFilterSettings filter = prefs.getFilterSettings();
        filter.setMinRank(20);
        filter.setDisplayFalseWarnings(true);
        filter.setMinPriority("Low");

        engine.setUserPreferences(prefs);

        log.info("Analyzing... {}", classFiles);
        engine.execute();
    }

    /**
     * The minimum requirement to have a "valid" archive plugin is to include
     * findbugs.xml, messages.xml and MANIFEST.MF files. The rest of the
     * resources are load using the parent ClassLoader (Not requires to be in
     * the jar).
     * <p>
     * Instead of building a file on disk, the result of the stream is kept in
     * memory and return as a byte array.
     *
     * @return
     * @throws IOException
     * @throws URISyntaxException 
     */
    private byte[] buildFakePluginJar() throws IOException, URISyntaxException {
        ClassLoader cl = getClass().getClassLoader();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        JarOutputStream jar = new JarOutputStream(buffer);

        final URL metadata = cl.getResource(metadataFolder);
        if (metadata != null) {
            final File dir = new File(metadata.toURI());
            
            //Add files to the jar stream
            addFilesToStream(cl, jar, dir, "");
        }
        jar.finish();
        jar.close();

        return buffer.toByteArray();
    }

    private void addFilesToStream(final ClassLoader cl, final JarOutputStream jar, final File dir,
            final String path) throws IOException {
        for (final File nextFile : dir.listFiles()) {
            if (nextFile.isFile()) {
                final String resource = path + nextFile.getName();
                jar.putNextEntry(new ZipEntry(resource));
                jar.write(IOUtils.toByteArray(cl.getResourceAsStream(metadataFolder + "/" + resource)));
            } else {
            	addFilesToStream(cl, jar, nextFile, path + nextFile.getName() + "/");
            }
        }
    }
}
