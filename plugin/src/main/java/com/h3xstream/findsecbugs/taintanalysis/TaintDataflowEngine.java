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
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.DepthFirstSearch;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.IAnalysisCache;
import edu.umd.cs.findbugs.classfile.IMethodAnalysisEngine;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import edu.umd.cs.findbugs.io.IO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.bcel.generic.MethodGen;

/**
 * Requests or creates needed objects and execute taint analysis,
 * extends taint method summaries with analyzed methods
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintDataflowEngine implements IMethodAnalysisEngine<TaintDataflow> {

    private static final FindSecBugsGlobalConfig CONFIG = FindSecBugsGlobalConfig.getInstance();
    private static final Logger LOGGER = Logger.getLogger(TaintDataflowEngine.class.getName());
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
        if (CONFIG.isDebugOutputSummaries()) {
            try {
                final String fileName = "derived-summaries.txt";
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(fileName), "utf-8"));
                // note: writer is not closed until the end
                LOGGER.info("Derived method summaries will be output to " + fileName);
            } catch (UnsupportedEncodingException ex) {
                assert false : ex.getMessage();
            } catch (FileNotFoundException ex) {
                AnalysisContext.logError("File for derived summaries cannot be created or opened", ex);
            }
        }
    }

    /**
     * Constructs the engine and loads all configured method summaries
     */
    public TaintDataflowEngine() {
        for (String path : METHODS_SUMMARIES_FILENAMES) {
            loadMethodSummaries(METHODS_SUMMARIES_PATH.concat(path), true);
        }
        for (String path : SAFE_ENCODERS_FILENAMES) {
            loadMethodSummaries(SAFE_ENCODERS_PATH.concat(path), true);
        }

        // Override the sensitive data taints
        loadMethodSummaries(METHODS_SUMMARIES_PATH.concat("taint-sensitive-data.txt"), false);

        if (CONFIG.isTaintedSystemVariables()) {
            loadMethodSummaries(METHODS_SUMMARIES_PATH.concat("tainted-system-variables.txt"), false);
            LOGGER.info("System variables are considered to be tainted");
        }
        String customConfigFile = CONFIG.getCustomConfigFile();
        if (customConfigFile != null && !customConfigFile.isEmpty()) {
            for (String configFile : customConfigFile.split(File.pathSeparator)) {
                addCustomSummaries(configFile);
            }
        }
        if (!CONFIG.isTaintedMainArgument()) {
            LOGGER.info("The argument of the main method is not considered tainted");
        }
    }
    
    private void loadMethodSummaries(String path, boolean checkRewrite) {
        assert path != null && !path.isEmpty();
        InputStream stream = null;
        try {
            stream = getClass().getClassLoader().getResourceAsStream(path);
            methodSummaries.load(stream, checkRewrite);
        } catch (IOException ex) {
            assert false : ex.getMessage();
        } finally {
            IO.close(stream);
        }
    }
    
    private void addCustomSummaries(String path) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
            methodSummaries.load(stream, false);
            LOGGER.log(Level.INFO, "Custom taint config loaded from {0}", path);
        } catch (IOException ex) {
            AnalysisContext.logError("cannot load custom taint config method summaries", ex);
        } finally {
            IO.close(stream);
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
        if (CONFIG.isDebugOutputSummaries() && writer != null) {
            TaintMethodSummary derivedSummary = methodSummaries.get(getSlashedMethodName(methodGen));
            if (derivedSummary != null) {
                try {
                    writer.append(getSlashedMethodName(methodGen) + ":" + derivedSummary + "\n");
                    writer.flush();
                } catch (IOException ex) {
                    AnalysisContext.logError("cannot write derived summaries", ex);
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
