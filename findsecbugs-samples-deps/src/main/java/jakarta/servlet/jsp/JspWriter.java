package jakarta.servlet.jsp;

import java.io.IOException;

abstract public class JspWriter extends java.io.Writer {

    abstract public void print(boolean b) throws IOException;
    abstract public void print(char c) throws IOException;
    abstract public void print(int i) throws IOException;
    abstract public void print(long l) throws IOException;
    abstract public void print(float f) throws IOException;
    abstract public void print(double d) throws IOException;
    abstract public void print(char s[]) throws IOException;
    abstract public void print(String s) throws IOException;
    abstract public void print(Object obj) throws IOException;
    abstract public void println() throws IOException;
    abstract public void println(boolean x) throws IOException;
    abstract public void println(char x) throws IOException;
    abstract public void println(int x) throws IOException;
    abstract public void println(long x) throws IOException;
    abstract public void println(float x) throws IOException;
    abstract public void println(double x) throws IOException;
    abstract public void println(char x[]) throws IOException;
    abstract public void println(String x) throws IOException;
    abstract public void println(Object x) throws IOException;

}
