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
package com.h3xstream.findsecbugs.csrf;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ArrayElementValue;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.util.Arrays;
import java.util.List;

/**
 * Detects Spring CSRF unrestricted RequestMapping
 *
 * @author Pablo Tamarit
 */
public class SpringCsrfUnrestrictedRequestMappingDetector implements Detector {

    private static final String SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING_TYPE = "SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING";

    private static final String REQUEST_MAPPING_ANNOTATION_TYPE = "Lorg/springframework/web/bind/annotation/RequestMapping;";
    private static final String METHOD_ANNOTATION_ATTRIBUTE_KEY = "method";
    private static final List<String> UNPROTECTED_HTTP_REQUEST_METHODS = Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS");

    private BugReporter bugReporter;

    public SpringCsrfUnrestrictedRequestMappingDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        for (Method method : javaClass.getMethods()) {
            if (isVulnerable(method)) {
                bugReporter.reportBug(new BugInstance(this, SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING_TYPE, Priorities.HIGH_PRIORITY) //
                        .addClassAndMethod(javaClass, method));
            }
        }
    }

    @Override
    public void report() {

    }

    private static boolean isVulnerable(Method method) {

        // If the method is not annotated with `@RequestMapping`, there is no vulnerability.
        AnnotationEntry requestMappingAnnotation = findRequestMappingAnnotation(method);
        if (requestMappingAnnotation == null) {
            return false;
        }

        // If the `@RequestMapping` annotation is used without the `method` annotation attribute,
        // there is a vulnerability.
        ElementValuePair methodAnnotationAttribute = findMethodAnnotationAttribute(requestMappingAnnotation);
        if (methodAnnotationAttribute == null) {
            return true;
        }

        // If the `@RequestMapping` annotation is used with the `method` annotation attribute equal to `{}`,
        // there is a vulnerability.
        ElementValue methodAnnotationAttributeValue = methodAnnotationAttribute.getValue();
        if (isEmptyArray(methodAnnotationAttributeValue)) {
            return true;
        }

        // If the `@RequestMapping` annotation is used with the `method` annotation attribute but contains a mix of
        // unprotected and protected HTTP request methods, there is a vulnerability.
        return isMixOfUnprotectedAndProtectedHttpRequestMethods(methodAnnotationAttributeValue);
    }

    private static AnnotationEntry findRequestMappingAnnotation(Method method) {
        for (AnnotationEntry annotationEntry : method.getAnnotationEntries()) {
            if (REQUEST_MAPPING_ANNOTATION_TYPE.equals(annotationEntry.getAnnotationType())) {
                return annotationEntry;
            }
        }
        return null;
    }

    private static ElementValuePair findMethodAnnotationAttribute(AnnotationEntry requestMappingAnnotation) {
        for (ElementValuePair elementValuePair : requestMappingAnnotation.getElementValuePairs()) {
            if (METHOD_ANNOTATION_ATTRIBUTE_KEY.equals(elementValuePair.getNameString())) {
                return elementValuePair;
            }
        }
        return null;
    }

    private static boolean isEmptyArray(ElementValue methodAnnotationAttributeValue) {
        if (!(methodAnnotationAttributeValue instanceof ArrayElementValue)) {
            return false;
        }
        ArrayElementValue arrayElementValue = (ArrayElementValue) methodAnnotationAttributeValue;

        return arrayElementValue.getElementValuesArraySize() == 0;
    }

    private static boolean isMixOfUnprotectedAndProtectedHttpRequestMethods(ElementValue methodAnnotationAttributeValue) {
        if (!(methodAnnotationAttributeValue instanceof ArrayElementValue)) {
            return false;
        }
        ArrayElementValue arrayElementValue = (ArrayElementValue) methodAnnotationAttributeValue;

        // There cannot be a mix if there is no more than one element.
        if (arrayElementValue.getElementValuesArraySize() <= 1) {
            return false;
        }

        // Return `true` as soon as we find at least one unprotected and at least one protected HTTP request method.
        boolean atLeastOneUnprotected = false;
        boolean atLeastOneProtected = false;
        ElementValue[] elementValues = arrayElementValue.getElementValuesArray();
        for (ElementValue elementValue : elementValues) {
            if (UNPROTECTED_HTTP_REQUEST_METHODS.contains(elementValue.stringifyValue())) {
                atLeastOneUnprotected = true;
            } else {
                atLeastOneProtected = true;
            }
            if (atLeastOneUnprotected && atLeastOneProtected) {
                return true;
            }
        }
        return false;
    }
}
