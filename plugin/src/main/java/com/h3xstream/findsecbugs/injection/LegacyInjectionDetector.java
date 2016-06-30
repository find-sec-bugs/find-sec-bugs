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
package com.h3xstream.findsecbugs.injection;

import edu.umd.cs.findbugs.BugReporter;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * Detector designed for extension to detect injection vulnerabilities using
 * the original mechanism with {@link InjectionSource} class
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public abstract class LegacyInjectionDetector extends BasicInjectionDetector {

    protected LegacyInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
            InstructionHandle handle) {
        InjectionPoint injectionPoint = null;
        for (InjectionSource source : getInjectionSource()) {
            injectionPoint = source.getInjectableParameters(invoke, cpg, handle);
            if (injectionPoint != InjectionPoint.NONE) {
                break;
            }
        }
        if (injectionPoint == null || injectionPoint == InjectionPoint.NONE) {
            injectionPoint = super.getInjectionPoint(invoke, cpg, handle);
        }
        return injectionPoint;
    }

    public abstract InjectionSource[] getInjectionSource();
}
