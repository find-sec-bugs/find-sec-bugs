package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspContext;

public interface SimpleTag extends JspTag {
    
    public void doTag() 
        throws javax.servlet.jsp.JspException, java.io.IOException;
    
    public void setParent( JspTag parent );
    
    public JspTag getParent();
    
    public void setJspContext( JspContext pc );
                
}
