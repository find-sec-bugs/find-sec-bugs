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

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class FindSecBugsGlobalConfigTest {

    @Test
    public void version() {
        FindSecBugsGlobalConfig.getInstance().setFindSecBugsVersion("1.3.3.7");
        assertEquals(FindSecBugsGlobalConfig.getInstance().getFindSecBugsVersion(),"1.3.3.7");
    }

}
