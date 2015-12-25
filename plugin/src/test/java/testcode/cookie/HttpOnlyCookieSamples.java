package testcode.cookie;

import javax.servlet.http.Cookie;

/**
 * This class is a replica of InsecureCookieSamples class
 */
public class HttpOnlyCookieSamples {

    private static final boolean CONST_TRUE = true;
    private static final boolean CONST_FALSE = false;

    void unsafeCookie1() {
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setHttpOnly(false);
    }

    void unsafeCookie2() {
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setHttpOnly(CONST_FALSE);
    }

    void unsafeCookie3(Cookie cookieOther) {
        Cookie newCookie = new Cookie("test1","1234");
        cookieOther.setHttpOnly(true); //Unrelated
    }

    void unsafeCookie4() {
        boolean unsafe = false;
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setHttpOnly(unsafe);
    }

    void unsafeCookie5() {
        Cookie newCookie = new Cookie("test1","1234");
    }


    void safeCookie1() {
        Cookie cookie = new Cookie("test1","1234");
        cookie.setHttpOnly(true);
    }

    void safeCookie2() {
        Cookie cookie = new Cookie("test1","1234");
        cookie.setHttpOnly(CONST_TRUE);
    }

    void safeCookie3() {
        boolean safe = true;
        Cookie cookie = new Cookie("test1","1234");
        cookie.setHttpOnly(safe);
    }


    void safeCookie4() {
        boolean safe = true;
        Cookie cookie = new Cookie("test1","1234");
        cookie.setSecure(false);
        cookie.setHttpOnly(safe);
        cookie.setSecure(false);
    }

    void safeCookie5(Cookie cookieOther) {
        Cookie newCookie = new Cookie("test1","1234");
        cookieOther.setHttpOnly(false); //Unrelated
    }
}
