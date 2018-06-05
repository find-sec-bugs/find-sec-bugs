package org.springframework.web.context.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestAttributes implements RequestAttributes {

    public ServletRequestAttributes(HttpServletRequest request) {

    }

    public ServletRequestAttributes(HttpServletRequest req, HttpServletResponse res) {

    }

    public void requestCompleted() {

    }
}
