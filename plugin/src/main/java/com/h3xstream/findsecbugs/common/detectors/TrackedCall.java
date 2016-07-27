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
package com.h3xstream.findsecbugs.common.detectors;

public class TrackedCall {
    private String invokeInstruction;
    public String getInvokeInstruction() { return invokeInstruction; }
    public void setInvokeInstruction(String invokeInstruction) { this.invokeInstruction = invokeInstruction; }

    private String bugType;
    public String getBugType() { return bugType; }
    public void setBugType(String bugType) { this.bugType = bugType; }

    private Integer checkedParamValue;
    public Integer getCheckedParamValue() { return checkedParamValue; }
    public void setCheckedParamValue(Integer checkedParamValue) { this.checkedParamValue = checkedParamValue; }

    public TrackedCall(String invokeInstruction, String bugType, Integer checkedParamValue) {
        this.invokeInstruction = invokeInstruction;
        this.bugType = bugType;

        this.checkedParamValue = checkedParamValue;
    }
}
