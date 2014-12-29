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

import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * UnboundId API offers both a Typed and a string based filters.
 * This detector only look at filter parameters from query..
 */
public class UnboundIdLdapInjectionSource implements InjectionSource {

    private static final String LDAP_INJECTION_TYPE = "LDAP_INJECTION";

    @Override
    public boolean isCandidate(ConstantPoolGen cpg) {
        for (int i = 0; i < cpg.getSize(); i++) {
            Constant cnt = cpg.getConstant(i);
            if (cnt instanceof ConstantUtf8) {
                String utf8String = ((ConstantUtf8) cnt).getBytes();
//                System.out.println("cnt= "+utf8String);
                if (utf8String.equals("com/unboundid/ldap/sdk/LDAPConnection")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {

        if (ins instanceof INVOKEVIRTUAL) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            if (className.equals("com.unboundid.ldap.sdk.LDAPConnection") && methodName.equals("search") &&
                    methodSignature.equals("(Ljava/lang/String;Lcom/unboundid/ldap/sdk/SearchScope;Ljava/lang/String;[Ljava/lang/String;)Lcom/unboundid/ldap/sdk/SearchResult;")) {
                return new InjectionPoint(new int[]{0, 2},LDAP_INJECTION_TYPE);
            }
        }

        return InjectionPoint.NONE;
    }

}
