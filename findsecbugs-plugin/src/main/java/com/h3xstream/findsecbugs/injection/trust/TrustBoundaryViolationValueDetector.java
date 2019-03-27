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


public class TrustBoundaryViolationValueDetector extends BasicInjectionDetector {

    public TrustBoundaryViolationValueDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("trust-boundary-violation-value.txt", "TRUST_BOUNDARY_VIOLATION");
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
        //**Low risk**
        //It is very common that variable are not sanetize and store in session.
        //By it self it pose little risk. The thinking is the injection or the critical operation
        //will be catch.
        //After all storing value in the session is not so different to storing value in local variables or any indirection.
        //**False positive**
        //The usual and most common configuration is to hide LOW priority (confidence).
        //This way this FP producer will not polute day to day review by developers.

        if (taint.isTainted() || !taint.isSafe()) {
            return Priorities.LOW_PRIORITY;
        }
        else {
            return Priorities.IGNORE_PRIORITY;
        }
    }
}
