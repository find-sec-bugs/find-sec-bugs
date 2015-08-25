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
package com.h3xstream.findsecbugs.common;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;
import org.mockito.Matchers;
import org.testng.annotations.Test;

import java.io.PrintStream;

import static org.mockito.Mockito.*;

public class ByteCodeTest {

    /**
     * For test coverage mostly .. not that useful.
     */
    @Test
    public void probeByteCodeDebug() {
        PrintStream sysOut = mock(PrintStream.class);

        System.out.println("Sysout hijack!");
        PrintStream oldPrintStream = System.out;

        try {
            System.setOut(sysOut);

            InvokeInstruction ins = mock(InvokeInstruction.class);
            ConstantPoolGen cpg = mock(ConstantPoolGen.class);

            when(ins.getClassName(Matchers.<ConstantPoolGen>any())).thenReturn("ClassTest");
            when(ins.getMethodName(Matchers.<ConstantPoolGen>any())).thenReturn("method");
            when(ins.getSignature(Matchers.<ConstantPoolGen>any())).thenReturn("(Lsignature)Lblah");

            //Print invocation
            ByteCode.printOpCode(ins, cpg);

            verify(sysOut, atLeastOnce()).println(contains("ClassTest.method"));
        } finally {
            System.setOut(oldPrintStream);
            System.out.println("Sysout is back!");
        }

    }
}
