package org.apache.tomcat;


import java.lang.reflect.InvocationTargetException;

import javax.naming.NamingException;
//STUB note: directly referenced in JSPC-generated code
public interface InstanceManager {

    public Object newInstance(Class<?> clazz)
            throws IllegalAccessException, InvocationTargetException, NamingException,
                InstantiationException;

    public Object newInstance(String className)
        throws IllegalAccessException, InvocationTargetException, NamingException,
            InstantiationException, ClassNotFoundException;

    public Object newInstance(String fqcn, ClassLoader classLoader)
        throws IllegalAccessException, InvocationTargetException, NamingException,
            InstantiationException, ClassNotFoundException;

    public void newInstance(Object o)
        throws IllegalAccessException, InvocationTargetException, NamingException;

    public void destroyInstance(Object o)
        throws IllegalAccessException, InvocationTargetException;
}
