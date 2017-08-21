<%--
    Definitions are inside /src/test/resources/com/h3xstream/findsecbugs/xss/CustomConfig.txt
--%>

<%
    //Let's say it's safe: javax/servlet/http/HttpServletRequest.getAttribute("safe"):SAFE@org/apache/jsp/xss/xss_005f8_005frequest_005fattribute_jsp
    String safe = (String) request.getAttribute("safe");

    //Let's say this is tainted: javax/servlet/http/HttpServletRequest.getAttribute("tainted"):TAINTED@org/apache/jsp/xss/xss_005f8_005frequest_005fattribute_jsp
    String tainted = (String) request.getAttribute("tainted");
%>

<%= safe %>
<%= request.getAttribute("safe") %>

<%= tainted %>
<%= request.getAttribute("tainted") %>

<%
    // TEST unknown

    String variableWithUnknownTaint = (String) request.getAttribute("example");

    // javax/servlet/http/HttpSession.getAttribute(UNKNOWN):SAFE@org/apache/jsp/xss/xss_005f8_005frequest_005fattribute_jsp
    String safeExampleObject = (String) request.getSession().getAttribute(variableWithUnknownTaint);
    out.println(safeExampleObject);
%>

<%
    // TEST multiple arguments

    String fileName = tainted + ".ext";

    // org/apache/jsp/xss/xss_005f8_005frequest_005fattribute_jsp.suffix(TAINTED,SAFE,"_suffix"):SAFE@org/apache/jsp/xss/xss_005f8_005frequest_005fattribute_jsp
    String extensionWithSuffix = suffix(fileName, 3, "_suffix");

    out.println(extensionWithSuffix);
%>

<%!
    String suffix(String s, int length, String suffix) {
        return s.substring(s.length() - length, s.length()) + suffix;
    }
%>