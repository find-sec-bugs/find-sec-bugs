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
package com.h3xstream.findsecbugs.crypto.cipher;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;

public class RsaNoPaddingDetector extends CipherDetector {

    public RsaNoPaddingDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    int getCipherPriority(String cipherValue) {
        //cipherValue = cipherValue.toLowerCase();

        if (cipherValue.startsWith("RSA/") && cipherValue.endsWith("/NoPadding")) {
            return Priorities.NORMAL_PRIORITY;
        }
        return Priorities.IGNORE_PRIORITY;
    }

    @Override
    String getBugPattern() {
        return "RSA_NO_PADDING";
    }
}
