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
package com.h3xstream.findsecbugs.injection.ssrf;

import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import edu.umd.cs.findbugs.BugReporter;

public class SSRFDetector extends BasicInjectionDetector {

    private static final String SCALA_PLAY_SSRF_TYPE = "SCALA_PLAY_SSRF";
    private static final String URLCONNECTION_SSRF_FD = "URLCONNECTION_SSRF_FD";

    public SSRFDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("scala-play-ssrf.txt", SCALA_PLAY_SSRF_TYPE);
        loadConfiguredSinks("urlconnection-ssrf.txt", URLCONNECTION_SSRF_FD);
    }
}