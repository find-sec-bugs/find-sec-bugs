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
package com.h3xstream.findsecbugs.password;

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.bcp.FieldVariable;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;

/**
 * GoogleApi provide code sample to sign URL using provided API key.
 * The management of the key is left to the developer.
 * Developers might simply copy-paste sample of code without loading the key from a configuration file.
 * <p>
 * If too much false positive are rise, the signature will be more precise. The assumption is that the code
 * was copy and left unchanged except the key.
 * <p>
 * Ref: http://gmaps-samples.googlecode.com/svn/trunk/urlsigning/UrlSigner.java
 */
public class GoogleApiKeyDetector implements Detector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";

    private BugReporter bugReporter;

    public GoogleApiKeyDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        boolean keyStringField = false;
        for (Field f : javaClass.getFields()) {
            if (f.getName().equals("keyString")) { //The expected field name
                keyStringField = true;
                break;
            }
        }

        if (!keyStringField) { //No key field identify
            return;
        }

        //Class name left unchanged
        if (javaClass.getClassName().contains("UrlSigner")) {

            bugReporter.reportBug(new BugInstance(this, HARD_CODE_PASSWORD_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(javaClass).addField(new FieldVariable(javaClass.getClassName(), "keyString", "Ljava/lang/String;")));
            return;
        }

        //Event if the class name was refactor, the method "signRequest" would probably be left.
        for (Method m : javaClass.getMethods()) {
            MethodGen methodGen = classContext.getMethodGen(m);

            if (methodGen != null && "signRequest".equals(methodGen.getName())) {
                bugReporter.reportBug(new BugInstance(this, HARD_CODE_PASSWORD_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(javaClass).addField(new FieldVariable(javaClass.getClassName(), "keyString", "")));
            }
        }
    }

    @Override
    public void report() {
    }
}
