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

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

/**
 * Detect Java object deserialization
 *
 * @author Alexander Minozhenko
 */
public class ObjectDeserializationDetector extends OpcodeStackDetector {
    private final BugReporter bugReporter;

    public ObjectDeserializationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int code) {
        printOpCode(code);
        if ((code == INVOKEVIRTUAL)) {
            if("java/io/ObjectInputStream".equals(getClassConstantOperand()) || getClassConstantOperand().contains("InputStream")) {

                String methodName = getNameConstantOperand();
                if (methodName.equals("readObject")) {
                    bugReporter.reportBug(new BugInstance(this, "OBJECT_DESERIALIZATION", NORMAL_PRIORITY).addClassAndMethod(this).addSourceLine(this));
                }
            }
        }
    }
}
