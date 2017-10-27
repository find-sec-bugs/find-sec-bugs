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
package com.h3xstream.findsecbugs.graph.util;

import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;

import java.util.ArrayList;

public class ClassMetadata {


    /**
     * This utility method will extract all superclass of a given Class.
     *
     * @param cd ClassDescriptor
     * @return
     */
    public static ArrayList<ClassDescriptor> listOfSuperClasses(ClassDescriptor cd){
        ClassDescriptor temp;
        ArrayList<ClassDescriptor> superClasses = new ArrayList<>();
        XClass xclassTemp = AnalysisContext.currentXFactory().getXClass(cd);
        if (xclassTemp != null){
            temp = xclassTemp.getSuperclassDescriptor();
        }
        else temp = null;

        if (temp != null){
            if (!temp.getClassName().equals("java/lang/Object")){
                superClasses.add(temp);
                while (temp != null && !temp.getClassName().equals("java/lang/Object")){
                    XClass xclass = AnalysisContext.currentXFactory().getXClass(temp);
                    if (xclass != null){
                        ClassDescriptor xSuper = xclass.getSuperclassDescriptor();
                        if (xSuper != null) {
                            superClasses.add(xSuper);
                        }
                        temp = xSuper;
                    }
                    else break;
                }
            }
        }
        return superClasses;
    }
}
