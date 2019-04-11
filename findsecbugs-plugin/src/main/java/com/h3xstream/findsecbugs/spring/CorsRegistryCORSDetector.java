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
package com.h3xstream.findsecbugs.spring;

import android.util.Log;
import com.h3xstream.findsecbugs.common.StackUtils;
import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.Taint.State;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import sun.management.BaseOperatingSystemImpl;
import sun.reflect.ConstantPool;

import java.io.*;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

//public class CorsRegistryCORSDetector extends BasicInjectionDetector {
//
//    private static final String PERMISSIVE_CORS = "PERMISSIVE_CORS";
//    private static final String CORS_REGISTRY_CLASS = "org.springframework.web.servlet.config.annotation.CorsRegistration";
//
//    // javap  -cp C:\Users\x5651\.m2\repository\org\springframework\spring-webmvc\5.1.6.RELEASE\spring-webmvc-5.1.6.RELEASE.jar -s or g.springframework.web.servlet.config.annotation.CorsRegistration
//    private static final InvokeMatcherBuilder CORS_REGISTRY_ALLOWED_ORIGINS_METHOD = invokeInstruction()
//            .atClass(CORS_REGISTRY_CLASS).atMethod("allowedOrigins")
//            .withArgs("([Ljava/lang/String;)Lorg/springframework/web/servlet/config/annotation/CorsRegistration;");
//
//
//    public CorsRegistryCORSDetector(BugReporter bugReporter) {
//        super(bugReporter);
//    }
//
//    /**
//     * 每次调用时都会用该函数判断是否存在漏洞
//     * invoke：表示一次调用
//     */
//    @Override
//    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
//            InstructionHandle handle) {
//        assert invoke != null && cpg != null;
//        // 可以通过一下方法获取InvokeMatcherBuilder的class、method、Signature
//        // System.out.println(invoke.getClassName(cpg));
//        // System.out.println(invoke.getMethodName(cpg));
//        // System.out.println(invoke.getSignature(cpg));
//
//        if (CORS_REGISTRY_ALLOWED_ORIGINS_METHOD.matches(invoke, cpg)) {
//            return new InjectionPoint(new int[] { 0 }, PERMISSIVE_CORS);
//        }
//        return InjectionPoint.NONE;
//    }
//
//    /**
//     * 返回危险等级
//     */
//    @Override
//    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset) throws DataflowAnalysisException {
//        // Get the value of the Access-Control-Allow-Origin parameter (Second argument from setHeader(2nd,1rst))
//        System.out.println(fact.toString());
//        System.out.println(fact.allSlots());
//        System.out.println(fact.getValue(1));
//        Taint  originsTaint= fact.getStackValue(1);
//        System.out.println(originsTaint.getConstantOrPotentialValue());
//
//        return Priorities.HIGH_PRIORITY;
////        if (originsTaint.getConstantOrPotentialValue().contains("*")) { //Ignore unknown/dynamic header name
////            return Priorities.HIGH_PRIORITY;
////        } else {
////            return Priorities.IGNORE_PRIORITY;
////        }
//    }
//}
public class CorsRegistryCORSDetector extends OpcodeStackDetector {

    private static final String PERMISSIVE_CORS = "PERMISSIVE_CORS";
    private BugReporter bugReporter;

    public CorsRegistryCORSDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == Const.INVOKEVIRTUAL && getNameConstantOperand().equals("allowedOrigins")) {
            OpcodeStack.Item item = stack.getStackItem(0); //First item on the stack is the last
            int idx=getNextCodeByte(-5);
            System.out.println(getNextCodeByte(-6)+":"+getNextCodeByte(-5)+":"+getNextCodeByte(4));
            System.out.println(item.getPC());

            System.out.println(getString(8));
            System.out.println(getString(9));
            System.out.println(getString(11));
//            if(StackUtils.isConstantInteger(item)) {
//                Integer value = (Integer) item.getConstant();
//                if(value == null || value != 0) {
//                    bugReporter.reportBug(new BugInstance(this, ANDROID_WORLD_WRITABLE_TYPE, Priorities.NORMAL_PRIORITY) //
//                            .addClass(this).addMethod(this).addSourceLine(this));
//                }
//            }
        }
    }
     public String getString(int idx) {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         DataOutputStream outputStream=new DataOutputStream(baos);
         try {
             getConstantPool().getConstant(idx).dump(outputStream);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return baos.toString();
     }
}
