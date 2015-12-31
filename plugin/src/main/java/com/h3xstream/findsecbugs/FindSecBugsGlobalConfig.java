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

    private static FindSecBugsGlobalConfig instance = null;

    private boolean printCustomInjectionWarning = true;
    private String findSecBugsVersion = "1.4.5";
    private boolean debugOutputSummaries = false;
    private boolean debugPrintInstructionVisited = false;


    private boolean debugPrintInvocationVisited = false;
    private boolean debugTaintState = false;

    /** Singleton code */

    protected FindSecBugsGlobalConfig() {
    }
    public static FindSecBugsGlobalConfig getInstance() {
        if(instance == null) {
            instance = new FindSecBugsGlobalConfig();
            instance.debugOutputSummaries = SystemProperties.getBoolean("findsecbugs.taint.outputsummaries",false);
        }
        return instance;
    }

    /*** Getter and setter **/

    /**
     * @return If the message regarding missing custom injection signatures should be printed.
     */
    public boolean isPrintCustomInjectionWarning() {
        return printCustomInjectionWarning;
    }

    public void setPrintCustomInjectionWarning(boolean printCustomInjectionWarning) {
        this.printCustomInjectionWarning = printCustomInjectionWarning;
    }

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
}
