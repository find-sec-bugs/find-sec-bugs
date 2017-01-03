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

package com.h3xstream.findsecbugs.injection;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

public class SinkFilesValidationTest {
    private static final boolean DEBUG = false;

    @Test
    public void validateSinks() throws IOException {
        InputStream in = getClass().getResourceAsStream("/injection-sinks");
        assertNotNull(in, "Unable list the resources in the injection-sinks directory");

        SinksLoader loader = new SinksLoader();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while((line = br.readLine()) != null) {
            if(DEBUG) System.out.println("File :"+line);

            loader.loadConfiguredSinks(line, "DUMMY",
                    new SinksLoader.InjectionPointReceiver() {
                        @Override
                        public void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
                            if(DEBUG) System.out.println("[+] fmn: "+fullMethodName);
                            String[] methodParts = fullMethodName.split("\\.");

                            //Test the validity of the class name
                            String className = methodParts[0].replace('/','.');
                            validateClass(className);
                        }
                    }
            );
        }
    }


    public void validateClass(String className) {
        if(className.endsWith("$")) return; //Skipping Scala class
        if(className.startsWith("play.")) return; //Temporary skip Play
        if(className.startsWith("anorm")) return; //Skipping Scala anorm library classes
        if(className.startsWith("slick")) return; //Skipping Scala slick library classes
        if(className.contains(".log")) return;
        if(className.contains(".Log")) return;
        if(className.equals("javax.naming.directory.Context")) return; //FIXME: It seems to be a error in the LDAP configuration file
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println("[!] Class not found "+className);
            fail("Method configurations were added for a class that does not exist. It is likely a typographical error or because the API was not tested.");
        }
    }


}
