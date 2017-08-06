package javax.servlet;

import java.io.IOException;
import java.io.PrintWriter;

public interface ServletResponse {

    PrintWriter getWriter() throws IOException;

    ServletOutputStream getOutputStream() throws IOException;

    void setContentType(String contentType);
}
