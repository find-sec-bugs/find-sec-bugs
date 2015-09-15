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
package com.h3xstream.findsecbugs.injection;

public class InjectionPoint {

    /**
     * This instance is use to represent "null" as no injection point.
     * It is a cleaner option than returning NULL by contract. (see Null Object pattern)
     */
    public static final InjectionPoint NONE = new InjectionPoint(new int[0], null);

    private final int[] injectableArguments;
    private String injectableMethod;
    private final String bugType;


    public InjectionPoint(int[] injectableArguments, String bugType) {
        this.injectableArguments = injectableArguments;
        this.bugType = bugType;
    }

    public int[] getInjectableArguments() {
        return injectableArguments;
    }

    public String getBugType() {
        return bugType;
    }

    public String getInjectableMethod() {
        return injectableMethod;
    }

    public void setInjectableMethod(String injectableMethod) {
        this.injectableMethod = injectableMethod;
    }
}
