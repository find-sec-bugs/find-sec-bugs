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
package testcode.pathtraversal;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public class PathTraversalSeparatorFalsePositive {

    private static String configFileName = "test.properties";

    public static void main(String[] args){
        {
            String DIR1 = ".";
            File t1 = new File(DIR1, FilenameUtils.getName(configFileName)); // OK
            System.out.println(t1.toString());
        }
        {
            String DIR2 = "." + File.separator;
            File t2 = new File(DIR2, FilenameUtils.getName(configFileName)); // OK
            System.out.println(t2.toString());
        }
        {
            String DIR3 = "." + FileSystems.getDefault().getSeparator();
            File t3 = new File(DIR3, FilenameUtils.getName(configFileName)); // OK
            System.out.println(t3.toString());
        }
    }


    public static void danger(String input){
        {
            String DIR1 = ".";
            File t1 = new File(DIR1, input); // TAINTED
            System.out.println(t1.toString());
        }
        {
            String DIR2 = "." + File.separator;
            File t2 = new File(DIR2, input); // TAINTED
            System.out.println(t2.toString());
        }
        {
            String DIR3 = "." + FileSystems.getDefault().getSeparator();
            File t3 = new File(DIR3, input); // TAINTED
            System.out.println(t3.toString());
        }
    }
    
}
