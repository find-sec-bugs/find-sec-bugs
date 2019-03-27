package javax.servlet.jsp.tagext;

public interface TryCatchFinally {
    void doCatch(Throwable var1) throws Throwable;

    void doFinally();
}