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

import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;

public class SqlInjectionDetector extends BasicInjectionDetector {

    public SqlInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("sql-hibernate.txt", "SQL_INJECTION_HIBERNATE");
        loadConfiguredSinks("sql-jdo.txt", "SQL_INJECTION_JDO");
        loadConfiguredSinks("sql-jpa.txt", "SQL_INJECTION_JPA");
        loadConfiguredSinks("sql-jdbc.txt", "SQL_INJECTION_JDBC");
        loadConfiguredSinks("sql-spring.txt", "SQL_INJECTION_SPRING_JDBC");
        loadConfiguredSinks("sql-scala-slick.txt", "SCALA_SQL_INJECTION_SLICK");
        loadConfiguredSinks("sql-scala-anorm.txt", "SCALA_SQL_INJECTION_ANORM");
        loadConfiguredSinks("sql-turbine.txt", "SQL_INJECTION_TURBINE");
        loadConfiguredSinks("sql-vertx-sql-client.txt", "SQL_INJECTION_VERTX");
        //TODO : Add org.springframework.jdbc.core.simple.SimpleJdbcTemplate (Spring < 3.2.1)
    }
    
    @Override
    protected int getPriority(Taint taint) {
        if (!taint.isSafe() && taint.hasTag(Taint.Tag.SQL_INJECTION_SAFE)) {
            return Priorities.IGNORE_PRIORITY;
        } else if (!taint.isSafe() && taint.hasTag(Taint.Tag.APOSTROPHE_ENCODED)) {
            return Priorities.LOW_PRIORITY;
        } else {
            return super.getPriority(taint);
        }
    }
}
