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
import com.h3xstream.findsecbugs.taintanalysis.data.UnknownSource;
import com.h3xstream.findsecbugs.taintanalysis.data.UnknownSourceType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class TaintFrameTest {

    @BeforeClass
    public void setUp() {
        //Make sure their are not side effect when print the stack frames
        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(false);
    }

    @Test
    public void validateSimpleTaintFrame() {
        TaintFrame frame = new TaintFrame(0);
        frame.pushValue(new Taint(Taint.State.TAINTED));
        frame.pushValue(new Taint(Taint.State.UNKNOWN));
        frame.pushValue(new Taint(Taint.State.NULL));
        frame.pushValue(new Taint(Taint.State.SAFE));

        String debugOutput = frame.toString();
        System.out.println(debugOutput);
        assertTrue(debugOutput.contains("0. SAFE {S}"));
        assertTrue(debugOutput.contains("1. NULL {N}"));
        assertTrue(debugOutput.contains("2. UNKNOWN {U}"));
        assertTrue(debugOutput.contains("3. TAINTED {T}"));
    }

    @Test
    public void validateSimpleTaintFrameWithLocal() {

        TaintFrame frame = new TaintFrame(4);
        frame.setValue(0, new Taint(Taint.State.TAINTED));
        frame.setValue(1, new Taint(Taint.State.TAINTED));
        frame.setValue(2, new Taint(Taint.State.UNKNOWN));
        frame.setValue(3, new Taint(Taint.State.UNKNOWN));
        frame.pushValue(new Taint(Taint.State.TAINTED));
        frame.pushValue(new Taint(Taint.State.UNKNOWN));
        frame.pushValue(new Taint(Taint.State.NULL));
        frame.pushValue(new Taint(Taint.State.SAFE));

        String[] variables = new String[] {"safe","null_value","external","modifyMe"};
        String debugOutput = frame.toString(variables);
        System.out.println(debugOutput);
        assertTrue(debugOutput.contains("0. SAFE {S}"));
        assertTrue(debugOutput.contains("1. NULL {N}"));
        assertTrue(debugOutput.contains("2. UNKNOWN {U}"));
        assertTrue(debugOutput.contains("3. TAINTED {T}"));

        for(String variable : variables) {
            assertTrue(debugOutput.contains("| "+variable));
        }
    }


    @Test
    public void validateSimpleTaintFrameWithLocalUnSet() {

        TaintFrame frame = new TaintFrame(4);
        frame.setValue(0, new Taint(Taint.State.TAINTED));
        frame.setValue(1, new Taint(Taint.State.TAINTED));
        //Not all slot are set
        frame.pushValue(new Taint(Taint.State.TAINTED));
        frame.pushValue(new Taint(Taint.State.UNKNOWN));
        frame.pushValue(new Taint(Taint.State.NULL));
        frame.pushValue(new Taint(Taint.State.SAFE));

        String[] variables = new String[] {"safe","null_value","external","modifyMe"};
        String debugOutput = frame.toString(variables);
        System.out.println(debugOutput);
        assertTrue(debugOutput.contains("0. SAFE {S}"));
        assertTrue(debugOutput.contains("1. NULL {N}"));
        assertTrue(debugOutput.contains("2. UNKNOWN {U}"));
        assertTrue(debugOutput.contains("3. TAINTED {T}"));

        for(String variable : variables) {
            assertTrue(debugOutput.contains("| "+variable));
        }
    }

    @Test
    public void validateSimpleTaintFrameWithOptionalAndConstant() {
        Taint unknown = new Taint(Taint.State.UNKNOWN);
        unknown.setPotentialValue("12345678");
        unknown.addTag(Taint.Tag.PASSWORD_VARIABLE);

        Taint constant = new Taint(Taint.State.SAFE);
        constant.setConstantValue("H@rdC0deStr1ng");

        TaintFrame frame = new TaintFrame(0);
        frame.pushValue(new Taint(Taint.State.TAINTED));
        frame.pushValue(unknown);
        frame.pushValue(new Taint(Taint.State.NULL));
        frame.pushValue(constant);

        String debugOutput = frame.toString();
        System.out.println(debugOutput);
        assertTrue(debugOutput.contains("0. SAFE {S"));
        assertTrue(debugOutput.contains("1. NULL {N}"));
        assertTrue(debugOutput.contains("2. UNKNOWN {U"));
        assertTrue(debugOutput.contains("3. TAINTED {T}"));
        assertTrue(debugOutput.contains("12345678"));
        assertTrue(debugOutput.contains("PASSWORD_VARIABLE"));
        assertTrue(debugOutput.contains("H@rdC0deStr1ng"));
    }

    @Test
    public void validateSimpleTaintFrameWithUnknownSources() {
        Taint unknown = new Taint(Taint.State.UNKNOWN);
        unknown.addSource(new UnknownSource(UnknownSourceType.FIELD, Taint.State.TAINTED).setSignatureField("taintedField"));
        unknown.addSource(new UnknownSource(UnknownSourceType.RETURN, Taint.State.TAINTED).setSignatureMethod("returnFrom"));
        unknown.addSource(new UnknownSource(UnknownSourceType.PARAMETER, Taint.State.TAINTED).setParameterIndex(2));

        TaintFrame frame = new TaintFrame(0);
        frame.pushValue(unknown);

        String debugOutput = frame.toString();
        System.out.println(debugOutput);
        assertTrue(debugOutput.contains("field[taintedField]"));
        assertTrue(debugOutput.contains("method[returnFrom]"));
        assertTrue(debugOutput.contains("parameter[2]"));
    }


}
