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

    // If you add unsafe calls in this method, you must change the CookieFlagsDetectorTest - It is validated with the
    // times(X) annotation
    void multipleCookies() {
        Cookie safeSecureCookie = new Cookie("cookie 3", "foo");
        safeSecureCookie.setSecure(true);

        // The line bellow should stay line 72 - It is used with the .atLine() annotation in the test
        Cookie unsafeSecureCookie = new Cookie("cookie 4", "bar");
        unsafeSecureCookie.setSecure(false);

        // The line bellow should stay line 76 - It is used with the .atLine() annotation in the test
        Cookie unsafeCookie = new Cookie("cookie 3", "foo");

        Cookie mixedCookiesSafe = new Cookie("cookie 4", "bar");
        // The line bellow should stay line 76 - It is used with the .atLine() annotation in the test
        Cookie mixedCookies = new Cookie("cookie 5", "bar");
        mixedCookiesSafe.setSecure(true);

        // The line bellow should stay line 84 - It is used with the .atLine() annotation in the test
        Cookie unsafeCookie2 = new Cookie("c1", "foo");
        unsafeCookie2.setSecure(false);

        Cookie safeCookie2 = new Cookie("c2", "bar");
        safeCookie2.setSecure(true);
    }
}
