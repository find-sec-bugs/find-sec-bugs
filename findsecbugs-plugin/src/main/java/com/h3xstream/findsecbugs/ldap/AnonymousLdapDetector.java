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
package com.h3xstream.findsecbugs.ldap;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.Instruction;
import java.util.Iterator;

public class AnonymousLdapDetector implements Detector {

    private static final String LDAP_ANONYMOUS = "LDAP_ANONYMOUS";

    private BugReporter bugReporter;

    public AnonymousLdapDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }
    
    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {

            try {
                analyzeMethod(m, classContext);
            } catch (CFGBuilderException e) {
            } catch (DataflowAnalysisException e) {
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);
        
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();

            Instruction inst = location.getHandle().getInstruction();
            
            if (inst instanceof LDC) {
                LDC ldc = (LDC) inst;
                if("java.naming.security.authentication".equals(ldc.getValue(cpg)) &&
                   "none".equals(ByteCode.getConstantLDC(location.getHandle().getNext(), cpg, String.class))){
                    JavaClass clz = classContext.getJavaClass();
                    bugReporter.reportBug(new BugInstance(this, LDAP_ANONYMOUS, Priorities.LOW_PRIORITY) //
                    .addClass(clz)
                    .addMethod(clz, m)
                    .addSourceLine(classContext, m, location));
                    break;
                }
            }            
        }
    }

    @Override
    public void report() {
    }
}
