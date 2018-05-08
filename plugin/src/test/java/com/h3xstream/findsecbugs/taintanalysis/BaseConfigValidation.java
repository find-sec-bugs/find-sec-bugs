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

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.fail;

public class BaseConfigValidation {
    private static final boolean DEBUG = false;


    TaintConfigLoader loader = new TaintConfigLoader();

    private List<String> classesDeprecatedInJava8 = Arrays.asList("java.time.ZonedId");
    private List<String> classesDeprecatedInJava9 = Arrays.asList("javax.activation.FileDataSource",
            "javax.xml.bind.DatatypeConverter");

    /**
     * Validate if the class name exists.
     * It is expecting that API will be in the classpath. The class are either part of the dependencies or in most
     * cases the stubs
     * @param className
     * @param origfileName
     */
    public void validateClass(String className, String origfileName) {
        if(classesDeprecatedInJava8.contains(className)) return;
        if(classesDeprecatedInJava9.contains(className)) return;

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
            String classNotFound = String.format("[!] Class not found %s (%s)",className,origfileName);
            //System.err.println(classNotFound);
            fail("Configurations were added for a class that does not exist. It is likely a typographical error or because the API was not tested. "+classNotFound);
        }
    }
}
