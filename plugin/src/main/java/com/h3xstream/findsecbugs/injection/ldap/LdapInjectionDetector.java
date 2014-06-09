/**
 * Find Security Bugs
 * Copyright (c) 2014, Philippe Arteau, All rights reserved.
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

import com.h3xstream.findsecbugs.injection.InjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import edu.umd.cs.findbugs.BugReporter;

public class LdapInjectionDetector extends InjectionDetector {

    private static final String LDAP_INJECTION_TYPE = "LDAP_INJECTION";

    public LdapInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    public InjectionSource[] getInjectionSource() {
        return new InjectionSource[]{new JndiLdapInjectionSource(), new UnboundIdLdapInjectionSource()};
    }

    @Override
    public String getBugType() {
        return LDAP_INJECTION_TYPE;
    }
}
