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
package com.h3xstream.findsecbugs.injection.trust;

import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;

/**
 * <p>
 * Trust Boundary Violation is fancy name to describe tainted value passed directly to session attribute.
 * This could be an expected behavior that allow an attacker to change the session state.
 * </p>
 * <p>
 * When the attribute name is dynamic, it is a lot more suspicious than when it is a dynamic value.
 * <code>setAttribute( suspiciousValue, "true")</code>
 * vs
 * <code>setAttribute( "language" , commonDynamicValue)</code>
 * </p>
 * <p>
 * For this reason, the trust boundary violation was split in two detector.
 * </p>
 *
 * @see TrustBoundaryViolationValueDetector
 */
public class TrustBoundaryViolationAttributeDetector extends BasicInjectionDetector {

    public TrustBoundaryViolationAttributeDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("trust-boundary-violation-attribute.txt", "TRUST_BOUNDARY_VIOLATION");
    }

    /**
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
            return Priorities.NORMAL_PRIORITY;
        }
        else if (!taint.isSafe()) {
            return Priorities.LOW_PRIORITY;
        }
        else {
            return Priorities.IGNORE_PRIORITY;
        }
    }
}
