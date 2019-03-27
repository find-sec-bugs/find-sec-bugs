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

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * <b>Cipher identify</b>
 *
 * <ul>
 * <li>DES/CBC/NoPadding (56 bit)</li>
 * <li>DES/CBC/PKCS5Padding (56 bit)</li>
 * <li>DES/ECB/NoPadding (56 bit)</li>
 * <li>DES/ECB/PKCS5Padding (56 bit)</li>
 * </ul>
 *
 * Ref: <a href="http://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html">Partial list of ciphers</a>
 */
public class DesUsageDetector extends CipherDetector {


    public DesUsageDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    int getCipherPriority(String cipherValue) {
        cipherValue = cipherValue.toLowerCase();
        if (cipherValue.equals("des") || cipherValue.startsWith("des/")) {
            return Priorities.NORMAL_PRIORITY;
        }
        return Priorities.IGNORE_PRIORITY;
    }

    @Override
    String getBugPattern() {
        return "DES_USAGE";
    }
}
