package com.h3xstream.findsecbugs.common;

import org.apache.bcel.generic.*;

public class ByteCode {

    /**
     * Print the the detail of the given instruction (class, method, etc.)
     *
     * @param ins Instruction
     * @param cpg Constant Pool
     */
    public static void printOpCode(Instruction ins, ConstantPoolGen cpg) {
        if (ins instanceof InvokeInstruction) {
            InvokeInstruction invokeIns = (InvokeInstruction) ins;
            System.out.println(ins.getClass().getSimpleName() + " " + invokeIns.getClassName(cpg).replaceAll("\\.", "/") + "." + invokeIns.getMethodName(cpg) + " (" + invokeIns.getSignature(cpg) + ")");
        } else {
            System.out.println(ins.getClass().getSimpleName() + " " + ins.toString());
        }
    }

}
