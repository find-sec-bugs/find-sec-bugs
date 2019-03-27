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
package com.h3xstream.findsecbugs.injection.ldap;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JndiLdapInjectionSourceAdditionalSignaturesTest extends BaseDetectorTest {

    @Test
    public void detectLdapInjectionInQuery() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/ldap/JndiLdapAdditionalSignature")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(39, 40, 41, 42, /**/ 44, 45, 46, 47, /**/ 49, 50, 51, 52, /**/ 54, 55, 56, 57)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("LDAP_INJECTION")
                            .inClass("JndiLdapAdditionalSignature")
                            .atLine(line)
                            .build()
            );
        }


        verify(reporter, times(16)).doReportBug(
                bugDefinition().bugType("LDAP_INJECTION").inMethod("moreLdapInjections").build()
        );
    }

    @Test
    public void detectLdapInjectionInQuerySunApi() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/ldap/JndiLdapAdditionalSignature")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(83, 84, 85, 86, /**/ 88, 89, 90, 91)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("LDAP_INJECTION")
                            .inClass("JndiLdapAdditionalSignature")
                            .atLine(line)
                            .build()
            );
        }


        verify(reporter, times(8)).doReportBug(
                bugDefinition().bugType("LDAP_INJECTION").inMethod("ldapInjectionSunApi").build()
        );
    }

    @Test
    public void detectLdapInjectionEdgeCase() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/ldap/JndiLdapSpecial")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("LDAP_INJECTION")
                        .inClass("JndiLdapSpecial")
                        .atLine(16)
                        .build()
        );

    }

}
