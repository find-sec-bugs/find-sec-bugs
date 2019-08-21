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
package com.h3xstream.findsecbugs.spring;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Similar to <code>edu.umd.cs.findbugs.ba.SignatureParser</code>
 *
 * It support the extraction of type in format such as:
 *  - java/util/List&lt;java/lang/String&gt; => java.util.List & java.lang.String
 */
public class SignatureParserWithGeneric {

    private String[] argumentsTypes;
    private String returnType;

    public SignatureParserWithGeneric(String signature) {
        String sigOnlyArgs = signature.substring(signature.indexOf("(")+1);
        String[] paramAndReturn = sigOnlyArgs.split("\\)");
        argumentsTypes = paramAndReturn[0].split(",");

        returnType = paramAndReturn[1];
    }

    public List<JavaClass[]> getArgumentsClasses() {
        List<JavaClass[]> types = new ArrayList<>();

        for(String argumentType : argumentsTypes) {
            if(argumentType.equals("")) continue;
            types.add(typeToJavaClass(argumentType));
        }
        return types;
    }

    public JavaClass[] getReturnClasses() {
        return typeToJavaClass(returnType);
    }

    private JavaClass[] typeToJavaClass(String type) {
        Matcher m = Pattern.compile("([^<]+)(<([^>]+)>)?").matcher(type);

        List<JavaClass> types = new ArrayList<>();
        if(m.find()) {
            try {
                types.add(Repository.lookupClass(cleanClassName(m.group(1))));
            } catch (ClassNotFoundException e) {
                //System.out.println(e.getMessage());
            }
            if(m.groupCount() == 3 && m.group(3) != null) {
                try {
                    types.add(Repository.lookupClass(cleanClassName(m.group(3))));
                } catch (ClassNotFoundException e) {
                    //System.out.println(e.getMessage());
                }
            }

        }
        return types.toArray(new JavaClass[types.size()]);
    }

    private String cleanClassName(String classname) {
        return classname.replaceAll("^L", "").replaceAll(";$","").replaceAll("/",".");
    }
}
