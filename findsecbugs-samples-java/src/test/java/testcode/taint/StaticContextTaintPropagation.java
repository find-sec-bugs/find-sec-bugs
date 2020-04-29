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
package testcode.taint;

import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tomas Polesovsky
 */
public class StaticContextTaintPropagation {
    private HttpServletRequest request;
    private SessionFactory sessionFactory;

    private static String simpleStaticVar = "Test";
    private static String computedStaticVar = "(" + surround(simpleStaticVar, "%") + ")";

    private static String propagatingStaticVar = "";

    private static String surround(String a, String b) {
        if (a == null) {
            return b + b;
        }

        return b + a + b;
    }

    public void falsePositive() {
        sessionFactory.openSession().createQuery("FROM comment WHERE userId like " + computedStaticVar);
    }

    public void taintedValue() {
        simpleStaticVar = request.getParameter("test");

        sessionFactory.openSession().createQuery("FROM comment WHERE userId like " + simpleStaticVar);
    }

    public void staticContextTaintPropagation() {
        propagatingStaticVar = request.getParameter("test");

        staticContextTaintPropagationAfter();
    }

    public void staticContextTaintPropagationAfter() {
        sessionFactory.openSession().createQuery("FROM comment WHERE userId like " + propagatingStaticVar);
    }

    public static class Issue541 {
        private static Issue541 INSTANCE;

        public static Issue541 get() {
            return INSTANCE;
        }

        public static void set(Issue541 issue541) {
            INSTANCE = issue541;
        }

        static {
            get();
        }
    }
}