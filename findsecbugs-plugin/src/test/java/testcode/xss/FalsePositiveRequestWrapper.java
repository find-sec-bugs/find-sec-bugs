package testcode.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Will make sure that not all HttpServletRequestWrapper are identify (numerous false positive)
 */
public class FalsePositiveRequestWrapper extends HttpServletRequestWrapper {

    public FalsePositiveRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        return super.getParameterValues(parameter);
    }

    @Override
    public String getParameter(String parameter) {
        return super.getParameter(parameter);
    }

    @Override
    public String getHeader(String name) {
        return super.getHeader(name);
    }
}
