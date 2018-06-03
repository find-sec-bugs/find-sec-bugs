package javax.servlet.jsp;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//STUB note: directly referenced in JSPC-generated code
abstract public class JspFactory {
    public static synchronized JspFactory getDefaultFactory() { return null ; }

    public abstract PageContext getPageContext(Servlet         servlet,
                               ServletRequest  request,
                               ServletResponse response,
                               String          errorPageURL,
                               boolean         needsSession,
                               int             buffer,
                               boolean         autoflush);

    public abstract void releasePageContext(PageContext pc);

    public abstract JspApplicationContext getJspApplicationContext(
        ServletContext context);
   
}