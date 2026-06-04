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
package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Location;
import java.util.Iterator;
import javax.crypto.Cipher;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.Type;

/**
 * <p>
 *  The main goal of the this detector is to find encryption being done with static initialization vector (IV).
 *  By design, the IV should be change for every message encrypt by a system.
 * </p>
 * <h3>Note on the implementation</h3>
 * <p>
 *  The strategy to find those occurrences is not to backtrack to find the potential source of the bytes being passed.
 *  It will not be trigger if SecureRandom instance is use. Therefor, it is very likely to trigger false positive if the
 *  encryption is separate from the IV generation.
 * </p>
 */
public class StaticIvDetector implements Detector {

    private static final boolean DEBUG = false;
    private static final String STATIC_IV = "STATIC_IV";
    private final BugReporter bugReporter;

    public StaticIvDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        Method[] methodList = javaClass.getMethods();
        for (Method m : methodList) {
            try {
                analyzeMethod(m, classContext);
            } catch (CFGBuilderException | DataflowAnalysisException e) {
                AnalysisContext.logError("Cannot analyze method", e);
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        boolean foundSafeIvGeneration = false;
        //Detect if the method is doing decryption/unwrapping only. If it is the case, IV should not be generated from this point
        //therefore it is a false positive
        boolean atLeastOneDecryptCipher = false;
        boolean atLeastOneEncryptCipher = false;
        boolean ivFetchFromCipher = false;
        //Track whether this method performs any Cipher.init at all. A method that only builds the
        //parameter spec (a helper that receives the IV as an argument and returns the spec) has no
        //encryption context, so an IV taken from a method parameter cannot be assumed to be static.
        boolean atLeastOneCipherInit = false;

        //First pass : it look for encryption and decryption mode to detect if the method does decryption only
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = nextLocation(i, cpg);
            Instruction inst = location.getHandle().getInstruction();
            //ByteCode.printOpCode(inst,cpg);

            if (inst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;

                //INVOKEVIRTUAL javax/crypto/Cipher.init ((ILjava/security/Key;)V)
                if (("javax.crypto.Cipher").equals(invoke.getClassName(cpg)) &&
                        "init".equals(invoke.getMethodName(cpg))) {
                    atLeastOneCipherInit = true;
                    ICONST iconst = ByteCode.getPrevInstruction(location.getHandle(), ICONST.class);
                    if (iconst != null) {
                        int mode = iconst.getValue().intValue();
                        switch (mode) {
                            // Wrapping and unwrapping are equivalent to encryption and decryption.

                            case Cipher.ENCRYPT_MODE: case Cipher.WRAP_MODE:
                                atLeastOneEncryptCipher = true;
                                break;
                            case Cipher.DECRYPT_MODE: case Cipher.UNWRAP_MODE:
                                atLeastOneDecryptCipher = true;
                                break;
                            default:
                                //Unknown mode. Statement will be ignored.
                                break;
                        }
                    }
                }
                //INVOKEVIRTUAL javax/crypto/Cipher.getIV (()[B)
                else if (("javax.crypto.Cipher").equals(invoke.getClassName(cpg)) &&
                        "getIV".equals(invoke.getMethodName(cpg))) {
                    ivFetchFromCipher = true;
                }
            }
        }

        //Second pass : It look for encryption method and a potential preceding SecureRandom usage
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = nextLocation(i, cpg);
            Instruction inst = location.getHandle().getInstruction();

            if (inst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
                if ("java.security.SecureRandom".equals(invoke.getClassName(cpg)) &&
                        "nextBytes".equals(invoke.getMethodName(cpg))) {
                    foundSafeIvGeneration = true;
                }
            } else if (inst instanceof INVOKESPECIAL &&
                    !ivFetchFromCipher //IV was generate with the KeyGenerator
                    && (!atLeastOneDecryptCipher || atLeastOneEncryptCipher) //The cipher is in decrypt mode (no iv generation)
                    && !foundSafeIvGeneration) {
                INVOKESPECIAL invoke = (INVOKESPECIAL) inst;
                if (("javax.crypto.spec.IvParameterSpec").equals(invoke.getClassName(cpg)) &&
                        "<init>".equals(invoke.getMethodName(cpg))) {

                    //When the method performs no encryption/decryption of its own and the IV is
                    //received as a method parameter, there is no evidence the IV is static (see #765).
                    //A genuinely static IV is built locally from constants (NEWARRAY) or read from a
                    //field (GETSTATIC/GETFIELD), so those still get flagged.
                    if (!atLeastOneCipherInit
                            && ivLoadedFromMethodParameter(location.getHandle(), m, cpg)) {
                        continue;
                    }

                    JavaClass clz = classContext.getJavaClass();
                    bugReporter.reportBug(new BugInstance(this, STATIC_IV, Priorities.NORMAL_PRIORITY) //
                            .addClass(clz)
                            .addMethod(clz,m)
                            .addSourceLine(classContext,m,location));
                }
            }
        }
    }

    /**
     * Determine whether the IV array passed to the spec constructor at {@code initHandle} is loaded
     * from a method parameter (an {@code ALOAD} of a parameter slot), as opposed to being built
     * locally from constants or read from a field.
     *
     * <p>The scan walks backward from the constructor call to the matching {@code NEW} of the spec
     * class. If a fresh array construction ({@code NEWARRAY}/{@code ANEWARRAY}) or a field read
     * ({@code GETSTATIC}/{@code GETFIELD}) appears in that window, the IV is treated as potentially
     * static and this method returns {@code false}. If the only array reference comes from an
     * {@code ALOAD} of a parameter slot, the IV is parameter-derived and the method returns
     * {@code true}.</p>
     */
    private boolean ivLoadedFromMethodParameter(InstructionHandle initHandle, Method m, ConstantPoolGen cpg) {
        int parameterSlots = parameterSlotCount(m);
        boolean parameterArrayLoaded = false;
        for (InstructionHandle h = initHandle.getPrev(); h != null; h = h.getPrev()) {
            Instruction ins = h.getInstruction();
            if (ins instanceof NEW) {
                NEW newIns = (NEW) ins;
                String createdClass = newIns.getLoadClassType(cpg).getClassName();
                if ("javax.crypto.spec.IvParameterSpec".equals(createdClass)
                        || "javax.crypto.spec.GCMParameterSpec".equals(createdClass)) {
                    //Reached the start of the spec construction.
                    break;
                }
            } else if (ins instanceof NEWARRAY || ins instanceof ANEWARRAY) {
                //The array is built locally; it may be a hardcoded constant IV.
                return false;
            } else if (ins instanceof GETSTATIC || ins instanceof GETFIELD) {
                //The array comes from a field; treat it as potentially static.
                return false;
            } else if (ins instanceof ALOAD) {
                int slot = ((ALOAD) ins).getIndex();
                if (slot < parameterSlots) {
                    parameterArrayLoaded = true;
                }
            }
        }
        return parameterArrayLoaded;
    }

    /**
     * Number of local-variable slots occupied by the method parameters (including {@code this} for
     * instance methods). {@code long} and {@code double} parameters each take two slots.
     */
    private int parameterSlotCount(Method m) {
        int slots = m.isStatic() ? 0 : 1;
        for (Type t : m.getArgumentTypes()) {
            slots += t.getSize();
        }
        return slots;
    }

    private Location nextLocation(Iterator<Location> i,ConstantPoolGen cpg) {
        Location loc = i.next();
        if(DEBUG) {
            ByteCode.printOpCode(loc.getHandle().getInstruction(), cpg);
        }
        return loc;
    }

    @Override
    public void report() {
    }
}
