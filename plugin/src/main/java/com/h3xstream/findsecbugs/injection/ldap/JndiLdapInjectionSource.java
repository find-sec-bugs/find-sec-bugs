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
package com.h3xstream.findsecbugs.injection.ldap;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

public class JndiLdapInjectionSource implements InjectionSource {

    private static final String LDAP_INJECTION_TYPE = "LDAP_INJECTION";

    @Override
    public boolean isCandidate(ConstantPoolGen cpg) {
        for (int i = 0; i < cpg.getSize(); i++) {
            Constant cnt = cpg.getConstant(i);
            if (cnt instanceof ConstantUtf8) {
                String utf8String = ((ConstantUtf8) cnt).getBytes();
//                System.out.println("cnt= "+utf8String);
                if (utf8String.equals("javax/naming/directory/InitialDirContext")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
        //ByteCode.printOpCode(ins, cpg);
        // Bytecode of the test cases:
        //        INVOKESPECIAL javax/naming/ldap/LdapName.<init>(Ljava/lang/String;)V
        //        INVOKEVIRTUAL javax/naming/directory/InitialDirContext.search(Ljavax/naming/Name;Ljava/lang/String;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;
        //        INVOKESPECIAL javax/naming/ldap/LdapName.<init>(Ljava/lang/String;)V
        //        INVOKEVIRTUAL javax/naming/directory/InitialDirContext.search(Ljavax/naming/Name;Ljava/lang/String;[Ljava/lang/Object;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;
        //        INVOKEVIRTUAL javax/naming/directory/InitialDirContext.search(Ljava/lang/String;Ljava/lang/String;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;
        //        INVOKEVIRTUAL javax/naming/directory/InitialDirContext.search(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;


        String methodName = ins.getMethodName(cpg);
        String methodSignature = ins.getSignature(cpg);
        String className = ins.getClassName(cpg);

            if ((ins instanceof INVOKEVIRTUAL && className.equals("javax.naming.directory.InitialDirContext") && methodName.equals("search")) ||
                (ins instanceof INVOKEINTERFACE && className.equals("javax.naming.directory.DirContext") && methodName.equals("search")) ||
                (ins instanceof INVOKEVIRTUAL && className.equals("javax.naming.ldap.InitialLdapContext") && methodName.equals("search")) ||
                (ins instanceof INVOKEINTERFACE && className.equals("javax.naming.ldap.LdapContext") && methodName.equals("search")) ||
                (ins instanceof INVOKEVIRTUAL && className.equals("com.sun.jndi.ldap.LdapCtx") && methodName.equals("search")) ||
                (ins instanceof INVOKEINTERFACE && className.equals("javax.naming.event.EventDirContext") && methodName.equals("search"))) {

                if(methodSignature.equals("(Ljavax/naming/Name;Ljava/lang/String;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;")) {
                    return new InjectionPoint(new int[]{1}, LDAP_INJECTION_TYPE);
                }
                else if(methodSignature.equals("(Ljavax/naming/Name;Ljava/lang/String;[Ljava/lang/Object;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;")) {
                    return new InjectionPoint(new int[]{2},LDAP_INJECTION_TYPE);
                }
                else if(methodSignature.equals("(Ljava/lang/String;Ljava/lang/String;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;")) {
                    return new InjectionPoint(new int[]{1, 2},LDAP_INJECTION_TYPE);
                }
                else if(methodSignature.equals("(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;")) {
                    return new InjectionPoint(new int[]{2, 3},LDAP_INJECTION_TYPE);
                }
            }
        return InjectionPoint.NONE;
    }


}
