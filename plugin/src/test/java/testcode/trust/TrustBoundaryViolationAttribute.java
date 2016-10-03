package testcode.trust;

import javax.servlet.http.HttpServletRequest;

public class TrustBoundaryViolationAttribute {

    //Taint input

    public void setSessionAttributeNameTainted(HttpServletRequest req) {
        String input = req.getParameter("input");
        req.getSession().setAttribute(input,"true");
    }

    public void setSessionAttributeValueTainted(HttpServletRequest req) {
        String input = req.getParameter("input");
        req.getSession().setAttribute("user",input);
    }

    //Unknown source

    public void setSessionAttributeNameUnknownSource(HttpServletRequest req, String input) {
        req.getSession().setAttribute(input,"true");
    }

    public void setSessionAttributeValueUnknownSource(HttpServletRequest req, String input) {
        req.getSession().setAttribute("user",input);
    }

    //Legacy api

    public void setSessionAttributeNameUnknownSourceLegacy(HttpServletRequest req, String input) {
        req.getSession().putValue(input, "true");
    }

    public void setSessionAttributeValueUnknownSourceLegacy(HttpServletRequest req, String input) {
        req.getSession().putValue("user",input);
    }

    //Safe

    public void setSessionAttributeSafe(HttpServletRequest req, String input) {
        if("enable".equals(input)) {
            req.getSession().setAttribute("user", "true");
        }
        else {
            req.getSession().setAttribute("user","false");
        }
    }
}
