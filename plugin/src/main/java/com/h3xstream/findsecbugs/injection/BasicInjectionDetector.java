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
package com.h3xstream.findsecbugs.injection;

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.SystemProperties;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.io.IO;
import edu.umd.cs.findbugs.util.ClassName;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * Detector designed for extension to detect basic injections with a list of
 * full method names with specified injectable arguments as taint sinks
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public abstract class BasicInjectionDetector extends AbstractInjectionDetector {

    private final Map<String, InjectionPoint> injectionMap = new HashMap<String, InjectionPoint>();
    private static final SinksLoader SINKS_LOADER = new SinksLoader();

    protected BasicInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);

        loadCustomConfigFiles();
    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
            InstructionHandle handle) {
        assert invoke != null && cpg != null;
        InjectionPoint injectionPoint = injectionMap.get(getFullMethodName(invoke, cpg));
        if (injectionPoint == null) {
            return InjectionPoint.NONE;
        }
        return injectionPoint;
    }

    protected void loadConfiguredSinks(InputStream stream, String bugType) throws IOException {
        SINKS_LOADER.loadSinks(stream, bugType, new SinksLoader.InjectionPointReceiver() {
            @Override
            public void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
                addParsedInjectionPoint(fullMethodName, injectionPoint);
            }
        });
    }

    /**
     * Loads taint sinks from configuration
     * 
     * @param filename name of the configuration file
     * @param bugType type of an injection bug
     */
    protected void loadConfiguredSinks(String filename, String bugType) {
        SINKS_LOADER.loadConfiguredSinks(filename, bugType, new SinksLoader.InjectionPointReceiver() {
            @Override
            public void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
                addParsedInjectionPoint(fullMethodName, injectionPoint);
            }
        });
    }

    /**
     * Loads taint sinks from custom file. The file name is passed using system property based on the current class name.<br />
     * <br />
     * Example for Linux/Mac OS X:<ul>
     *     <li>-Dfindsecbugs.injection.customconfigfile.SqlInjectionDetector="/tmp/sql-custom.txt|SQL_INJECTION_HIBERNATE:/tmp/sql2-custom.txt|SQL_INJECTION_HIBERNATE"</li>
     *     <li>-Dfindsecbugs.injection.customconfigfile.ScriptInjectionDetector="/tmp/script-engine-custom.txt|SCRIPT_ENGINE_INJECTION:/tmp/el-custom.txt|EL_INJECTION"</li>
     * </ul>
     * Example for Windows:<ul>
     *     <li>-Dfindsecbugs.injection.customconfigfile.SqlInjectionDetector="C:\Temp\sql-custom.txt|SQL_INJECTION_HIBERNATE;C:\Temp\sql2-custom.txt|SQL_INJECTION_HIBERNATE"</li>
     *     <li>-Dfindsecbugs.injection.customconfigfile.ScriptInjectionDetector="C:\Temp\script-engine-custom.txt|SCRIPT_ENGINE_INJECTION;C:\Temp\el-custom.txt|EL_INJECTION"</li>
     * </ul>
     */
    protected void loadCustomConfigFiles() {
        String customConfigFile = FindSecBugsGlobalConfig.loadFromSystem("findsecbugs.injection.customconfigfile." + getClass().getSimpleName(), null);
        if (customConfigFile != null && !customConfigFile.isEmpty()) {
            for (String configFile : customConfigFile.split(File.pathSeparator)) {
                String[] injectionDefinition = configFile.split(Pattern.quote("|"));

                if (injectionDefinition.length != 2 ||
                    injectionDefinition[0].trim().isEmpty() ||
                    injectionDefinition[1].trim().isEmpty()) {

                    AnalysisContext.logError("Wrong injection config file definition: " + configFile + ". Syntax: fileName|bugType, example: sql-custom.txt|SQL_INJECTION_HIBERNATE");

                    continue;
                }

                loadCustomSinks(injectionDefinition[0], injectionDefinition[1]);
            }
        }
    }

    /**
     * Loads taint sinks configuration file from file system. If the file doesn't exist on file system, loads the file from classpath.
     *
     * @param fileName name of the configuration file
     * @param bugType type of an injection bug
     */
    protected void loadCustomSinks(String fileName, String bugType) {
        InputStream stream = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                stream = new FileInputStream(file);
                loadConfiguredSinks(stream, bugType);
            }
            else {
                stream = getClass().getClassLoader().getResourceAsStream(fileName);
                loadConfiguredSinks(stream, bugType);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Cannot load custom injection sinks from " + fileName, ex);
        } finally {
            IO.close(stream);
        }
    }

    /**
     * Loads a single taint sink (like a line of configuration)
     * 
     * @param line specification of the sink
     * @param bugType type of an injection bug
     */
    protected void loadSink(String line, String bugType) {
        SINKS_LOADER.loadSink(line, bugType, new SinksLoader.InjectionPointReceiver() {
            @Override
            public void receiveInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
                addParsedInjectionPoint(fullMethodName, injectionPoint);
            }
        });
    }

    protected void addParsedInjectionPoint(String fullMethodName, InjectionPoint injectionPoint) {
        assert !injectionMap.containsKey(fullMethodName);
        injectionMap.put(fullMethodName, injectionPoint);
    }
    
    private String getFullMethodName(InvokeInstruction invoke, ConstantPoolGen cpg) {
        return ClassName.toSlashedClassName(invoke.getReferenceType(cpg).toString())
                + "." + invoke.getMethodName(cpg) + invoke.getSignature(cpg);
    }
}
