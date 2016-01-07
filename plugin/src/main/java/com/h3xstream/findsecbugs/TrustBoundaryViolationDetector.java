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
 * Trust Boundary Violation is fancy name to describe tainted value passed directly to session attribute.
 * This could be an expected behavior that allow an attacker to change the session state.
 */
public class TrustBoundaryViolationDetector extends BasicInjectionDetector {

    public TrustBoundaryViolationDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("trust-boundary-violation.txt", "TRUST_BOUNDARY_VIOLATION");
    }

    /**=
     * All or nothing :
     * <ul>
     * <li>If the taint to sink path is found, it is mark as high</li>
     * <li>If the source is not confirm, it is mark as low. This is will be the most common case.</li>
     * </ul>
     * @param taint Taint state
     * @return High or low confidence
     */
    @Override
    protected int getPriority(Taint taint) {
        if (taint.isTainted()) {
            return Priorities.HIGH_PRIORITY;
        } else if (!taint.isSafe()) {
            return Priorities.LOW_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
    }
}
