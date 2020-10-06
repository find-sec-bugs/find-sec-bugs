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
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;

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

                    JavaClass clz = classContext.getJavaClass();
                    bugReporter.reportBug(new BugInstance(this, STATIC_IV, Priorities.NORMAL_PRIORITY) //
                            .addClass(clz)
                            .addMethod(clz,m)
                            .addSourceLine(classContext,m,location));
                }
            }
        }
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
