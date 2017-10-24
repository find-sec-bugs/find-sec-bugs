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

public abstract class CipherDetector extends BasicInjectionDetector {


    private static final String DES_USAGE_TYPE = "DES_USAGE";

    private static final InvokeMatcherBuilder CIPHER_GET_INSTANCE = invokeInstruction()
            .atClass("javax/crypto/Cipher")
            .atMethod("getInstance")
            .withArgs("(Ljava/lang/String;)Ljavax/crypto/Cipher;");
    private static final InvokeMatcherBuilder CIPHER_GET_INSTANCE_PROVIDER = invokeInstruction()
            .atClass("javax/crypto/Cipher")
            .atMethod("getInstance")
            .withArgs("(Ljava/lang/String;Ljava/lang/String;)Ljavax/crypto/Cipher;",
                    "(Ljava/lang/String;Ljava/security/Provider;)Ljavax/crypto/Cipher;");

    private static final InvokeMatcherBuilder KEYGENERATOR_GET_INSTANCE = invokeInstruction()
            .atClass("javax/crypto/KeyGenerator")
            .atMethod("getInstance")
            .withArgs("(Ljava/lang/String;)Ljavax/crypto/KeyGenerator;");
    private static final InvokeMatcherBuilder KEYGENERATOR_GET_INSTANCE_PROVIDER = invokeInstruction()
            .atClass("javax/crypto/KeyGenerator")
            .atMethod("getInstance")
            .withArgs("(Ljava/lang/String;Ljava/lang/String;)Ljavax/crypto/KeyGenerator;",
                    "(Ljava/lang/String;Ljava/security/Provider;)Ljavax/crypto/KeyGenerator;");


    public CipherDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    /**
     * Hook Cipher.getInstance(), KeyGenerator.getInstance()
     * @param invoke
     * @param cpg
     * @param handle
     * @return
     */
    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        assert invoke != null && cpg != null;

        //ByteCode.printOpCode(invoke,cpg);

        if(CIPHER_GET_INSTANCE.matches(invoke,cpg)) {
            return new InjectionPoint(new int[]{0}, getBugPattern());
        }
        else if(CIPHER_GET_INSTANCE_PROVIDER.matches(invoke,cpg)) {
            return new InjectionPoint(new int[]{1}, getBugPattern());
        }
        else if(KEYGENERATOR_GET_INSTANCE.matches(invoke,cpg)) {
            return new InjectionPoint(new int[]{0}, getBugPattern());
        }
        else if(KEYGENERATOR_GET_INSTANCE_PROVIDER.matches(invoke,cpg)) {
            return new InjectionPoint(new int[]{1}, getBugPattern());
        }
        return InjectionPoint.NONE;
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset)
            throws DataflowAnalysisException {

        Taint valueTaint = fact.getStackValue(offset);

        String cipherValue = valueTaint.getConstantOrPotentialValue();
        if(cipherValue == null) {
            return Priorities.IGNORE_PRIORITY;
        }
        return getCipherPriority(cipherValue);
    }

    abstract int    getCipherPriority(String cipherValue);
    abstract String getBugPattern();
}
