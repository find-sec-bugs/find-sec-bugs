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
package com.h3xstream.findsecbugs.taintanalysis;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

import static org.mockito.Mockito.*;

public class CharacterTaintPropagationTest extends BaseDetectorTest {

    @Test
    public void validateCharacterTaintPropagation() throws Exception {
        //Locate test code
        String[] files = { getClassFilePath("testcode/taint/CharacterTaintPropagation") };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());

        analyze(files, reporter);

        verify(reporter, times(9)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN").build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromCharConcat")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromChar")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromCharToString")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromCharGetName")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromCharStringBuilder")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromCharStringBuffer")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromCharacterToString")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromCharacterConcat")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("unsafeFileFromString")
                        .withPriority("Medium")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("safeFileFromConstant")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("CharacterTaintPropagation").inMethod("safeFileFromConstantWithInt")
                        .build());

    }
}
