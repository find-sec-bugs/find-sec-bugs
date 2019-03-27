package javax.portlet;

import java.io.IOException;
import java.io.PrintWriter;

public interface RenderResponse extends MimeResponse {
    void setContentType(String type);
}
