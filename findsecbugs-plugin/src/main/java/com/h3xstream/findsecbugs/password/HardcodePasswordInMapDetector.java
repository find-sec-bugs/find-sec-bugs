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

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import edu.umd.cs.findbugs.BugReporter;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

/**
 * Detect hard-code password in settings map (key value configurations constructed at runtime)
 */
public class HardcodePasswordInMapDetector extends AbstractHardcodePasswordInMapDetector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";

    private static final InvokeMatcherBuilder MAP_PUT_METHOD = invokeInstruction().atMethod("put")
            .withArgs("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final InvokeMatcherBuilder MAP_PUT_IF_ABSENT_METHOD = invokeInstruction().atMethod("putIfAbsent")
            .withArgs("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final InvokeMatcherBuilder HASHTABLE_SET_PROPERTY = invokeInstruction().atMethod("setProperty")
            .withArgs("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");

    public HardcodePasswordInMapDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        assert invoke != null && cpg != null;

        if (MAP_PUT_METHOD.matches(invoke, cpg)
                || MAP_PUT_IF_ABSENT_METHOD.matches(invoke, cpg)
                || HASHTABLE_SET_PROPERTY.matches(invoke, cpg)) {
            return new InjectionPoint(new int[]{0}, HARD_CODE_PASSWORD_TYPE);
        }
        return InjectionPoint.NONE;
    }
}
