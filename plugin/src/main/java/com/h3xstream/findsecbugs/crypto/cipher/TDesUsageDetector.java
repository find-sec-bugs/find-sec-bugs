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

/**
 * <b>Cipher identify</b>
 *
 * <ul>
 * <li>DESede/CBC/NoPadding (168 bit)</li>
 * <li>DESede/CBC/PKCS5Padding (168 bit)</li>
 * <li>DESede/ECB/NoPadding (168 bit)</li>
 * <li>DESede/ECB/PKCS5Padding (168 bit)</li>
 * </ul>
 *
 * Ref: <a href="http://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html">Partial list of ciphers</a>
 */
public class TDesUsageDetector extends CipherDetector {


    public TDesUsageDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    int getCipherPriority(String cipherValue) {
        cipherValue = cipherValue.toLowerCase();
        if (cipherValue.equals("desede") || cipherValue.startsWith("desede/")) {
            return Priorities.LOW_PRIORITY;
        }
        return Priorities.IGNORE_PRIORITY;
    }

    @Override
    String getBugPattern() {
        return "TDES_USAGE";
    }
}
