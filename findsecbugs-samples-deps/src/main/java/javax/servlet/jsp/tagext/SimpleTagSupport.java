package javax.servlet.jsp.tagext;

import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;

public class SimpleTagSupport  {
    
    JspContext jspContext ;
    
    
    
    public void doTag() throws JspException, IOException {
    }
    
    
    protected JspContext getJspContext() {
        return jspContext ;
    }
    
    public void setJspContext(JspContext jspContext) {
        this.jspContext = jspContext ;
    }

}
