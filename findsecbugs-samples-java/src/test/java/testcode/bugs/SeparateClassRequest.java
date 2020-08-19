package testcode.bugs;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class SeparateClassRequest {
    private HttpServletRequest request;


    public SeparateClassRequest( HttpServletRequest request ) {
        this.request = request;
    }

    public String getTheParameter(String p) {
        return request.getParameter(p);
    }

    public String getTheCookie(String c) {
        Cookie[] cookies = request.getCookies();

        String value = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(c)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }

        return value;
    }

    // This method is a 'safe' source.
    public String getTheValue(String p) {
        return "bar";
    }
}
