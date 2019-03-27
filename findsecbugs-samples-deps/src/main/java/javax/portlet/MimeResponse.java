package javax.portlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface MimeResponse {

    PrintWriter getWriter() throws IOException;

    OutputStream getPortletOutputStream() throws IOException;
}
