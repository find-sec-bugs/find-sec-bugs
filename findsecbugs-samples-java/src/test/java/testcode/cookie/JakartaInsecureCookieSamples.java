package testcode.cookie;

import jakarta.servlet.http.Cookie;

public class JakartaInsecureCookieSamples {

    void safeJakartaCookie() {
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setSecure(true);
    }

    void unsafeJakartaCookie1() {
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setSecure(false);
    }

    void unsafeJakartaCookie2() {
        Cookie newCookie = new Cookie("test1","1234");
    }
}
