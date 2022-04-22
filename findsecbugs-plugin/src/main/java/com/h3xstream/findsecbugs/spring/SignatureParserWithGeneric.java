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

import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.generic.GenericObjectType;
import edu.umd.cs.findbugs.ba.generic.GenericSignatureParser;
import edu.umd.cs.findbugs.ba.generic.GenericUtilities;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Similar to <code>edu.umd.cs.findbugs.ba.SignatureParser</code>
 *
 * It support the extraction of type in format such as:
 *  - java/util/List&lt;java/lang/String&gt; => java.util.List & java.lang.String
 */
public class SignatureParserWithGeneric {

    private final List<String> argumentsTypes;
    private final String returnType;

    public SignatureParserWithGeneric(String signature) {
        GenericSignatureParser delegate = new GenericSignatureParser(signature);
        List<String> arguments = new ArrayList<>();
        delegate.parameterSignatureIterator().forEachRemaining(arguments::add);
        this.argumentsTypes = Collections.unmodifiableList(arguments);
        this.returnType = delegate.getReturnTypeSignature();
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

    private JavaClass[] typeToJavaClass(String signature) {
        if ("V".equals(signature)) {
            // Special case for void
            return new JavaClass[0];
        }
        
        Type type = GenericUtilities.getType(signature);
        
        List<JavaClass> types =  typeToJavaClass(type);
        
        return types.toArray(new JavaClass[types.size()]);
    }
    
    private List<JavaClass> typeToJavaClass(Type type) {
        List<JavaClass> types = new ArrayList<>();
        
        if (type instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) type;
            
            return typeToJavaClass(arrayType.getBasicType());
        } else if (type instanceof GenericObjectType) {
            GenericObjectType genericObjectType = (GenericObjectType) type;
            
            try {
                types.add(Repository.lookupClass(genericObjectType.getClassName()));
            } catch (ClassNotFoundException e) {
                AnalysisContext.reportMissingClass(e);
            }
            
            if (genericObjectType.getParameters() != null) {
                for (Type parameterType : genericObjectType.getParameters()) {
                    types.addAll(typeToJavaClass(parameterType));
                }
            }
        } else if (type instanceof ObjectType) {
            ObjectType objectType = (ObjectType) type;

            try {
                types.add(Repository.lookupClass(objectType.getClassName()));
            } catch (ClassNotFoundException e) {
                AnalysisContext.reportMissingClass(e);
            }
        }
        
        return types;
    }
}
