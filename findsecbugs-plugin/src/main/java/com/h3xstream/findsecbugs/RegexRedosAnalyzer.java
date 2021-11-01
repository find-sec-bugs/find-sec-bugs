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
package com.h3xstream.findsecbugs;

public class RegexRedosAnalyzer {

    private static final char[] OPENING_CHAR = {'(', '['};

    private static final char[] CLOSING_CHAR = {')', ']'};

    private static final char[] PLUS_CHAR = {'+', '*', '?'};

    private boolean vulnerable = false;


    public boolean isVulnerable() {
        return vulnerable;
    }

    public void analyseRegexString(String regex) {
        if (regex.length() > 0) {
            recurAnalyseRegex(regex, regex.length() - 1, 0);
        }
    }

    private int recurAnalyseRegex(String regex, int startPosition, int level) {
//        print(level, "level = " + level);
        if (level == 2) {
            vulnerable = true;

            return 0;
        }


//        print(level, "Analysing " + regex.substring(0, startPosition + 1));

        boolean openingMode = false;

        for (int i = startPosition; i >= 0; i--) {
//            print(level, "[" + i + "] = '" + regex.charAt(i) + "'");

            if (isChar(regex, i, OPENING_CHAR)) {
//                print(level, "<<<<");
                return i;
            }

            if (isChar(regex, i, CLOSING_CHAR)) {
                int newLevel = level;
                if (i + 1 < regex.length() && isChar(regex, i + 1, PLUS_CHAR)) {
                    newLevel += 1;
                }
//                print(level, ">>>>");
                openingMode = true;
                i = recurAnalyseRegex(regex, i - 1, newLevel);
                if (i == -1) {
                    return 0;
                }
//                print(level, "Restarting at " + i);
            }
        }

//        print(level, "END!");

        return 0;
    }

    /**
     * @param value
     * @param position
     * @param charToTest
     * @return
     */
    private boolean isChar(String value, int position, char[] charToTest) {
        char actualChar = value.charAt(position);
        boolean oneCharFound = false;
        for (char ch : charToTest) {
            if (actualChar == ch) {
                oneCharFound = true;
                break;
            }
        }
        return oneCharFound && (position == 0 || value.charAt(position - 1) != '\\');
    }

    //Debug method

//    private void print(int level,Object obj) {
//        System.out.println(lvl(level) + "> "+ obj);
//    }
//
//    private String lvl(int level) {
//        StringBuilder str = new StringBuilder();
//        for(int i=0;i<level;i++) {
//            str.append("-\t");
//        }
//        return str.toString();
//    }
}
