package com.opensymphony.xwork2.ognl;

import ognl.ClassResolver;
import ognl.OgnlException;
import ognl.TypeConverter;

import javax.management.ReflectionException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class OgnlUtil {


    public void setDevMode(String mode) {

    }

    public void setEnableExpressionCache(String cache) {

    }

    public void setEnableEvalExpression(String evalExpression) {

    }

    public void setExcludedClasses(String commaDelimitedClasses) {

    }

    public void setExcludedPackageNamePatterns(String commaDelimitedPackagePatterns) {

    }

    public void setExcludedPackageNames(String commaDelimitedPackageNames) {
    }

    public Set<Class<?>> getExcludedClasses() {
        return null;
    }

    public Set<Pattern> getExcludedPackageNamePatterns() {
        return null;
    }

    public Set<String> getExcludedPackageNames() {
        return null;
    }

    public void setAllowStaticMethodAccess(String allowStaticMethodAccess) {
    }

    public void setProperties(Map<String, ?> props, Object o, Map<String, Object> context) {
    }

    public void setProperties(Map<String, ?> props, Object o, Map<String, Object> context, boolean throwPropertyExceptions) throws ReflectionException {

    }

    public void setProperties(Map<String, ?> properties, Object o) {
        setProperties(properties, o, false);
    }

    public void setProperties(Map<String, ?> properties, Object o, boolean throwPropertyExceptions) {

    }

    public void setProperty(String name, Object value, Object o, Map<String, Object> context) {
    }

    public void setProperty(String name, Object value, Object o, Map<String, Object> context, boolean throwPropertyExceptions) {

    }

    public Object getRealTarget(String property, Map<String, Object> context, Object root) throws OgnlException {
        return root;
    }

    public void setValue(final String name, final Map<String, Object> context, final Object root, final Object value) throws OgnlException {

    }

    private boolean isEvalExpression(Object tree, Map<String, Object> context) throws OgnlException {

        return false;
    }

    private boolean isArithmeticExpression(Object tree, Map<String, Object> context) throws OgnlException {

        return false;
    }

    private boolean isSimpleMethod(Object tree, Map<String, Object> context) throws OgnlException {

        return false;
    }

    public Object getValue(final String name, final Map<String, Object> context, final Object root) throws OgnlException {

        return null;
    }

    public Object callMethod(final String name, final Map<String, Object> context, final Object root) throws OgnlException {

        return null;
    }

    public Object getValue(final String name, final Map<String, Object> context, final Object root, final Class resultType) throws OgnlException {
        return null;
    }


    public Object compile(String expression) throws OgnlException {
        return null;
    }

    private <T> Object compileAndExecute(String expression, Map<String, Object> context, OgnlTask<T> task) throws OgnlException {
        return null;
    }

    private <T> Object compileAndExecuteMethod(String expression, Map<String, Object> context, OgnlTask<T> task) throws OgnlException {

        return null;
    }

    public Object compile(String expression, Map<String, Object> context) throws OgnlException {
        return null;
    }

    private void checkEnableEvalExpression(Object tree, Map<String, Object> context) throws OgnlException {

    }

    private void checkSimpleMethod(Object tree, Map<String, Object> context) throws OgnlException {

    }

    public void copy(final Object from, final Object to, final Map<String, Object> context, Collection<String> exclusions, Collection<String> inclusions) {

    }

    public void copy(Object from, Object to, Map<String, Object> context) {
        copy(from, to, context, null, null);
    }

    public PropertyDescriptor[] getPropertyDescriptors(Object source) throws IntrospectionException {

        return null;
    }


    public PropertyDescriptor[] getPropertyDescriptors(Class clazz) throws IntrospectionException {

        return null;
    }

    public Map<String, Object> getBeanMap(final Object source) throws IntrospectionException, OgnlException {
        return null;
    }

    public BeanInfo getBeanInfo(Object from) throws IntrospectionException {

        return null;
    }

    public BeanInfo getBeanInfo(Class clazz) throws IntrospectionException {

        return null;
    }

    void internalSetProperty(String name, Object value, Object o, Map<String, Object> context, boolean throwPropertyExceptions) throws ReflectionException{

    }

    TypeConverter getTypeConverterFromContext(Map<String, Object> context) {

        return null;
    }

    protected Map createDefaultContext(Object root) {
        return createDefaultContext(root, null);
    }

    protected Map createDefaultContext(Object root, ClassResolver classResolver) {

        return null;
    }

    private interface OgnlTask<T> {
        T execute(Object tree) throws OgnlException;
    }
}
