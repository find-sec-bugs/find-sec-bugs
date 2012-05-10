package edu.umd.cs.findbugs.test.service;

import java.net.URL;

public class ClassFileLocator {

    /**
     * @param path
     * @return Full path to the class file base on class name.
     */
    public String getClassFilePath(String path) {
        ClassLoader cl = getClass().getClassLoader();
        URL url = cl.getResource(path+".class");

        String filename = url.toString();

        final String prefix = "file:/";
        if(filename.startsWith(prefix)) {
            filename = filename.substring(prefix.length());
        }
        return filename;
    }
}
