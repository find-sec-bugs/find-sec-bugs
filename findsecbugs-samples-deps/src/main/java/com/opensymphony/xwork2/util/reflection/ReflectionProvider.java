package com.opensymphony.xwork2.util.reflection;

import javax.management.ReflectionException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public interface ReflectionProvider {

    Method getGetMethod(Class targetClass, String propertyName) throws IntrospectionException, ReflectionException;

    Method getSetMethod(Class targetClass, String propertyName) throws IntrospectionException, ReflectionException;

    Field getField(Class inClass, String name);

    void setProperties(Map<String, ?> props, Object o, Map<String, Object> context);

    void setProperties(Map<String, ?> props, Object o, Map<String, Object> context, boolean throwPropertyExceptions) throws ReflectionException;

    void setProperties(Map<String, ?> properties, Object o);

    PropertyDescriptor getPropertyDescriptor(Class targetClass, String propertyName) throws IntrospectionException, ReflectionException;

    void copy(Object from, Object to, Map<String, Object> context, Collection<String> exclusions, Collection<String> inclusions);

    Object getRealTarget(String property, Map<String, Object> context, Object root) throws ReflectionException;

    void setProperty(String name, Object value, Object o, Map<String, Object> context, boolean throwPropertyExceptions);

    void setProperty(String name, Object value, Object o, Map<String, Object> context);

    Map<String, Object> getBeanMap(Object source) throws IntrospectionException, ReflectionException;

    Object getValue( String expression, Map<String, Object> context, Object root ) throws ReflectionException;

    void setValue( String expression, Map<String, Object> context, Object root, Object value ) throws ReflectionException;

    PropertyDescriptor[] getPropertyDescriptors(Object source) throws IntrospectionException;

}
