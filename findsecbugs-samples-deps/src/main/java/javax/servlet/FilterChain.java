package javax.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FilterChain {

    void doFilter(HttpServletRequest request, HttpServletResponse response);
}
