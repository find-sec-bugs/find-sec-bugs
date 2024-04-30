package testcode.cookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JakartaPersistentCookie extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        if(email != null && !email.equals("")) {
            setCookieFor1Week(email); //OK
            setCookieFor1DayUnitConfusion(email); //BAD
            setCookieFor1Year(email); //BAD
        }
    }

    //3600 seconds == 1 hour
    private void setCookieFor1Week(String email) { //Reasonable duration. No warning will be trigger
        Cookie cookie = new Cookie("emailCookie", email);
        cookie.setMaxAge(3600*24*7);
    }

    private void setCookieFor1DayUnitConfusion(String email) { //Example of unit confusion (sec. not ms.)
        Cookie cookie = new Cookie("emailCookie", email);
        cookie.setMaxAge(1000*3600*24); //Expect 24 hour, in fact 1000 day
    }

    private void setCookieFor1Year(String email) { //More than one year will be mark as potentially dangerous/unwanted
        Cookie cookie = new Cookie("emailCookie", email);
        cookie.setMaxAge(3600*24*365);
    }
}
