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
package com.h3xstream.findsecbugs.taintanalysis.extra;

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrameAdditionalVisitor;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.MethodGen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This detector will set the return value of PageContextImpl.proprietaryEvaluate as safe for XSS is some very specific case.
 * Because the most common false positive are similar, can use a whitelist of expression to ignored.
 *
 * Here are some patterns that, we considered safe:
 * <code>${e:forHtmlContent(param.test_param)}</code> OWASP Java Encoder being used
 * <code>${e:forHtmlContent(someVariable1)}</code>
 * <code>${pageContext.request.contextPath}</code> Not to be confused with the pathInfo. This information is not coming from the client.
 *
 * With Tomcat 5.5, their seems to be a different API with a 5th parameter (boolean).
 * <a href="https://tomcat.apache.org/tomcat-5.5-doc/jasper/docs/api/org/apache/jasper/runtime/PageContextImpl.html#proprietaryEvaluate(java.lang.String,%20java.lang.Class,%20javax.servlet.jsp.PageContext,%20org.apache.jasper.runtime.ProtectedFunctionMapper,%20boolean)">Ref</a>
 *
 * Additional safe regular expression patterns can be added by specifying a file in the system property
 * "findsecbugs.jstlsafe.customregexfile".
 */
public class JstlExpressionWhiteLister extends BasicInjectionDetector implements TaintFrameAdditionalVisitor {

    private static final String SYSTEM_PROPERTY = "findsecbugs.jstlsafe.customregexfile";

    private static final Pattern TAG_FOR_HTML_CONTENT_PATTERN = Pattern.compile("\\$\\{e:forHtmlContent\\([a-zA-Z0-9\\._]+\\)\\}");
    private static final Pattern TAG_TO_SAFE_JSON_PATTERN = Pattern.compile("^\\$\\{\\s*[a-zA-Z]+:toSafeJSON\\([a-zA-Z0-9\\-#,\\\'\\\"\\&\\[\\]@\\\\ \\._\\(\\):]+\\)\\s*\\}$");
    private static final Pattern TAG_SAFE_QUOTE_PATTERN = Pattern.compile("^\\$\\{\\s*[a-zA-Z]+:safeQuote\\([a-zA-Z0-9\\-#,\\\'\\\"\\&\\[\\]@\\\\ \\._\\(\\):]+\\)\\s*\\}$");
    private static final String CONTEXT_PATH_PATTERN = "${pageContext.request.contextPath}";

    private static final InvokeMatcherBuilder JAVAX_PROPRIETARY_EVALUATE = invokeInstruction()
            .atClass("org/apache/jasper/runtime/PageContextImpl")
            .atMethod("proprietaryEvaluate")
            .withArgs("(Ljava/lang/String;Ljava/lang/Class;Ljavax/servlet/jsp/PageContext;Lorg/apache/jasper/runtime/ProtectedFunctionMapper;)Ljava/lang/Object;");
    private static final InvokeMatcherBuilder JAKARTA_PROPRIETARY_EVALUATE = invokeInstruction()
            .atClass("org/apache/jasper/runtime/PageContextImpl")
            .atMethod("proprietaryEvaluate")
            .withArgs("(Ljava/lang/String;Ljava/lang/Class;Ljakarta/servlet/jsp/PageContext;Lorg/apache/jasper/runtime/ProtectedFunctionMapper;)Ljava/lang/Object;");

    private final List<Pattern> safePatterns;

    public JstlExpressionWhiteLister(BugReporter bugReporter) {
        super(bugReporter);

        this.safePatterns = getCustomPatterns();

        registerVisitor(this);
    }


    @Override
    public void visitInvoke(InvokeInstruction invoke, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters, ConstantPoolGen cpg) throws DataflowAnalysisException {

        if(isProprietaryEvaluateMethod(invoke, cpg)) {
            Taint defaultVal = parameters.get(3); //The expression is the fourth parameter starting from the right. (Top of the stack last arguments)
            if(defaultVal.getConstantValue() != null) {

                String expression = defaultVal.getConstantValue();
                if(TAG_FOR_HTML_CONTENT_PATTERN.matcher(expression).find() ||
                   TAG_TO_SAFE_JSON_PATTERN.matcher(expression).find() ||
                   TAG_SAFE_QUOTE_PATTERN.matcher(expression).find() ||
                   CONTEXT_PATH_PATTERN.equals(expression)) {

                    Taint value = frameType.getTopValue();
                    value.addTag(Taint.Tag.XSS_SAFE);
                }
                else {
                    for (final Pattern safePattern : safePatterns) {
                        if (safePattern.matcher(expression).find()) {
                            Taint value = frameType.getTopValue();
                            value.addTag(Taint.Tag.XSS_SAFE);
                        }
                    }
                }
            }
        }
    }

    private boolean isProprietaryEvaluateMethod(InvokeInstruction invoke, ConstantPoolGen cpg) {
        return JAVAX_PROPRIETARY_EVALUATE.matches(invoke, cpg) || JAKARTA_PROPRIETARY_EVALUATE.matches(invoke, cpg);
    }

    @Override
    public void visitLoad(LoadInstruction load, MethodGen methodGen, TaintFrame frameType, int numProduced, ConstantPoolGen cpg) {
    }

    @Override
    public void visitField(FieldInstruction put, MethodGen methodGen, TaintFrame frameType, Taint taintFrame, int numProduced, ConstantPoolGen cpg) throws Exception {
    }

    @Override
    public void visitReturn(MethodGen methodGen, Taint returnValue, ConstantPoolGen cpg) throws Exception {
    }

    private static List<Pattern> getCustomPatterns() {
        final List<Pattern> safePatterns = new ArrayList<>();
        String propertyValue = FindSecBugsGlobalConfig.getInstance().loadFromSystem(SYSTEM_PROPERTY, "");
        if(!propertyValue.trim().equals("")) {
            for (final String patternFile : propertyValue.split(File.pathSeparator)) {
                safePatterns.addAll(customPatternsFromFile(patternFile));
            }
        }
        return safePatterns;
    }

    private static List<Pattern> customPatternsFromFile(final String path) {
        File file = new File(path);
        try (InputStream stream = file.exists()
             ? new FileInputStream(file)
             : JstlExpressionWhiteLister.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                String message = String.format("Could not add custom patterns. "
                        + "Neither file %s nor resource matching %s found.",
                        file.getAbsolutePath(), path);
                throw new IllegalArgumentException(message);
            }

            return patternsFromStream(stream);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load custom safe regex patterns from " + path, ex);
        }
    }

    private static List<Pattern> patternsFromStream(final InputStream stream) throws IOException
    {
        final List<Pattern> patterns = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line.trim())) {
                    patterns.add(Pattern.compile(line));
                }
            }
        }
        return patterns;
    }
}
