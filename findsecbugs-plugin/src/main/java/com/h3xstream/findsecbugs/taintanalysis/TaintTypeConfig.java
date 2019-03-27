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
package com.h3xstream.findsecbugs.taintanalysis;

import java.io.IOException;

/**
 * Predecessor for method and class type summary configs
 *
 * @author Tomas Polesovsky (Liferay, Inc.)
 */
public interface TaintTypeConfig {

    /**
     * Initializes the taint config object from String
     *
     * @param taintConfig definition of the state
     * @return initialized object with taint summary
     * @throws java.io.IOException for bad format of parameter
     * @throws NullPointerException if argument is null
     */
    public TaintTypeConfig load(String taintConfig) throws IOException;
}
