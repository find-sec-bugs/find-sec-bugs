package com.opensymphony.xwork2.util;

public interface ValueStack {

    public abstract void setValue(String expr, Object value);
    void setParameter(String expr, Object value);
    public abstract void setValue(String expr, Object value, boolean throwExceptionOnFailure);
    public abstract String findString(String expr);
    public abstract String findString(String expr, boolean throwExceptionOnFailure);
    public abstract Object findValue(String expr);
    public abstract Object findValue(String expr, boolean throwExceptionOnFailure);
    public abstract Object findValue(String expr, Class asType);
    public abstract Object findValue(String expr, Class asType,  boolean throwExceptionOnFailure);
}
