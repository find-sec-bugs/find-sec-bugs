package testcode.cookie;

import javax.servlet.http.Cookie;

public class InsecureCookieSamples {

    private static final boolean CONST_TRUE = true;
    private static final boolean CONST_FALSE = false;

    void unsafeCookie1() {
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setSecure(false);
    }

    void unsafeCookie2() {
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setSecure(CONST_FALSE);
    }

    void unsafeCookie3(Cookie cookieOther) {
        Cookie newCookie = new Cookie("test1","1234");
        cookieOther.setSecure(true); //Unrelated
    }

    void unsafeCookie4() {
        boolean unsafe = false;
        Cookie newCookie = new Cookie("test1","1234");
        newCookie.setSecure(unsafe);
    }

    void unsafeCookie5() {
        Cookie newCookie = new Cookie("test1","1234");
    }


    void safeCookie1() {
        Cookie cookie = new Cookie("test1","1234");
        cookie.setSecure(true);
    }

    void safeCookie2() {
        Cookie cookie = new Cookie("test1","1234");
        cookie.setSecure(CONST_TRUE);
    }

    void safeCookie3() {
        boolean safe = true;
        Cookie cookie = new Cookie("test1","1234");
        cookie.setSecure(safe);
    }


    void safeCookie4() {
        boolean safe = true;
        Cookie cookie = new Cookie("test1","1234");
        cookie.setHttpOnly(false);
        cookie.setSecure(safe);
        cookie.setHttpOnly(false);
    }

    void safeCookie5(Cookie cookieOther) {
        Cookie newCookie = new Cookie("test1","1234");
        cookieOther.setSecure(false); //Unrelated
    }

}
