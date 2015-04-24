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
import java.io.IOException;

public class AcegiSafeSessionFilter extends HttpSessionContextIntegrationFilter {
    public AcegiSafeSessionFilter() throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {


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
}
