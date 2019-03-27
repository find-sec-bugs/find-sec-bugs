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

import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

public class TaintMethodConfigTest {
    @Test
    public void validateSimpleTaintMethodConfig() throws IOException {
        String sig = "android/database/DatabaseUtils.concatenateWhere(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";
        TaintMethodConfig config = new TaintMethodConfig(true).load("1#2");
        config.setTypeSignature(sig);
        String output = config.toString();
        System.out.println(output);

        assertTrue(output.contains("DatabaseUtils"));

        ////////

        String sig1    = "java/net/URLEncoder.encode(Ljava/lang/String;)Ljava/lang/String;";
        String config1 = "0|+URL_ENCODED,+XSS_SAFE";
        String sig2    = "java/net/URLDecoder.decode(Ljava/lang/String;)Ljava/lang/String;";
        String config2 = "0|-URL_ENCODED,-XSS_SAFE";

        TaintMethodConfig tmconfig1 = new TaintMethodConfig(true).load(config1);
        tmconfig1.setTypeSignature(sig1);
        TaintMethodConfig tmconfig2 = new TaintMethodConfig(true).load(config2);
        tmconfig2.setTypeSignature(sig2);

        output = tmconfig1.toString();
        System.out.println(output);
        assertTrue(output.contains("URLEncoder.encode"));
        assertTrue(output.contains("+URL_ENCODED")); //The order of the tags is subject to change
        assertTrue(output.contains("+XSS_SAFE"));
        output = tmconfig2.toString();
        System.out.println(output);
        assertTrue(output.contains("URLDecoder.decode"));
        assertTrue(output.contains("-URL_ENCODED"));
        assertTrue(output.contains("-XSS_SAFE"));

    }
}
