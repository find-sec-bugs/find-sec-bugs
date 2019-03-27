package javax.servlet.jsp;

import javax.el.ELResolver;
import javax.el.ExpressionFactory;
//STUB note: indirectly referenced in JSPC-generated code
public interface JspApplicationContext {
    
    public void addELResolver(ELResolver resolver);
    
    public ExpressionFactory getExpressionFactory();
    
}
