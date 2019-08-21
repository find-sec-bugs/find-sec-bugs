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
package com.h3xstream.findbugs.test.matcher;

import com.h3xstream.findbugs.test.jsp.DebugExtensionExtractor;
import com.h3xstream.findbugs.test.jsp.SmapParser;
import com.h3xstream.findbugs.test.service.ClassFileLocator;
import edu.umd.cs.findbugs.BugInstance;
import org.apache.commons.io.IOUtils;
import org.mockito.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DSL to build BugInstanceMatcher
 */
public class BugInstanceMatcherBuilder {

    private static final Logger log = LoggerFactory.getLogger(BugInstanceMatcherBuilder.class);

    private String bugType;
    private String className;
    private String methodName;
    private String fieldName;
    private Integer lineNumber;
    private Integer lineNumberApprox;
    private String priority;
    private String jspFile;
    private Integer jspLine;
    private List<String> unknownSources = new ArrayList<>();

    public BugInstanceMatcherBuilder bugType(String bugType) {
        this.bugType = bugType;
        return this;
    }

    public BugInstanceMatcherBuilder inClass(String className) {
        this.className = className;
        return this;
    }

    public BugInstanceMatcherBuilder inMethod(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public BugInstanceMatcherBuilder atField(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public BugInstanceMatcherBuilder atLine(int lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    /**
     * @deprecated Use atJspLine for JSP line mapping
     * @param lineNumberApprox Line to verify accepting an offset of 1
     * @return
     */
    @Deprecated
    public BugInstanceMatcherBuilder atLineApprox(int lineNumberApprox) {
        this.lineNumberApprox = lineNumberApprox;
        return this;
    }

    /**
     * Define the priority of the detector
     * @param priority Priority can be "High", "Medium" or "Low"
     * @return
     */
    public BugInstanceMatcherBuilder withPriority(String priority) {
        this.priority = priority;
        return this;
    }

    public BugInstanceMatcherBuilder inJspFile(String jspFile) {
        this.jspFile = jspFile;
        return this;
    }

    public BugInstanceMatcherBuilder atJspLine(Integer jspLine) {
        this.jspLine = jspLine;
        return this;
    }

    public BugInstanceMatcherBuilder causeBySource(String unknownSource) {
        this.unknownSources.add(unknownSource);
        return this;
    }

    /**
     * @return Mockito Matcher
     */
    public BugInstance build() {

        //JSP line to Java source conversion
        List<Integer> multipleChoicesLine = null;
        if(jspLine != null) {
            if(jspFile != null) {
                //Map JSP lines to Java base on the smap file if available
                multipleChoicesLine = mapJspToJavaLine(jspFile,jspLine);
            }
            else {
                throw new RuntimeException("JSP file not set.");
            }
        }

        return Matchers.argThat(new BugInstanceMatcher(bugType, className, methodName, fieldName, lineNumber, lineNumberApprox, priority, jspFile, multipleChoicesLine,unknownSources));
    }

    private static List<Integer>  mapJspToJavaLine(String jspFile, Integer jspLine) {
        List<Integer> outJavaLines = new ArrayList<>();

        ClassFileLocator locator = new ClassFileLocator();
        String jspClassLocation = locator.getJspFilePath(jspFile);
        File smapFile = new File(jspClassLocation + ".smap");


        try {
            String debugInfo;
            if(smapFile.exists()) {
                debugInfo = IOUtils.toString(new FileInputStream(smapFile),"UTF-8");
            }
            else {
                //
                if(!new File(jspClassLocation).exists())
                    throw new RuntimeException("Unable to locate the class file "+ jspClassLocation);

                debugInfo = new DebugExtensionExtractor().getDebugExtFromClass(new FileInputStream(jspClassLocation));

                if(debugInfo == null)
                    throw new RuntimeException("SMAP info is missing. ("+smapFile+" or embedded in "+jspClassLocation+")");
            }

            //Convert
            SmapParser smapDebug = new SmapParser(debugInfo);

            for(Integer val : smapDebug.getOriginalLine(jspLine)) outJavaLines.add(val);

            log.info("The JSP line "+jspLine+" was mapped to "+ Arrays.toString(outJavaLines.toArray()));
            if(outJavaLines.isEmpty()) {
                throw new RuntimeException("Unable to find the mapping for the JSP line "+jspLine);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to open the smap file.",e);
        }
        return outJavaLines;
    }

}
