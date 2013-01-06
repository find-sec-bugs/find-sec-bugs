package com.h3xstream.findsecbugs.common;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;
import org.mockito.Matchers;
import org.testng.annotations.Test;

import java.io.PrintStream;

import static org.mockito.Mockito.*;

public class ByteCodeTest {
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
            ByteCode.printOpCode(ins,cpg);

            verify(sysOut,atLeastOnce()).println(contains("ClassTest.method"));
        }
        finally {
            System.setOut(oldPrintStream);
            System.out.println("Sysout is back!");
        }

    }
}
