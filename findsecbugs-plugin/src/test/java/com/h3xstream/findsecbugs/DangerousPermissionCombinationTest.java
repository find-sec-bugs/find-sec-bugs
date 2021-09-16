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

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class DangerousPermissionCombinationTest extends BaseDetectorTest {
    @Test
    public void testReflectPermissionSuppressAccessChecks() throws Exception {
        verifyDPCBug("ReflectPermissionSuppressAccessChecks", 12, true);
    }

    @Test
    public void testReflectPermissionNewProxyInPackage() throws Exception {
        verifyDPCBug("ReflectPermissionNewProxyInPackage", 12, false);
    }

    @Test
    public void testRuntimePermissionCreateClassLoader() throws Exception {
        verifyDPCBug("RuntimePermissionCreateClassLoader", 11, true);
    }

    @Test
    public void testRuntimePermissionGetClassLoader() throws Exception {
        verifyDPCBug("RuntimePermissionGetClassLoader", 11, false);
    }

    private void verifyDPCBug(String className, int line, boolean isBug) throws Exception {
        //Locate test code
        String[] files = {
            getClassFilePath("testcode/permission/" + className + ".java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, isBug ? times(1) : never()).doReportBug(
            bugDefinition()
                    .bugType("DANGEROUS_PERMISSION_COMBINATION")
                    .inClass(className)
                    .inMethod("getPermissions")
                    .withPriority("Medium")
                    .atLine(line)
                    .build()
        );
    }
}
