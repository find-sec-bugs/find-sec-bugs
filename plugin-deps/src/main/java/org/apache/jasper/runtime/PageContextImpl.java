package org.apache.jasper.runtime;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

//STUB note: directly referenced in JSPC-generated code
public class PageContextImpl extends PageContext {
    private static final JspFactory jspf = JspFactory.getDefaultFactory();


    // directly referenced by JSPC-generated code
    public static Object proprietaryEvaluate(final String expression,
            final Class<?> expectedType, final PageContext pageContext,
            final ProtectedFunctionMapper functionMap)
            throws ELException {
        final ExpressionFactory exprFactory = jspf.getJspApplicationContext(pageContext.getServletContext()).getExpressionFactory();
        ELContext ctx = pageContext.getELContext();
        ValueExpression ve = exprFactory.createValueExpression(ctx, expression, expectedType);
        return ve.getValue(ctx);
    }

}
