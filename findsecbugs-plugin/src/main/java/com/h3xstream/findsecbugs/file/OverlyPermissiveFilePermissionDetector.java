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
package com.h3xstream.findsecbugs.file;

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
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LDC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OverlyPermissiveFilePermissionDetector implements Detector {

    private static final boolean DEBUG = false;
    public static final String OVERLY_PERMISSIVE_FILE_PERMISSION = "OVERLY_PERMISSIVE_FILE_PERMISSION";
    private final BugReporter bugReporter;

    public OverlyPermissiveFilePermissionDetector(BugReporter bugReporter) {
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

    @Override
    public void report() {

    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        boolean addIntToSet = false;
        boolean callSetPermissions = false;
        List<Location> locationFound = new ArrayList<Location>();

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = nextLocation(i, cpg);
            Instruction inst = location.getHandle().getInstruction();
//            ByteCode.printOpCode(inst,cpg);

            if (inst instanceof GETSTATIC) {
                GETSTATIC getstatic = (GETSTATIC) inst;
                String fieldName = getstatic.getName(cpg);
//                System.out.println("Field:"+fieldName);
                if(fieldName.endsWith("OTHERS_READ") || fieldName.endsWith("OTHERS_WRITE") || fieldName.endsWith("OTHERS_EXECUTE")) {
                    addIntToSet = true;
                    locationFound.add(location);
                }
            }
            if (inst instanceof INVOKESTATIC) {
                INVOKESTATIC invoke = (INVOKESTATIC) inst;
                //invokestatic   java/nio/file/Files.setPosixFilePermissions (Ljava/nio/file/Path;Ljava/util/Set;)Ljava/nio/file/Path;
                if (("java.nio.file.Files").equals(invoke.getClassName(cpg)) && //
                        "setPosixFilePermissions".equals(invoke.getMethodName(cpg))) {
                    callSetPermissions = true;
                }


                //invokestatic    java/nio/file/attribute/PosixFilePermissions.fromString(Ljava/lang/String;)Ljava/util/Set;

                if (("java.nio.file.attribute.PosixFilePermissions").equals(invoke.getClassName(cpg)) && //
                        "fromString".equals(invoke.getMethodName(cpg))) {


                    LDC loadConst = ByteCode.getPrevInstruction(location.getHandle(), LDC.class);
                    if(loadConst != null) {
                        String permissionString = (String) loadConst.getValue(cpg);
                        if (!permissionString.endsWith("---")) {
                            JavaClass clz = classContext.getJavaClass();
                            bugReporter.reportBug(new BugInstance(this, OVERLY_PERMISSIVE_FILE_PERMISSION, Priorities.NORMAL_PRIORITY) //
                                    .addClass(clz) //
                                    .addMethod(clz, m) //
                                    .addSourceLine(classContext, m, location));
                        }
                    }
                }
            }
        }

        if(addIntToSet && callSetPermissions && locationFound != null) {
            JavaClass clz = classContext.getJavaClass();
            for(Location loc:locationFound) {
                bugReporter.reportBug(new BugInstance(this, OVERLY_PERMISSIVE_FILE_PERMISSION, Priorities.NORMAL_PRIORITY) //
                        .addClass(clz) //
                        .addMethod(clz, m) //
                        .addSourceLine(classContext, m, loc));
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

}
