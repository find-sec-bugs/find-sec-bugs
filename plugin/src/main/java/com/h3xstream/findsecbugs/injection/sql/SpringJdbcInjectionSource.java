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
import org.apache.bcel.generic.*;

public class SpringJdbcInjectionSource implements InjectionSource {

    protected static final String SQL_INJECTION_TYPE = "SQL_INJECTION_SPRING_JDBC";

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

        if (ins instanceof INVOKESPECIAL) {
            /*
            INVOKESPECIAL org/springframework/jdbc/core/PreparedStatementCreatorFactory.<init> ((Ljava/lang/String;)V)
            INVOKESPECIAL org/springframework/jdbc/core/PreparedStatementCreatorFactory.<init> ((Ljava/lang/String;[I)V)
            INVOKESPECIAL org/springframework/jdbc/core/PreparedStatementCreatorFactory.<init> ((Ljava/lang/String;Ljava/util/List;)V)
            */

            if (className.equals("org.springframework.jdbc.core.PreparedStatementCreatorFactory") && methodName.equals("<init>")) {
                if(methodSignature.equals("(Ljava/lang/String;)V")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                else if(methodSignature.equals("(Ljava/lang/String;[I)V") || methodSignature.equals("(Ljava/lang/String;Ljava/util/List;)V")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
            }

        }
        //FIXME : Add equivalent API for org.springframework.jdbc.core.simple.SimpleJdbcTemplate (Spring < 3.2.1)
        else if ((ins instanceof INVOKEVIRTUAL && className.equals("org.springframework.jdbc.core.JdbcTemplate")) ||
                (ins instanceof INVOKEINTERFACE && className.equals("org.springframework.jdbc.core.JdbcOperations")) ) {

            if (methodName.equals("execute")) {

                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.execute ((Ljava/lang/String;)V)
                if(methodSignature.equals("(Ljava/lang/String;)V")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.execute ((Ljava/lang/String;Lorg/springframework/jdbc/core/PreparedStatementCallback;)Ljava/lang/Object;)
                else if(methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/PreparedStatementCallback;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.execute ((Ljava/lang/String;Lorg/springframework/jdbc/core/CallableStatementCallback;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/CallableStatementCallback;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
            }

            else if (methodName.equals("batchUpdate")) {

                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.batchUpdate (([Ljava/lang/String;)[I)
                if(methodSignature.equals("([Ljava/lang/String;)[I")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.batchUpdate ((Ljava/lang/String;Lorg/springframework/jdbc/core/BatchPreparedStatementSetter;)[I)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/BatchPreparedStatementSetter;)[I")) {

                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.batchUpdate ((Ljava/lang/String;Lorg/springframework/jdbc/core/BatchPreparedStatementSetter;)[I)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/BatchPreparedStatementSetter;)[I")) {

                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.batchUpdate ((Ljava/lang/String;Ljava/util/Collection;ILorg/springframework/jdbc/core/ParameterizedPreparedStatementSetter;)[[I)
                else if (methodSignature.equals("(Ljava/lang/String;Ljava/util/Collection;ILorg/springframework/jdbc/core/ParameterizedPreparedStatementSetter;)[[I")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }

                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.batchUpdate ((Ljava/lang/String;Ljava/util/List;)[I)
                else if (methodSignature.equals("(Ljava/lang/String;Ljava/util/List;)[I")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }

                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.batchUpdate ((Ljava/lang/String;Ljava/util/List;[I)[I)
                else if (methodSignature.equals("(Ljava/lang/String;Ljava/util/List;[I)[I")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
            }

            else if (methodName.equals("queryForObject")) {
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForObject ((Ljava/lang/String;Lorg/springframework/jdbc/core/RowMapper;)Ljava/lang/Object;)
                if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/RowMapper;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForObject ((Ljava/lang/String;Lorg/springframework/jdbc/core/RowMapper;[Ljava/lang/Object;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/RowMapper;[Ljava/lang/Object;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForObject ((Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }

                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForObject ((Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForObject ((Ljava/lang/String;[Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForObject ((Ljava/lang/String;[Ljava/lang/Object;[ILjava/lang/Class;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[ILjava/lang/Class;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForObject ((Ljava/lang/String;[Ljava/lang/Object;[ILorg/springframework/jdbc/core/RowMapper;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[ILorg/springframework/jdbc/core/RowMapper;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForObject ((Ljava/lang/String;[Ljava/lang/Object;Lorg/springframework/jdbc/core/RowMapper;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;Lorg/springframework/jdbc/core/RowMapper;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
            }

            else if (methodName.equals("query")) {

                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;Lorg/springframework/jdbc/core/ResultSetExtractor;)Ljava/lang/Object;)
                if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/ResultSetExtractor;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;Lorg/springframework/jdbc/core/RowCallbackHandler;)V)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/RowCallbackHandler;)V")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;Lorg/springframework/jdbc/core/RowMapper;)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/RowMapper;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }

                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;Lorg/springframework/jdbc/core/PreparedStatementSetter;Lorg/springframework/jdbc/core/ResultSetExtractor;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/PreparedStatementSetter;Lorg/springframework/jdbc/core/ResultSetExtractor;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;Lorg/springframework/jdbc/core/PreparedStatementSetter;Lorg/springframework/jdbc/core/RowCallbackHandler;)V)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/PreparedStatementSetter;Lorg/springframework/jdbc/core/RowCallbackHandler;)V")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;Lorg/springframework/jdbc/core/PreparedStatementSetter;Lorg/springframework/jdbc/core/RowMapper;)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;Lorg/springframework/jdbc/core/PreparedStatementSetter;Lorg/springframework/jdbc/core/RowMapper;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;[Ljava/lang/Object;Lorg/springframework/jdbc/core/RowMapper;)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;Lorg/springframework/jdbc/core/RowMapper;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;[Ljava/lang/Object;Lorg/springframework/jdbc/core/RowCallbackHandler;)V)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;Lorg/springframework/jdbc/core/RowCallbackHandler;)V")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;[Ljava/lang/Object;Lorg/springframework/jdbc/core/ResultSetExtractor;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;Lorg/springframework/jdbc/core/ResultSetExtractor;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;[Ljava/lang/Object;[ILorg/springframework/jdbc/core/ResultSetExtractor;)Ljava/lang/Object;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[ILorg/springframework/jdbc/core/ResultSetExtractor;)Ljava/lang/Object;")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;[Ljava/lang/Object;[ILorg/springframework/jdbc/core/RowMapper;)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[ILorg/springframework/jdbc/core/RowMapper;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.query ((Ljava/lang/String;[Ljava/lang/Object;[ILorg/springframework/jdbc/core/RowCallbackHandler;)V)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[ILorg/springframework/jdbc/core/RowCallbackHandler;)V")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }
            }

            else if (methodName.equals("queryForList")) {
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForList ((Ljava/lang/String;)Ljava/util/List;)
                if (methodSignature.equals("(Ljava/lang/String;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForList ((Ljava/lang/String;Ljava/lang/Class;)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;Ljava/lang/Class;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForList ((Ljava/lang/String;[Ljava/lang/Object;Ljava/lang/Class;)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;Ljava/lang/Class;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForList ((Ljava/lang/String;[Ljava/lang/Object;[I)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[I)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForList ((Ljava/lang/String;[Ljava/lang/Object;[ILjava/lang/Class;)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[ILjava/lang/Class;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{3}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForList ((Ljava/lang/String;[Ljava/lang/Object;)Ljava/util/List;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;)Ljava/util/List;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
            }

            else if (methodName.equals("queryForMap")) {
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForMap ((Ljava/lang/String;)Ljava/util/Map;)
                if (methodSignature.equals("(Ljava/lang/String;)Ljava/util/Map;")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForMap ((Ljava/lang/String;[Ljava/lang/Object;)Ljava/util/Map;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;)Ljava/util/Map;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForMap ((Ljava/lang/String;[Ljava/lang/Object;[I)Ljava/util/Map;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[I)Ljava/util/Map;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
            }

            else if (methodName.equals("queryForRowSet")) {
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;)Lorg/springframework/jdbc/support/rowset/SqlRowSet;)
                if (methodSignature.equals("(Ljava/lang/String;)Lorg/springframework/jdbc/support/rowset/SqlRowSet;")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;[Ljava/lang/Object;)Lorg/springframework/jdbc/support/rowset/SqlRowSet;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;)Lorg/springframework/jdbc/support/rowset/SqlRowSet;")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;[Ljava/lang/Object;[I)Lorg/springframework/jdbc/support/rowset/SqlRowSet;)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[I)Lorg/springframework/jdbc/support/rowset/SqlRowSet;")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
            }

            else if (methodName.equals("queryForInt")) {
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;)I)
                if (methodSignature.equals("(Ljava/lang/String;)I")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;[Ljava/lang/Object;)I)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;)I")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;[Ljava/lang/Object;[I)I)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[I)I")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
            }

            else if (methodName.equals("queryForLong")) {
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;)J)
                if (methodSignature.equals("(Ljava/lang/String;)J")) {
                    return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;[Ljava/lang/Object;)J)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;)J")) {
                    return new InjectionPoint(new int[]{1}, SQL_INJECTION_TYPE);
                }
                //INVOKEVIRTUAL org/springframework/jdbc/core/JdbcTemplate.queryForRowSet ((Ljava/lang/String;[Ljava/lang/Object;[I)J)
                else if (methodSignature.equals("(Ljava/lang/String;[Ljava/lang/Object;[I)J")) {
                    return new InjectionPoint(new int[]{2}, SQL_INJECTION_TYPE);
                }
            }
        }

        return InjectionPoint.NONE;
    }
}
