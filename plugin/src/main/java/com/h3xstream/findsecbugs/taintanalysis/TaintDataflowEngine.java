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

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.DepthFirstSearch;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.IAnalysisCache;
import edu.umd.cs.findbugs.classfile.IMethodAnalysisEngine;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.apache.bcel.generic.MethodGen;

/**
 * Requests or creates needed objects and execute taint analysis,
 * extends taint method summaries with analyzed methods
 * 
 * @author David Formanek
 */
public class TaintDataflowEngine implements IMethodAnalysisEngine<TaintDataflow> {

    private static final String METHODS_SUMMARIES_PATH = "taint-config/";
    private static final String[] METHODS_SUMMARIES_FILENAMES = {
        "java-lang.txt",
        "java-ee.txt",
        "collections.txt",
        "java-net.txt",
        "scala.txt",
        "logging.txt",
        "other.txt",
    };
    private static final String SAFE_ENCODERS_PATH = "safe-encoders/";
    private static final String[] SAFE_ENCODERS_FILENAMES = {
        "owasp.txt",
        "apache-commons.txt",
        "other.txt"
    };

    private final TaintMethodSummaryMap methodSummaries = new TaintMethodSummaryMap();

    private static Writer writer = null;
    
    static {
        if (FindSecBugsGlobalConfig.getInstance().isDebugOutputSummaries()) {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("derived-summaries.txt"), "utf-8"));
            } catch (UnsupportedEncodingException ex) {
                assert false : ex.getMessage();
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public TaintDataflowEngine() {
        for (String path : METHODS_SUMMARIES_FILENAMES) {
            loadMethodSummaries(METHODS_SUMMARIES_PATH.concat(path));
        }
        for (String path : SAFE_ENCODERS_FILENAMES) {
            loadMethodSummaries(SAFE_ENCODERS_PATH.concat(path));
        }
    }
    
    private void loadMethodSummaries(String path) {
        assert path != null && !path.isEmpty();
        InputStream stream = null;
        try {
            stream = getClass().getClassLoader().getResourceAsStream(path);
            methodSummaries.load(stream);
        } catch (IOException ex) {
            throw new RuntimeException("cannot load summaries from " + path, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    throw new RuntimeException("cannot close stream", ex);
                }
            }
        }
    }
    
    @Override
    public TaintDataflow analyze(IAnalysisCache cache, MethodDescriptor descriptor)
            throws CheckedAnalysisException {
        CFG cfg = cache.getMethodAnalysis(CFG.class, descriptor);
        DepthFirstSearch dfs = cache.getMethodAnalysis(DepthFirstSearch.class, descriptor);
        MethodGen methodGen = cache.getMethodAnalysis(MethodGen.class, descriptor);
        TaintAnalysis analysis = new TaintAnalysis(methodGen, dfs, descriptor, methodSummaries);
        TaintDataflow flow = new TaintDataflow(cfg, analysis);
        flow.execute();
        analysis.finishAnalysis();
        if (FindSecBugsGlobalConfig.getInstance().isDebugOutputSummaries() && writer != null) {
            TaintMethodSummary derivedSummary = methodSummaries.get(getSlashedMethodName(methodGen));
            if (derivedSummary != null) {
                try {
                    writer.append(getSlashedMethodName(methodGen) + ":" + derivedSummary + "\n");
                    writer.flush();
                } catch (IOException ex) {
                    System.out.println("cannot write: " + ex.getMessage());
                }
            }
        }
        return flow;
    }

    private static String getSlashedMethodName(MethodGen methodGen) {
        String methodNameWithSignature = methodGen.getName() + methodGen.getSignature();
        String slashedClassName = methodGen.getClassName().replace('.', '/');
        return slashedClassName + "." + methodNameWithSignature;
    }
    
    @Override
    public void registerWith(IAnalysisCache iac) {
        iac.registerMethodAnalysisEngine(TaintDataflow.class, this);
    }
}
