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
package com.h3xstream.findsecbugs.cookie;

import com.h3xstream.findsecbugs.common.detectors.AbstractInstanceTrackingDetector;
import edu.umd.cs.findbugs.BugReporter;

public class CookieFlagsDetector extends AbstractInstanceTrackingDetector {

    private static final String INSECURE_COOKIE_TYPE = "INSECURE_COOKIE";
    private static final String HTTPONLY_COOKIE_TYPE = "HTTPONLY_COOKIE";

    public CookieFlagsDetector(BugReporter bugReporter) {
        super(bugReporter);

        addTrackedCall("javax/servlet/http/Cookie.<init>", "javax/servlet/http/Cookie.setSecure", INSECURE_COOKIE_TYPE);
        addTrackedCall("javax/servlet/http/Cookie.<init>", "javax/servlet/http/Cookie.setHttpOnly", HTTPONLY_COOKIE_TYPE);
    }
}