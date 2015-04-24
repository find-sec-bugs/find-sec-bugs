package testcode.sessionfilter;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SpringSafeSessionFilter extends RequestContextFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        ServletRequestAttributes attributes = new ServletRequestAttributes(req, res);

        try {

            if(1 + 1 == 2) {

                SecurityContext oldCtx = SecurityContextHolder.getContext();
                SecurityContextHolder.setContext(null); //
                try {
                    super.doFilter(req, res, chain);
                } finally {
                    SecurityContextHolder.setContext(oldCtx);
                }
            }
            else {
                super.doFilter(req, res, chain);
            }
        }
        finally {
            attributes.requestCompleted();
        }
    }
}
