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
package com.h3xstream.findsecbugs.common;

import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.Hierarchy;
import org.apache.bcel.classfile.JavaClass;

public class InterfaceUtils {
    /**
     * Test if the given class is a subtype of ONE of the super classes given.
     * <br/>
     * The following test that the class is a subclass of Hashtable.
     *
     * <pre>
     * boolean isHashtable = InterfaceUtils.isSubtype( classThatCouldBeAHashTable, "java.util.Hashtable");
     * </pre>
     *
     * @param javaClass Class to test
     * @param superClasses If classes extends or implements those classes
     * @return
     */
    public static boolean isSubtype(JavaClass javaClass, String... superClasses) {
        return isSubtype(javaClass.getClassName(),superClasses);
    }

    /**
     * Test if the given class is a subtype of ONE of the super classes given.
     * <br/>
     * The following test that the class is a subclass of Hashtable.
     * <pre>
     * boolean isHashtable = InterfaceUtils.isSubtype( classThatCouldBeAHashTable, "java.util.Hashtable");
     * </pre>
     *
     * @param className Class to test
     * @param superClasses If classes extends or implements those classes
     * @return
     */
    public static boolean isSubtype(String className, String... superClasses) {
        for(String potentialSuperClass : superClasses) {
            try {
                if(Hierarchy.isSubtype(className, potentialSuperClass)) {
                    return true;
                }
            } catch (ClassNotFoundException e) {
                AnalysisContext.reportMissingClass(e);
            }
        }
        return false;
    }
}
