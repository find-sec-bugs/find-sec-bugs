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
package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.SystemProperties;

/**
 * This class contains some flag that can be used to create global configuration.
 * This could be use to have alternative behavior if the command line client is used versus the usual FindBugs
 * environment.
 */
public class FindSecBugsGlobalConfig {

    private static final FindSecBugsGlobalConfig instance = new FindSecBugsGlobalConfig();

    private String findSecBugsVersion = "1.4.6";
    private boolean debugPrintInstructionVisited = false;
    private boolean debugPrintInvocationVisited = false;
    private boolean debugTaintState = false;
    
    // set through SystemProperties
    private boolean debugOutputSummaries;
    private boolean taintedSystemVariables;
    private String customConfigFile;
    private boolean taintedMainArgument;
    private boolean reportPotentialXssWrongContext;

    protected FindSecBugsGlobalConfig() {
        debugOutputSummaries = Boolean.parseBoolean(loadFromSystem("findsecbugs.taint.outputsummaries", Boolean.FALSE.toString()));
        taintedSystemVariables = Boolean.parseBoolean(loadFromSystem("findsecbugs.taint.taintedsystemvariables", Boolean.FALSE.toString()));
        customConfigFile = loadFromSystem("findsecbugs.taint.customconfigfile", null);
        taintedMainArgument = Boolean.parseBoolean(loadFromSystem("findsecbugs.taint.taintedmainargument", Boolean.TRUE.toString()));
        reportPotentialXssWrongContext = Boolean.parseBoolean(loadFromSystem("findsecbugs.taint.reportpotentialxsswrongcontext", Boolean.FALSE.toString()));
    }

    public static String loadFromSystem(String key, String defaultValue) {
        String value = System.getenv(key);
        value = SystemProperties.getProperty(key, value);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public String getCustomConfigFile(String suffix) {
        return loadFromSystem("findsecbugs.injection.customconfigfile." + suffix, null);
    }

    public static FindSecBugsGlobalConfig getInstance() {
        return instance;
    }

    /*** Getters and setters only **/

    public String getFindSecBugsVersion() {
        return findSecBugsVersion;
    }

    public void setFindSecBugsVersion(String findSecBugsVersion) {
        this.findSecBugsVersion = findSecBugsVersion;
    }

    public boolean isDebugOutputSummaries() {
        return debugOutputSummaries;
    }

    public void setDebugOutputSummaries(boolean debugOutputSummaries) {
        this.debugOutputSummaries = debugOutputSummaries;
    }

    public boolean isDebugPrintInstructionVisited() {
        return debugPrintInstructionVisited;
    }

    public void setDebugPrintInstructionVisited(boolean debugPrintInstructionVisited) {
        this.debugPrintInstructionVisited = debugPrintInstructionVisited;
    }

    public boolean isDebugPrintInvocationVisited() {
        return debugPrintInvocationVisited;
    }

    public void setDebugPrintInvocationVisited(boolean debugPrintInvocationVisited) {
        this.debugPrintInvocationVisited = debugPrintInvocationVisited;
    }

    public boolean isDebugTaintState() {
        return debugTaintState;
    }

    public void setDebugTaintState(boolean debugTaintState) {
        this.debugTaintState = debugTaintState;
    }

    public boolean isTaintedSystemVariables() {
        return taintedSystemVariables;
    }

    public void setTaintedSystemVariables(boolean taintedSystemVariables) {
        this.taintedSystemVariables = taintedSystemVariables;
    }

    public String getCustomConfigFile() {
        return customConfigFile;
    }

    public void setCustomConfigFile(String customConfigFile) {
        this.customConfigFile = customConfigFile;
    }

    public boolean isTaintedMainArgument() {
        return taintedMainArgument;
    }

    public void setTaintedMainArgument(boolean taintedMainArguments) {
        this.taintedMainArgument = taintedMainArguments;
    }

    public boolean isReportPotentialXssWrongContext() {
        return reportPotentialXssWrongContext;
    }

    public void setReportPotentialXssWrongContext(boolean reportPotentialXssWrongContext) {
        this.reportPotentialXssWrongContext = reportPotentialXssWrongContext;
    }
}
