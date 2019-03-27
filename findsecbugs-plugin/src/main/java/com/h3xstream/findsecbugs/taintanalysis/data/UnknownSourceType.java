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
package com.h3xstream.findsecbugs.taintanalysis.data;

/**
 * This enum document the type of unknown source.
 */
public enum UnknownSourceType {
    /**
     * Define unknown value where the value is coming from a function parameter.
     * It may also be the combination of a parameter with another source.
     * <code>
     *     void callMe(String userInput) {
     *         executeQuery("SELECT * FROM User WHERE id=" + userInput);
     *     }
     * </code>
     */
    PARAMETER,

    /**
     * Define unknown value where the value is coming from an external call.
     *
     * <code>
     *     void callMe() {
     *         executeQuery("SELECT * FROM User WHERE id=" + externalCall());
     *     }
     * </code>
     */
    RETURN,

    /**
     * Define unknown value where the value is coming from a field.
     * <code>
     *     private String userInput;
     *
     *     void callMe() {
     *         executeQuery("SELECT * FROM User WHERE id=" + userInput);
     *     }
     * </code>
     */
    FIELD
}
