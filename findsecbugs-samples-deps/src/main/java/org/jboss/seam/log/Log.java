package org.jboss.seam.log;

/**
 * https://github.com/seam2/jboss-seam/blob/f3077fee9d04b2b3545628cd9e6b58c859feb988/jboss-seam/src/main/java/org/jboss/seam/log/Log.java
 */
public interface Log {
    public void trace(Object object, Object... params);
    public void trace(Object object, Throwable t, Object... params);
    public void debug(Object object, Object... params);
    public void debug(Object object, Throwable t, Object... params);
    public void info(Object object, Object... params);
    public void info(Object object, Throwable t, Object... params);
    public void warn(Object object, Object... params);
    public void warn(Object object, Throwable t, Object... params);
    public void error(Object object, Object... params);
    public void error(Object object, Throwable t, Object... params);
    public void fatal(Object object, Object... params);
    public void fatal(Object object, Throwable t, Object... params);
}
