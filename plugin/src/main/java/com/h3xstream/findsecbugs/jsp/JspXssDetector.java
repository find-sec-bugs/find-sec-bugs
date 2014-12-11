/**
 * Find Security Bugs
 * Copyright (c) 2014, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.*;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.util.*;

/**
 * Basic rule that attempts to find additional XSS that the built-in FB rule didn't find.
 */
public class JspXssDetector implements Detector {

    private static final String XSS_JSP_PRINT = "XSS_JSP_PRINT";
    private static final String XSS_SERVLET = "XSS_SERVLET";
    private static final boolean DEBUG = false;

    private BugReporter bugReporter;

    public JspXssDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {

            try {
                analyzeMethod(m,classContext);
            } catch (CFGBuilderException e) {
            } catch (DataflowAnalysisException e) {
            }
        }
    }


    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        Set<Integer> taintedLocals = new HashSet<Integer>();

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = nextLocation(i, cpg);
            Instruction inst = location.getHandle().getInstruction();

            if (inst instanceof InvokeInstruction) {
                InvokeInstruction invoke = (InvokeInstruction) inst;

                //Identify tainted input
                if ("javax.servlet.http.HttpServletRequest".equals(invoke.getClassName(cpg)) && (
                        "getAttribute".equals(invoke.getMethodName(cpg)) || //Attributes are values coming from the controller
                        "getParameter".equals(invoke.getMethodName(cpg)) //Get parameter (GET/POST parameters)
                        )) {

                    Location potentialStore;
                    while((potentialStore = nextLocation(i,cpg)) != null) {
                        Instruction potentialStoreIns = potentialStore.getHandle().getInstruction();
                        if(potentialStoreIns instanceof CHECKCAST) {
                            continue;
                        }
                        if(potentialStoreIns instanceof StoreInstruction) {
                            StoreInstruction taintedStore = (StoreInstruction) potentialStoreIns;
                            if(DEBUG) System.out.println("Tainted input in index "+taintedStore.getIndex());

                            taintedLocals.add(taintedStore.getIndex());
                        }
                        break; //We are only looking at the next instruction following the getAttribute
                    }
                }
            }
            else if(inst instanceof LoadInstruction) {

                LoadInstruction loadIns = (LoadInstruction) inst;

                //Tainted used
                if(taintedLocals.contains(loadIns.getIndex())) {

                    if(DEBUG) System.out.println("Use of the tainted local at index "+loadIns.getIndex());

                    Location locationAfterLoad = i.next();
                    Instruction locationAfterLoadIns = locationAfterLoad.getHandle().getInstruction();
                    //Potential print
                    if(locationAfterLoadIns instanceof InvokeInstruction) {
                        InvokeInstruction invoke = (InvokeInstruction) locationAfterLoadIns;

                        if ("javax.servlet.jsp.JspWriter".equals(invoke.getClassName(cpg)) &&
                                "print".equals(invoke.getMethodName(cpg))) {

                            JavaClass clz = classContext.getJavaClass();
                            bugReporter.reportBug(new BugInstance(this, XSS_JSP_PRINT, Priorities.HIGH_PRIORITY) //
                                    .addClass(clz)
                                    .addMethod(clz,m)
                                    .addSourceLine(classContext,m,locationAfterLoad));
                        }
                        else if("java.io.PrintWriter".equals(invoke.getClassName(cpg)) &&
                                "write".equals(invoke.getMethodName(cpg))) {
                            JavaClass clz = classContext.getJavaClass();
                            bugReporter.reportBug(new BugInstance(this, XSS_SERVLET, Priorities.HIGH_PRIORITY) //
                                    .addClass(clz)
                                    .addMethod(clz,m)
                                    .addSourceLine(classContext,m,locationAfterLoad));
                        }
                    }
                    //Skipping checkCast
                    else if(locationAfterLoadIns instanceof CHECKCAST) {
                        locationAfterLoadIns = i.next().getHandle().getInstruction();

                    }

                    //Tainted local is being transfer
                    if(locationAfterLoadIns instanceof StoreInstruction) {
                        StoreInstruction relocateTainted = (StoreInstruction) locationAfterLoadIns;
                        taintedLocals.add(relocateTainted.getIndex());
                        if(DEBUG) System.out.println("(relocate) Tainted input in index "+relocateTainted.getIndex());

                    }
                }

            }
            else if(inst instanceof StoreInstruction) {
                //Potential overwrite of tainted variable
                StoreInstruction storeIns = (StoreInstruction) inst;
                int storeDestination = storeIns.getIndex();
                if(taintedLocals.contains(storeDestination)) {
                    taintedLocals.remove(storeDestination);
                }
            }
        }
    }

    private Location nextLocation(Iterator<Location> i,ConstantPoolGen cpg) {
        Location loc = i.next();
        if(DEBUG) ByteCode.printOpCode(loc.getHandle().getInstruction(), cpg);
        return loc;
    }


    @Override
    public void report() {
    }

}
