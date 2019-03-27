package com.opensymphony.xwork2.ognl;

import ognl.OgnlException;

import javax.management.ReflectionException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public class OgnlReflectionProvider {
    private OgnlUtil ognlUtil;

    public void setOgnlUtil(OgnlUtil ognlUtil) {
        this.ognlUtil = ognlUtil;
    }

    public Field getField(Class inClass, String name) {
        return null;
    }

    public Method getGetMethod(Class targetClass, String propertyName)
            throws IntrospectionException, ReflectionException {

        return null;
    }

    public Method getSetMethod(Class targetClass, String propertyName)
            throws IntrospectionException, ReflectionException {

        return null;
    }

    public void setProperties(Map<String, ?> props, Object o, Map<String, Object> context) {
        ognlUtil.setProperties(props, o, context);
    }

    public void setProperties(Map<String, ?> props, Object o, Map<String, Object> context, boolean throwPropertyExceptions) throws ReflectionException{
        ognlUtil.setProperties(props, o, context, throwPropertyExceptions);

    }

    public void setProperties(Map<String, ?> properties, Object o) {
        ognlUtil.setProperties(properties, o);
    }

    public PropertyDescriptor getPropertyDescriptor(Class targetClass,
                                                    String propertyName) throws IntrospectionException,
            ReflectionException {
        return null;
    }

    public void copy(Object from, Object to, Map<String, Object> context,
                     Collection<String> exclusions, Collection<String> inclusions) {
    }

    public Object getRealTarget(String property, Map<String, Object> context, Object root)
            throws ReflectionException {

        return null;
    }

    public void setProperty(String name, Object value, Object o, Map<String, Object> context) {

    }

    public void setProperty(String name, Object value, Object o, Map<String, Object> context, boolean throwPropertyExceptions) {

    }

    public Map getBeanMap(Object source) throws IntrospectionException,
            ReflectionException {
        return null;
    }

    public Object getValue(String expression, Map<String, Object> context, Object root)
            throws ReflectionException {
        return null;
    }

    public void setValue(String expression, Map<String, Object> context, Object root,
                         Object value) throws ReflectionException {
    }

    public PropertyDescriptor[] getPropertyDescriptors(Object source)
            throws IntrospectionException {
        return ognlUtil.getPropertyDescriptors(source);
    }
}
