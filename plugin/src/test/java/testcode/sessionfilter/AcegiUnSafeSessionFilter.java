package testcode.sessionfilter;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.HttpSessionContextIntegrationFilter;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AcegiUnSafeSessionFilter extends HttpSessionContextIntegrationFilter {
    public AcegiUnSafeSessionFilter() throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Authentication old = SecurityContextHolder.getContext().getAuthentication();

        if(1 + 1 == 2) {
            try {
                SecurityContextHolder.getContext().setAuthentication(null);
                super.doFilter(req, res, chain);
            } finally {
                SecurityContextHolder.getContext().setAuthentication(old);
            }
        }
        else {
            super.doFilter(req, res, chain);
        }
    }
}
