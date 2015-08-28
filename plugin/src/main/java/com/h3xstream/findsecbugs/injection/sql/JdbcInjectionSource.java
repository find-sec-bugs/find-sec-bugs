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
package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.*;

/**
 *
 */
public class JdbcInjectionSource implements InjectionSource {

    protected static final String SQL_INJECTION_TYPE = "SQL_INJECTION_JDBC";

    @Override
    public boolean isCandidate(ConstantPoolGen cpg) {
        return true;
    }

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
        //ByteCode.printOpCode(ins,cpg);

        String methodName = ins.getMethodName(cpg);
        String methodSignature = ins.getSignature(cpg);
        String className = ins.getClassName(cpg);

        if (ins instanceof INVOKEINTERFACE) {
            /*
            INVOKESPECIAL org/springframework/jdbc/core/PreparedStatementCreatorFactory.<init> ((Ljava/lang/String;)V)
            INVOKESPECIAL org/springframework/jdbc/core/PreparedStatementCreatorFactory.<init> ((Ljava/lang/String;[I)V)
            INVOKESPECIAL org/springframework/jdbc/core/PreparedStatementCreatorFactory.<init> ((Ljava/lang/String;Ljava/util/List;)V)
            */

            if (className.equals("java.sql.Statement")) {

                //executeQuery signature

                //INVOKEINTERFACE java/sql/Statement.executeQuery ((Ljava/lang/String;)Ljava/sql/ResultSet;)
                if (methodName.equals("executeQuery") && methodSignature.equals("(Ljava/lang/String;)Ljava/sql/ResultSet;")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }


                //INVOKEINTERFACE java/sql/Statement.execute ((Ljava/lang/String;)Z)
                else if (methodName.equals("execute") && methodSignature.equals("(Ljava/lang/String;)Z")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.execute ((Ljava/lang/String;I)Z)
                else if (methodName.equals("execute") && methodSignature.equals("(Ljava/lang/String;I)Z")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.execute ((Ljava/lang/String;[I)Z)
                else if (methodName.equals("execute") && methodSignature.equals("(Ljava/lang/String;[I)Z")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.execute ((Ljava/lang/String;[Ljava/lang/String;)Z)
                else if (methodName.equals("execute") && methodSignature.equals("(Ljava/lang/String;[Ljava/lang/String;)Z")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }

                //executeUpdate signatures

                //INVOKEINTERFACE java/sql/Statement.executeUpdate ((Ljava/lang/String;)I)
                else if (methodName.equals("executeUpdate") && methodSignature.equals("(Ljava/lang/String;)I")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.executeUpdate ((Ljava/lang/String;I)I)
                else if (methodName.equals("executeUpdate") && methodSignature.equals("(Ljava/lang/String;I)I")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.executeUpdate ((Ljava/lang/String;[I)I)
                else if (methodName.equals("executeUpdate") && methodSignature.equals("(Ljava/lang/String;[I)I")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.executeUpdate ((Ljava/lang/String;[Ljava/lang/String;)I)
                else if (methodName.equals("executeUpdate") && methodSignature.equals("(Ljava/lang/String;[Ljava/lang/String;)I")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }

                //executeLargeUpdate signatures

                //INVOKEINTERFACE java/sql/Statement.executeLargeUpdate ((Ljava/lang/String;)J)
                else if (methodName.equals("executeLargeUpdate") && methodSignature.equals("(Ljava/lang/String;)J")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.executeLargeUpdate ((Ljava/lang/String;I)J)
                else if (methodName.equals("executeLargeUpdate") && methodSignature.equals("(Ljava/lang/String;I)J")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.executeLargeUpdate ((Ljava/lang/String;[I)J)
                else if (methodName.equals("executeLargeUpdate") && methodSignature.equals("(Ljava/lang/String;[I)J")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Statement.executeLargeUpdate ((Ljava/lang/String;[Ljava/lang/String;)J)
                else if (methodName.equals("executeLargeUpdate") && methodSignature.equals("(Ljava/lang/String;[Ljava/lang/String;)J")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }

                //INVOKEINTERFACE java/sql/Statement.addBatch ((Ljava/lang/String;)V)
                else if (methodName.equals("addBatch") && methodSignature.equals("(Ljava/lang/String;)V")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }


            }
            else if (className.equals("java.sql.Connection")) {


                //prepareCall signatures

                //INVOKEINTERFACE java/sql/Connection.prepareCall ((Ljava/lang/String;)Ljava/sql/CallableStatement;)
                if (methodName.equals("prepareCall") && methodSignature.equals("(Ljava/lang/String;)Ljava/sql/CallableStatement;")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Connection.prepareCall ((Ljava/lang/String;II)Ljava/sql/CallableStatement;)
                else if (methodName.equals("prepareCall") && methodSignature.equals("(Ljava/lang/String;II)Ljava/sql/CallableStatement;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Connection.prepareCall ((Ljava/lang/String;III)Ljava/sql/CallableStatement;)
                else if (methodName.equals("prepareCall") && methodSignature.equals("(Ljava/lang/String;III)Ljava/sql/CallableStatement;")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }

                //prepareStatement signatures

                //INVOKEINTERFACE java/sql/Connection.prepareStatement ((Ljava/lang/String;)Ljava/sql/PreparedStatement;)
                else if (methodName.equals("prepareStatement") && methodSignature.equals("(Ljava/lang/String;)Ljava/sql/PreparedStatement;")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Connection.prepareStatement ((Ljava/lang/String;I)Ljava/sql/PreparedStatement;)
                else if (methodName.equals("prepareStatement") && methodSignature.equals("(Ljava/lang/String;I)Ljava/sql/PreparedStatement;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Connection.prepareStatement ((Ljava/lang/String;II)Ljava/sql/PreparedStatement;)
                else if (methodName.equals("prepareStatement") && methodSignature.equals("(Ljava/lang/String;II)Ljava/sql/PreparedStatement;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Connection.prepareStatement ((Ljava/lang/String;III)Ljava/sql/PreparedStatement;)
                else if (methodName.equals("prepareStatement") && methodSignature.equals("(Ljava/lang/String;III)Ljava/sql/PreparedStatement;")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Connection.prepareStatement ((Ljava/lang/String;[I)Ljava/sql/PreparedStatement;)
                else if (methodName.equals("prepareStatement") && methodSignature.equals("(Ljava/lang/String;[I)Ljava/sql/PreparedStatement;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEINTERFACE java/sql/Connection.prepareStatement ((Ljava/lang/String;[Ljava/lang/String;)Ljava/sql/PreparedStatement;)
                else if (methodName.equals("prepareStatement") && methodSignature.equals("(Ljava/lang/String;[Ljava/lang/String;)Ljava/sql/PreparedStatement;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }

                //nativeSQL signatures

                //INVOKEINTERFACE java/sql/Connection.nativeSQL ((Ljava/lang/String;)Ljava/lang/String;)
                else if (methodName.equals("nativeSQL") && methodSignature.equals("(Ljava/lang/String;)Ljava/lang/String;")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }

            }
        }


        return InjectionPoint.NONE;
    }
}
