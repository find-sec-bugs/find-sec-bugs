package com.h3xstream.findsecbugs.injection;

import java.util.Objects;

/**
 * @author Tomas Polesovsky
 */
public class ClassMethodSignature {
    private String className;
    private String methodName;
    private String signature;

    public ClassMethodSignature(String className, String methodName, String signature) {
        this.className = className;
        this.methodName = methodName;
        this.signature = signature;
    }

    public static ClassMethodSignature from(String fullMethodName) {
        int periodPos = fullMethodName.indexOf('.');
        int parenthesisPos = fullMethodName.indexOf('(', periodPos);

        String className = fullMethodName.substring(0, periodPos);
        String methodName = fullMethodName.substring(periodPos + 1, parenthesisPos);
        String signature = fullMethodName.substring(parenthesisPos);

        return new ClassMethodSignature(className, methodName, signature);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassMethodSignature that = (ClassMethodSignature) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName, signature);
    }
}
