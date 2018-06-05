package org.springframework.boot.autoconfigure.web;

import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

public interface ErrorAttributes {

    Map<String, Object> getErrorAttributes(RequestAttributes var1, boolean var2);

    Throwable getError(RequestAttributes var1);
}
