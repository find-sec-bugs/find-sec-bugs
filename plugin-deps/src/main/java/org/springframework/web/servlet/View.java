package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {

    String getContentType();

    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
