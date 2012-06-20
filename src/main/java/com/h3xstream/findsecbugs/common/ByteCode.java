package com.h3xstream.findsecbugs.common;

import org.apache.bcel.generic.*;

public class ByteCode {

    /**
     * Print the the detail of the given instruction (class, method, etc.)
     * @param ins Instruction
     * @param cpg Constant Pool
     */
    public static void printOpCode(InvokeInstruction ins, ConstantPoolGen cpg) {
        System.out.println(ins.getClass().getSimpleName() + " " + ins.getClassName(cpg).replaceAll("\\.","/") + "." + ins.getMethodName(cpg) + " (" + ins.getSignature(cpg) + ")");
    }
}
