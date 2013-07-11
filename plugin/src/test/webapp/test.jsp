<%@page import="static org.apache.commons.lang.StringEscapeUtils.*" pageEncoding="UTF-8" %>

<%
    String evilInput = (String) request.getAttribute("input");
%>


<%
    out.print(evilInput);
    out.print(escapeHtml(evilInput));
%>