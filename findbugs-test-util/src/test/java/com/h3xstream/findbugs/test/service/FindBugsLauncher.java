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
package com.h3xstream.findbugs.test.service;

import edu.umd.cs.findbugs.*;
import edu.umd.cs.findbugs.config.ProjectFilterSettings;
import edu.umd.cs.findbugs.config.UserPreferences;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import static org.mockito.Mockito.mock;

@NotThreadSafe
public class FindBugsLauncher {

    private static final Logger log = LoggerFactory.getLogger(FindBugsLauncher.class);


    static Plugin loadedPlugin;

    /**
	 * Launch an analysis on the given source files.
	 *
	 * @param classFiles
	 * @param bugReporter
	 * @throws java.io.IOException
	 * @throws InterruptedException
	 * @throws edu.umd.cs.findbugs.PluginException
	 */
	public void analyze(String[] classFiles, BugReporter bugReporter) throws IOException, InterruptedException,
            PluginException, NoSuchFieldException, IllegalAccessException {

		final ClassLoader cl = getClass().getClassLoader();

		Project project = new Project();
		project.setProjectName("automate-test-project");
		for(String file : classFiles) {
			project.addFile(file);
		}

        if(loadedPlugin == null) {
            //Initialize the plugin base on the findbugs.xml
            byte[] archive = buildFakePluginJar();

            File f = new File(System.getProperty("java.io.tmpdir"),"plugin.jar");
            log.info("Writing "+f.getCanonicalPath());
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            out.write(archive);
            out.close();

            loadedPlugin = Plugin.loadCustomPlugin(f.toURL(),project);
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
        
        log.info("Analyzing... {}",classFiles);
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
	 * @return
	 * @throws IOException
	 */
	private byte[] buildFakePluginJar() throws IOException {
		ClassLoader cl = getClass().getClassLoader();

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JarOutputStream jar = new JarOutputStream(buffer);

        //Add files to the jar stream
		for(String resource : Arrays.asList("findbugs.xml", "messages.xml", "META-INF/MANIFEST.MF")) {
			jar.putNextEntry(new ZipEntry(resource));
			jar.write(IOUtils.toByteArray(cl.getResourceAsStream("metadata/" + resource)));
		}
		jar.finish();

		return buffer.toByteArray();
	}
}
