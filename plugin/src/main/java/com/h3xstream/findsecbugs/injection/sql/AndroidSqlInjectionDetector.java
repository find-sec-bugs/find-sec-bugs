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
package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.xss.XssJspDetector;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.generic.ConstantPoolGen;

public class AndroidSqlInjectionDetector extends BasicInjectionDetector {

    public static final String[] CONTENT_PROVIDER_TYPES = {"android.content.ContentProvider"};

    public AndroidSqlInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("sql-android.txt", "SQL_INJECTION_ANDROID");
    }
    
    @Override
    protected int getPriority(Taint taint) {
        if (!taint.isSafe() && taint.hasTag(Taint.Tag.SQL_INJECTION_SAFE)) {
            return Priorities.IGNORE_PRIORITY;
        } else if (!taint.isSafe() && taint.hasTag(Taint.Tag.APOSTROPHE_ENCODED)) {
            return Priorities.LOW_PRIORITY;
        }
        else {
            return super.getPriority(taint);
        }

    }
}
