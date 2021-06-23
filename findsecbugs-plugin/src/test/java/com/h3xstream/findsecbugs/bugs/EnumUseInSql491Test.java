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
package com.h3xstream.findsecbugs.bugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * Regression test to reproduce the issue reported :
 * https://github.com/find-sec-bugs/find-sec-bugs/issues/491
 */
public class EnumUseInSql491Test extends BaseDetectorTest {

    @Test
    public void avoidSqlInjectionWithArrayUsage() throws Exception {

        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/bugs/EnumUseInSql491.java"),
                getClassFilePath("testcode/bugs/SomeEnum.java"),
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //No SQL injections should be trigger from this sample
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JPA") //
                        .inClass("EnumUseInSql491") //
                        .build()
        );
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION") //
                        .inClass("EnumUseInSql491") //
                        .build()
        );
    }

}
