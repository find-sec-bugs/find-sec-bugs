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
package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AndroidInjectionSourceTest extends BaseDetectorTest {

//    @BeforeClass
//    public void setUp() {
////        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);
//    }

    @Test
    public void detectAndroidInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/android/AndroidSql"),
                getClassFilePath("testcode/sqli/android/AndroidContentProviderUsage")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        List<Integer> linesSQLiteDatabase = Arrays.asList(13, 15, 16, 17, 18, 20, 21, 23, 24, 26, 27, 29, 31, 32, 34, 35);
        for (Integer line : linesSQLiteDatabase) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_ANDROID")
                            .inClass("AndroidSql").inMethod("sampleSQLiteDatabase").atLine(line)
                            .build()
            );
        }

        int startLine = 41;
        for (Integer line : Arrays.asList(startLine, startLine++, startLine++, startLine++)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_ANDROID")
                            .inClass("AndroidSql").inMethod("sampleDatabaseUtils").atLine(line)
                            .build()
            );
        }
        List<Integer> linesSQLiteQueryBuilder = range(48, 96);
        linesSQLiteQueryBuilder.removeAll(Arrays.asList(49,55,60,66,72,78,84,88,92));
        for (Integer line : linesSQLiteQueryBuilder) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_ANDROID")
                            .inClass("AndroidSql").inMethod("sampleSQLiteQueryBuilder").atLine(line)
                            .build()
            );
        }

        //Only the previous 5 cases should be marked as vulnerable
        verify(reporter, times(linesSQLiteDatabase.size() + 4 + linesSQLiteQueryBuilder.size())).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_ANDROID")
                        .build()
        );
    }

    @Test
    public void detectAndroidContentProviderInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/android/AndroidContentProviderUsage"),
                getClassFilePath("testcode/sqli/android/LocalProvider"),
                getClassFilePath("testcode/sqli/android/NullContentProvider"),
                //getClassFilePath("android/content/ContentProvider")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        int lineStart = 6;
        for (Integer line : Arrays.asList(lineStart, lineStart++, lineStart++, lineStart++)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_ANDROID")
                            .inClass("AndroidContentProviderUsage").inMethod("detect").atLine(line)
                            .build()
            );
        }

        verify(reporter, times(4)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_ANDROID")
                        .inClass("AndroidContentProviderUsage").build()
        );
    }

    @Test
    public void detectAndroidCustomContentProvider() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/android/AndroidContentProviderUsage"),
                getClassFilePath("testcode/sqli/android/LocalProvider"),
                getClassFilePath("testcode/sqli/android/NullContentProvider"),
                //getClassFilePath("android/content/ContentProvider")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        //NullContentProvider should not rise any issue
        //LocalProvider should rise two bugs at line 97 and 104
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_ANDROID")
                        .inClass("LocalProvider").inMethod("query").atLine(97)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_ANDROID")
                        .inClass("LocalProvider").inMethod("query").atLine(104)
                        .build()
        );

        verify(reporter, times(2)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_ANDROID")
                        .inClass("LocalProvider").build()
        );
    }
}
