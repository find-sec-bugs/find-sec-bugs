package javax.servlet;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ServletOutputStream extends OutputStream {

    public void print(String s) throws IOException { }

    public void println(String s) throws IOException { }
}
