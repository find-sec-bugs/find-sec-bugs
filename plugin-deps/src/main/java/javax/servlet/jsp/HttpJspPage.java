package javax.servlet.jsp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpJspPage extends JspPage {

    public void _jspService(HttpServletRequest request,
                            HttpServletResponse response)
       throws ServletException, IOException;
}
