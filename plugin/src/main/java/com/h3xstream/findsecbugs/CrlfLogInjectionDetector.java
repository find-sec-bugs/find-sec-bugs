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
package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;

/**
 * Detects logging of tainted values - CRLF injection (or Improper Output Neutralization for Logs)
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class CrlfLogInjectionDetector extends BasicInjectionDetector {

    public CrlfLogInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("crlf-logs.txt", "CRLF_INJECTION_LOGS");
    }

    @Override
    protected int getPriority(Taint taint) {
        if (taint.isTainted()) {
            return Priorities.NORMAL_PRIORITY;
        } else if (!taint.isSafe()) {
            return Priorities.LOW_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
    }
}
