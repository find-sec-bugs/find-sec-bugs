package org.springframework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

public abstract class ReflectionUtils {

    public static Method findMethod(Class<?> clazz, String name) {
        return null;
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) { return null; }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        return null;
    }

    public static Object invokeJdbcMethod(Method method, Object target) throws SQLException {
        return null;
    }

    public static Object invokeJdbcMethod(Method method, Object target, Object... args) throws SQLException {
        return null;
    }
}
