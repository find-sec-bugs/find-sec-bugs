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
package testcode.serial.groovy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ConversionHandler implements InvocationHandler, Serializable {
    private Object delegate;

    public ConversionHandler(Object delegate) {
        if (delegate == null) throw new IllegalArgumentException("delegate must not be null");
        this.delegate = delegate;
    }

    public Object getDelegate() {
        return this.delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!checkMethod(method)) {
             return invokeCustom(proxy, method, args);
         }
         try {
             return method.invoke(this, args);
         } catch (InvocationTargetException ite) {
             throw ite.getTargetException();
         }
    }

    protected boolean checkMethod(Method method) {
        return isCoreObjectMethod(method);
    }

    public abstract Object invokeCustom(Object proxy, Method method, Object[] args) throws Throwable;

    public static boolean isCoreObjectMethod(Method method) {
        return Object.class.equals(method.getDeclaringClass());
    }
}
