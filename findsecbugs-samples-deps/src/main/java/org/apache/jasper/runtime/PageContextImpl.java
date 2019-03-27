package org.apache.jasper.runtime;

import javax.el.ELException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

//STUB note: directly referenced in JSPC-generated code
public class PageContextImpl extends PageContext {

    private ServletConfig config;

    private ServletContext context;
    PageContextImpl() { }

    public ServletConfig getServletConfig() {
        return config;
    }

    public ServletContext getServletContext() {
        return config.getServletContext();
    }

    // directly referenced by JSPC-generated code
    public static Object proprietaryEvaluate(final String expression,
            final Class<?> expectedType, final PageContext pageContext,
            final ProtectedFunctionMapper functionMap)
            throws ELException {
        return "" ;
    }

}
