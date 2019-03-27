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
package com.h3xstream.findsecbugs.injection.custom;

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomInjectionDetector extends BasicInjectionDetector {

    private static final Logger LOG = Logger.getLogger(CustomInjectionDetector.class.getName());

    private static final String SYSTEM_PROPERTY = "findsecbugs.injection.sources";

    public CustomInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);

        List<URL> urls = getSystemProperty();

        if (urls.size() > 0) {
            LOG.info("Loading additional injection sources from " + Arrays.toString(urls.toArray()) + "");
        }

        for(URL url : urls) {
            try {
                InputStream in = url.openStream();
                loadConfiguredSinks(in, "CUSTOM_INJECTION");
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Unable to load injection sources from :" + url.toString(), e);
            }
        }

    }

    @Override
    protected int getPriority(Taint taint) {
        if (!taint.isSafe() && taint.hasTag(Taint.Tag.CUSTOM_INJECTION_SAFE)) {
            return Priorities.IGNORE_PRIORITY;
        } else {
            return super.getPriority(taint);
        }
    }

    private List<URL> getSystemProperty() {
        String propertyValue = FindSecBugsGlobalConfig.getInstance().loadFromSystem(SYSTEM_PROPERTY, "");
        if(propertyValue.equals("")) {
            return new ArrayList<URL>();
        }
        String[] resourcePaths = propertyValue.split(",");
        List<URL> urls = new ArrayList<URL>(resourcePaths.length);
        for (String resourcePath : resourcePaths) {
            File file = new File(resourcePath);
            if (file.exists()) {
                try {
                    urls.add(file.toURI().toURL());
                } catch (MalformedURLException e) {
                    LOG.log(Level.SEVERE, file + " did not load.", e); //Unlikely if the file exists
                }
            }
            else {
                URL url = getClass().getResource(resourcePath);
                if(url != null) {
                    urls.add(url);
                }
            }
        }

        return urls;
    }


}
