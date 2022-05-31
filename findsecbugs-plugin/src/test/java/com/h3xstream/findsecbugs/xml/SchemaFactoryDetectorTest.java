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
package com.h3xstream.findsecbugs.xml;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SchemaFactoryDetectorTest extends BaseDetectorTest {

    @Test
    public void vulnerableExternalEntityRef() throws Exception {
        final String[] files = {
                getClassFilePath("testcode/xxe/schema/SchemaFactoryVulnerableExternalEntityRef")
        };

        final EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_SCHEMA_FACTORY")
                        .inClass("SchemaFactoryVulnerableExternalEntityRef").inMethod("main").atLine(21)
                        .build()
        );
    }

    @Test
    public void vulnerableExternalSchemaLocation() throws Exception {
        final String[] files = {
                getClassFilePath("testcode/xxe/schema/SchemaFactoryVulnerableExternalSchemaLocation")
        };

        final EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_SCHEMA_FACTORY")
                        .inClass("SchemaFactoryVulnerableExternalSchemaLocation").inMethod("main").atLine(21)
                        .build()
        );
    }

    @Test
    public void safeWithSecureProcessing() throws Exception {
        final String[] files = {
                getClassFilePath("testcode/xxe/schema/SchemaFactorySafeFeatureSecureProcessing")
        };

        final EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SCHEMA_FACTORY")
                        .inClass("SchemaFactorySafeFeatureSecureProcessing")
                        .build()
        );
    }

    @Test
    public void safeWithAccessExternalDisabled() throws Exception {
        final String[] files = {
                getClassFilePath("testcode/xxe/schema/SchemaFactorySafeAccessExternalDisabled")
        };

        final EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SCHEMA_FACTORY")
                        .inClass("SchemaFactorySafeAccessExternalDisabled")
                        .build()
        );
    }
}
