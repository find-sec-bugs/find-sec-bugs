package testcode.cookie;

import jakarta.servlet.http.Cookie;

public class JakartaHttpOnlyCookieSamples {

    void safeJakartaCookie() {
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setHttpOnly(true);
    }

    void unsafeJakartaCookie1() {
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setHttpOnly(false);
    }

    void unsafeJakartaCookie2() {
        Cookie newCookie = new Cookie("test1","1234");
    }
}
