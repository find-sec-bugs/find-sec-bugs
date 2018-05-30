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
package com.h3xstream.findsecbugs.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * Temporary alternative to Map.of (Java 9)
 * https://stackoverflow.com/a/17474581/89769
 */
public class HashMapBuilder {

    public static Map<String, Object> buildObj(String... data) {
        Map<String, Object> result = new HashMap<String, Object>();

        if(data.length % 2 != 0)
            throw new IllegalArgumentException("Odd number of arguments");

        String key = null;
        Integer step = -1;

        for(String value : data){
            step++;
            switch(step % 2){
                case 0:
                    if(value == null)
                        throw new IllegalArgumentException("Null key value");
                    key = value;
                    continue;
                case 1:
                    result.put(key, value);
                    break;
            }
        }

        return result;
    }

    public static Map<String, String> build(String... data) {
        Map<String, String> result = new HashMap<String, String>();

        if(data.length % 2 != 0)
            throw new IllegalArgumentException("Odd number of arguments");

        String key = null;
        Integer step = -1;

        for(String value : data){
            step++;
            switch(step % 2){
                case 0:
                    if(value == null)
                        throw new IllegalArgumentException("Null key value");
                    key = value;
                    continue;
                case 1:
                    result.put(key, value);
                    break;
            }
        }

        return result;
    }
}
