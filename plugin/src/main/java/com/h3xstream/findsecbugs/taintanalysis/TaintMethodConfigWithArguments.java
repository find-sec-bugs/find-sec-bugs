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
import java.util.regex.Pattern;

/**
 * Summary of information about a taint analysis method with configured arguments .<br />
 * <br />
 * Can be used to fine-tune false-positives in specific classes.<br />
 * <br />
 * Examples:<br />
 *      org/apache/jasper/runtime/PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}",SAFE,SAFE,NULL):SAFE
 *
 * @author Tomas Polesovsky (Liferay, Inc.)
 * @author Brad Flood (Keyhole Software) 
 */
public class TaintMethodConfigWithArguments extends TaintMethodConfig {
    private static final Pattern methodWithStringConstantOrEnumPattern;
    
    protected static final List<String> EL_PROCESSOR_METHODS = Arrays.asList("org/apache/jasper/runtime/PageContextImpl.proprietaryEvaluate");
    protected static final List<String> SAFE_EXPRESSIONS = Arrays.asList("pageContext.request.contextPath");

    static {

        String javaIdentifierRegex = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
        String classNameRegex = javaIdentifierRegex+"(\\/"+javaIdentifierRegex+")*";
        String methodRegex = "(("+javaIdentifierRegex+"(\\$extension)?)|(<init>))";

        //org/apache/jasper/runtime/PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}",SAFE,SAFE,NULL):SAFE        
        String stringConstantRegex = "\"[^\"]*\"";
        String enumNameRegex = "[A-Z_]+";
        String methodArguments = "(" + stringConstantRegex + ",?|" + enumNameRegex + ",?)*";
        String methodWithStringConstantOrEnumRegex = classNameRegex + "\\." + methodRegex + "\\(" + methodArguments + "\\)";
        methodWithStringConstantOrEnumPattern = Pattern.compile(methodWithStringConstantOrEnumRegex);
    }

    /**
     * Constructs an empty configured summary
     */
    public TaintMethodConfigWithArguments() {
        super(true);
    }

    public static boolean accepts(String typeSignature, String config) {
        boolean patternMatches = methodWithStringConstantOrEnumPattern.matcher(typeSignature).matches() &&
                TaintMethodConfig.configPattern.matcher(config).matches();
        
        if (!isSafeExpression(typeSignature)) {
            return false ;
        }
        
        return patternMatches ;
    }

    
    private static boolean isSafeExpression(String typeSignature) {
        if (!involvesExpressionLanguageEvaluation(typeSignature)) {
            return true ;
        }
        
        for (String safeExpression : SAFE_EXPRESSIONS) {
            if (typeSignature.contains(safeExpression)) {
                return true ;
            } 
        }
        return false ;
    }
    
    private static boolean involvesExpressionLanguageEvaluation(String typeSignature) {       
        for (String elProcessorMethod : EL_PROCESSOR_METHODS) {
            if (typeSignature.startsWith(elProcessorMethod)) {
                return true ;
            } 
        }
        return false ;
    }
}
