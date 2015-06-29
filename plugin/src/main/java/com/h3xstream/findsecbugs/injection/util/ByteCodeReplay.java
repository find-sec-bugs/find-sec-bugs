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
package com.h3xstream.findsecbugs.injection.util;

import com.h3xstream.findsecbugs.common.ByteCode;
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ConstantPushInstruction;
import org.apache.bcel.generic.ConversionInstruction;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.POP;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.PUTSTATIC;
import org.apache.bcel.generic.StoreInstruction;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import org.apache.bcel.generic.Type;

public class ByteCodeReplay {

    private static final boolean DEBUG = false;

    private Map<Integer,VariableState> localVariables = new HashMap<Integer,VariableState>();
    private Stack<VariableState> stackValues = new Stack<VariableState>();

    private ConstantPoolGen cpg;

    public ByteCodeReplay(ConstantPoolGen cpg){
        this.cpg = cpg;
    }


    public void exec(InstructionHandle instructionHandle) {
        Instruction ins = instructionHandle.getInstruction();

        if(DEBUG) {
            System.out.print("[+]");
            ByteCode.printOpCode(instructionHandle, cpg);
        }

        if (ins instanceof InvokeInstruction) {
            InvokeInstruction i = (InvokeInstruction) ins;
            Type[] types = i.getArgumentTypes(cpg);


            boolean transferTainteness =
                    (ins instanceof INVOKEVIRTUAL && "java.lang.StringBuilder".equals(i.getClassName(cpg)) &&
                    "append".equals(i.getMethodName(cpg)) &&
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;".equals(i.getSignature(cpg)))
                    ||
                    (ins instanceof INVOKESPECIAL && //
                            "java.lang.StringBuilder".equals(i.getClassName(cpg)) &&
                            "<init>".equals(i.getMethodName(cpg)) &&
                            "(Ljava/lang/String;)V".equals(i.getSignature(cpg)));

            if(transferTainteness) {

                VariableState appendValue = stackValues.pop();
                VariableState stringBuilder = stackValues.pop();
                stringBuilder.affectTaintedValue(appendValue);
                stackValues.push(stringBuilder);
            }
            else if(ins instanceof INVOKEVIRTUAL && "java.lang.StringBuilder".equals(i.getClassName(cpg)) &&
                    "toString".equals(i.getMethodName(cpg))) {
                //Transfer the tainteness of the StringBuilder to the string return
                VariableState stringBuilder = stackValues.pop();
                VariableState newVar = VariableState.newSafeValue(i.getReturnType(cpg));
                newVar.affectTaintedValue(stringBuilder);
                stackValues.push(newVar);
            }
            else if(ins instanceof INVOKESPECIAL && //
                    "java.lang.StringBuilder".equals(i.getClassName(cpg)) &&
                    "<init>".equals(i.getMethodName(cpg)) &&
                    "()V".equals(i.getSignature(cpg))) {
                //Empty constructor means empty buffer for the moment
                stackValues.pop();
            }
            else {
                for(int a=0;a<types.length;a++) { //One pop per argument
                    Type type =types[a];

                    VariableState var = stackValues.pop();
                    if(type != Type.STRING && !(type instanceof BasicType))
                        var.setTainted(true);
                }
                if(stackValues.size() > 0)
                    stackValues.pop(); //Pop object ref

                boolean isConstructor = ins instanceof INVOKESPECIAL && "<init>".equals(i.getMethodName(cpg));
                if(!isConstructor) {
                    Type returnType = i.getReturnType(cpg);
                    stackValues.push(VariableState.newTaintedValue(returnType));
                }

            }

        } else if (ins instanceof LDC) {
            LDC i = (LDC) ins;

            String value = "LDC "+i.getValue(cpg).toString();
            stackValues.push(VariableState.newSafeValue(value,i.getType(cpg)));

        } else if (ins instanceof LDC2_W) {
            LDC2_W i = (LDC2_W) ins;

            String value = "LDC "+i.getValue(cpg).toString();
            stackValues.push(VariableState.newSafeValue(i.getType(cpg)));
        } else if (ins instanceof NEW) {
            NEW i = (NEW) ins;
            if(i.getLoadClassType(cpg).getClassName().equals("java.lang.StringBuilder")) {
                stackValues.push(VariableState.newSafeValue(i.getLoadClassType(cpg)));
            }
            else {
                stackValues.push(VariableState.newTaintedValue(i.getLoadClassType(cpg)));
            }

        } else if (ins instanceof DUP) {
            VariableState var = stackValues.pop();
            stackValues.push(var);
            stackValues.push(var);
        }

        //////Local variable

        else if (ins instanceof LoadInstruction) {
            LoadInstruction i = (LoadInstruction) ins;

            VariableState var = localVariables.get(i.getIndex());
            if(var == null) var = VariableState.newTaintedValue(i.getType(cpg));
            stackValues.push(var);

        } else if (ins instanceof StoreInstruction) {
            StoreInstruction i = (StoreInstruction) ins;
            VariableState var = stackValues.pop();
            localVariables.put(i.getIndex(),var);
        }

        //////Instance fields

        else if (ins instanceof GETFIELD) {
            GETFIELD i = (GETFIELD) ins;
            stackValues.push(VariableState.newTaintedValue(i.getType(cpg)));

        } else if (ins instanceof PUTFIELD) {
            PUTFIELD i = (PUTFIELD) ins;
            stackValues.pop();

        }

        //////Static fields

        else if (ins instanceof GETSTATIC) {
            GETSTATIC i = (GETSTATIC) ins;

            stackValues.push(VariableState.newSafeValue(i.getType(cpg)));

        } else if (ins instanceof PUTSTATIC) {
            PUTSTATIC i = (PUTSTATIC) ins;
            stackValues.pop();

        }

        //Others

        else if (ins instanceof ConstantPushInstruction) {
            ConstantPushInstruction i = (ConstantPushInstruction) ins;
            stackValues.push(VariableState.newSafeValue(i.getType(cpg)));
        }

        //Array operations
        else if (ins instanceof ANEWARRAY) {
            ANEWARRAY i = (ANEWARRAY) ins;
            stackValues.pop();
            stackValues.push(VariableState.newSafeValue("array", i.getType(cpg)));
        }

        else if (ins instanceof ARRAYLENGTH) {
            ARRAYLENGTH i = (ARRAYLENGTH) ins;
            stackValues.pop(); //Array ref
            stackValues.push(VariableState.newSafeValue(Type.INT));
        }

        else if (ins instanceof AASTORE) {
            stackValues.pop(); //Value
            stackValues.pop(); //Index to store to
            stackValues.pop(); //Array ref
        }

        else if(ins instanceof AALOAD) {
            AALOAD i = (AALOAD) ins;
            stackValues.pop(); //index
            VariableState array = stackValues.pop(); //Array

            //Inherits tainteness from the source array
            VariableState valueExtract = VariableState.newSafeValue(i.getType(cpg));
            valueExtract.affectTaintedValue(array);

            stackValues.push(valueExtract);
        }

        //Equality test
        else if (ins instanceof IfInstruction) {
            IfInstruction i = (IfInstruction) ins;
            stackValues.pop(); //Pop value test
            if(i.getClass().getName().contains("CMP")) {
                stackValues.pop();  //Pop value compare IF_ICMPLE, IF_ICMPGT, etc...
            }
        }

        else if (ins instanceof POP) {
            stackValues.pop();
        }

        else if (ins instanceof ConversionInstruction) {
            //Only type change..
        }
        else if (ins instanceof NOP) {
            //Well..
        }
        else if (ins instanceof GotoInstruction) {
            //Well..
        }
        else {
            System.out.println("/!\\ INSTRUCTION NOT SUPPORT => " + ins.getName());
        }


    }

    /**
     * Analyze the stack and validate that the stack value passed to the sensible parameters are tainted.
     *
     * @param instructionHandle
     * @param arguments
     * @return True if at least one parameter is tainted.
     */
    public boolean isTaintedStackParameter(InstructionHandle instructionHandle, int[] arguments) {
        if(DEBUG) {
            System.out.println("Tainted status at :");
            ByteCode.printOpCode(instructionHandle, cpg);
            System.out.println("For the stack indexes : " + Arrays.toString(arguments));
            displayStack();
            displayLocals();
        }
        ListIterator<VariableState> varIt = stackValues.listIterator(stackValues.size());
        int i=0;
        List<Integer> argumentsList = asList(arguments);
        while(varIt.hasPrevious()){
            VariableState var = varIt.previous();
            if(argumentsList.contains(i)) {
                if(var.isTainted()) {
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    private void displayStack() {
        System.out.println("==================================================================================");
        System.out.println("Stack :");
        int i=0;
        for(VariableState var : stackValues) {
            if(var == null) {
                System.out.println(String.format("<%02d> null", i++));
            }
            else {
                System.out.println(String.format("<%02d> %s", i++, var.toString()));
            }
        }
        System.out.println("==================================================================================");
    }
    private void displayLocals() {
        System.out.println("==================================================================================");
        System.out.println("Local variables :");
        for(Map.Entry<Integer,VariableState> var : localVariables.entrySet()) {
            System.out.println(String.format("<%02d> %s",
                    var.getKey(),
                    var.getValue().toString()));
        }
        System.out.println("==================================================================================");
    }

    public List<Integer> asList(final int[] is)
    {
        return new AbstractList<Integer>() {
            public Integer get(int i) { return is[i]; }
            public int size() { return is.length; }
        };
    }
}
