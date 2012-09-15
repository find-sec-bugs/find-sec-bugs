package com.h3xstream.findsecbugs.common;

import org.apache.bcel.classfile.JavaClass;

public class InterfaceUtils {


    public static boolean classImplements(JavaClass javaClass,String interfaceName) {
        String[] interfaces = javaClass.getInterfaceNames();
        for (String name : interfaces) {
            if (name.equals(interfaceName)) {
                return true;
            }
        }
        return false;
    }
}
