package testcode.xss;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;

public class CustomXssInjection {
    
    public void testInjection(String inputParameter) throws SQLException {
        XssInjectable.render(inputParameter);
        XssInjectable.render(StringEscapeUtils.escapeHtml4(inputParameter));
        XssInjectable.render("<a>" + inputParameter + "</a>");
    }
}

class XssInjectable {

    static void render(String html) {
    }
}
