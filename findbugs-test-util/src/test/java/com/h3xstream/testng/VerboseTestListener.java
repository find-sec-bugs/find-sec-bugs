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
package com.h3xstream.testng;

import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findbugs.test.FbTestGlobalSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.lang.management.ManagementFactory;

/**
 * This listener is useful to add more detail about test failure.
 * (Intended for Travis-CI)
 */
public class VerboseTestListener extends TestListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(VerboseTestListener.class);

    @Override
    public void onTestFailure(ITestResult tr) {
        log.error(tr.getName() + " failed", tr.getThrowable());
    }

    @Override
    public void onStart(ITestContext ctx) {
        log.info("<<<<<<<<<<<<<<<<<<<< {} started >>>>>>>>>>>>>>>>>>>>", ctx.getName());
        FbTestGlobalSettings.setRunningFromMaven(true);
    }

    @Override
    public void onFinish(ITestContext ctx) {
        System.gc();
        Runtime rt = Runtime.getRuntime();
        long inMb = 1024 * 1024;
        log.info("Total memory : " + rt.totalMemory() / inMb);
        log.info("Free memory  : " + rt.freeMemory() / inMb);
        log.info("Memory usage : " + (rt.totalMemory() - rt.freeMemory()) / inMb);
        log.info("Process      : " + ManagementFactory.getRuntimeMXBean().getName());
        log.info("<<<<<<<<<<<<<<<<<<<< {} finished >>>>>>>>>>>>>>>>>>>>", ctx.getName());
    }
}
