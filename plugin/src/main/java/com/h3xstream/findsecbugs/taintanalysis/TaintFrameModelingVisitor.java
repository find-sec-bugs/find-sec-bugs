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

import edu.umd.cs.findbugs.ba.AbstractFrameModelingVisitor;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.InvalidBytecodeException;
import edu.umd.cs.findbugs.util.ClassName;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.bcel.Constants;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StoreInstruction;

/**
 * Visitor to make instruction transfer of taint values easier
 *
 * @author David Formanek
 */
public class TaintFrameModelingVisitor extends AbstractFrameModelingVisitor<Taint, TaintFrame> {

    private static final String CONFIG_DIR = "taint-config";
    private static final String TRANSFER_METHODS_FILENAME = "transfer-methods.txt";
    
    private static final String TO_STRING_METHOD = "toString()Ljava/lang/String;";
    private static final Collection<Integer> EMPTY_PARAMS = Collections.emptyList();
    private static final Collection<Integer> PARAM_0;
    private final Map<String, Collection<Integer>> transferMethods
            = new HashMap<String, Collection<Integer>>();
    
    static {
        Collection<Integer> param0 = new ArrayList<Integer>(1);
        param0.add(0);
        PARAM_0 = Collections.unmodifiableCollection(param0);
    }
    
    public TaintFrameModelingVisitor(ConstantPoolGen cpg) {
        super(cpg);
        try {
            loadMap(TRANSFER_METHODS_FILENAME, transferMethods, "#");
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        }
    }

    @Override
    public Taint getDefaultValue() {
        return new Taint(Taint.State.UNKNOWN);
    }

    @Override
    public void visitLDC(LDC obj) {
        pushSafe();
    }

    @Override
    public void visitLDC2_W(LDC2_W obj) {
        // double and long type takes two slots in BCEL
        pushSafe();
        pushSafe();
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        getFrame().pushValue(new Taint(Taint.State.NULL));
    }
    
    @Override
    public void visitNEW(NEW obj) {
        pushSafe();
    }
    
    /*@Override
    public void handleStoreInstruction(StoreInstruction obj) {
        try {
            int numConsumed = obj.consumeStack(cpg);
            if (numConsumed == Constants.UNPREDICTABLE) {
                throw new InvalidBytecodeException("Unpredictable stack consumption");
            }
            int index = obj.getIndex();
            while (numConsumed-- > 0) {
                Taint value = getFrame().popValue();
                value.invalidateLocalVariableIndex();
                getFrame().setValue(index++, value);
            }
        } catch (DataflowAnalysisException e) {
            throw new InvalidBytecodeException(e.toString());
        }
    }*/
    
    @Override
    public void handleLoadInstruction(LoadInstruction obj) {
        int numProduced = obj.produceStack(cpg);
        if (numProduced == Constants.UNPREDICTABLE) {
            throw new InvalidBytecodeException("Unpredictable stack production");
        }
        int index = obj.getIndex() + numProduced;
        while (numProduced-- > 0) {
            Taint value = getFrame().getValue(--index);
            value.setLocalVariableIndex(index);
            getFrame().pushValue(value);
        }
    }
    
    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE obj) {
        visitInvoke(obj);
    }
    
    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL obj) {
        visitInvoke(obj);
    }
    
    @Override
    public void visitINVOKESTATIC(INVOKESTATIC obj) {
        visitInvoke(obj);
    }
    
    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj) {
        visitInvoke(obj);
    }

    private void visitInvoke(InvokeInstruction obj) {
        String className = obj.getReferenceType(cpg).toString();
        String methodNameWithSig = obj.getMethodName(cpg) + obj.getSignature(cpg);
        String fullMethodName = ClassName.toSlashedClassName(className) + "." + methodNameWithSig;
        Collection<Integer> transferParameters;
        if (TO_STRING_METHOD.equals(methodNameWithSig)) {
            transferParameters = PARAM_0;
        } else {
            transferParameters = transferMethods.getOrDefault(fullMethodName, EMPTY_PARAMS);
        }
        Taint taint = getMethodTaint(transferParameters);
        
        if (fullMethodName.equals("java/lang/StringBuilder.append(Ljava/lang/String;)Ljava/lang/StringBuilder;")) {
            int mutableStackPosition = 1; // StringBuilder
            try {
                Taint stackValue = getFrame().getStackValue(mutableStackPosition);
                if (stackValue.hasValidLocalVariableIndex()) {
                    int index = stackValue.getLocalVariableIndex();
                    getFrame().setValue(index, taint);
                }
                // else we are not able to transfer taint
            } catch (DataflowAnalysisException ex) {
                throw new RuntimeException("Bad mutable stack position specification", ex);
            }
        }
        
        modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), taint);
        if (fullMethodName.contains("<init>")
                && transferMethods.containsKey(fullMethodName)
                && getFrame().getStackDepth() != 0) {
            try {
                Taint popValue = getFrame().popValue();
                getFrame().pushValue(Taint.merge(popValue, taint));
            } catch (DataflowAnalysisException ex) {
                assert false;
            }
        }
    }

    private Taint getMethodTaint(Collection<Integer> transferParameters) {
        Taint taint = null;
        for (Integer transferParameter : transferParameters) {
            try {
                Taint value = getFrame().getStackValue(transferParameter);
                taint = (taint == null) ? value : Taint.merge(taint, value);
            } catch (DataflowAnalysisException ex) {
                throw new RuntimeException("Bad transfer parameter specification", ex);
            }
        }
        if (taint == null) {
            taint = getDefaultValue();
        }
        return taint;
    }

    private void pushSafe() {
        getFrame().pushValue(new Taint(Taint.State.SAFE));
    }
    
    private void loadMap(String filename, Map<String, Collection<Integer>> map,
            String separator) throws IOException {
        BufferedReader reader = null;
        try {
            reader = getReader(filename);
            for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] tuple = line.split(separator);
                int count = tuple.length - 1;
                Collection<Integer> parameters = new ArrayList<Integer>(count);
                for (int i = 0; i < count; i++) {
                    parameters.add(Integer.parseInt(tuple[i + 1]));
                }
                map.put(tuple[0], parameters);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private BufferedReader getReader(String filename) {
        String path = CONFIG_DIR + "/" + filename;
        return new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(path)
        ));
    }
}
