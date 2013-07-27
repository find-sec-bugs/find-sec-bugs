<%@page import="static org.apache.commons.lang.StringEscapeUtils.*" pageEncoding="UTF-8" %>

<%
    String evilInput = (String) request.getAttribute("input");
%>


<%
//XSS
out.print(evilInput);
//OK
out.print(escapeHtml(evilInput));
%>

<%= evilInput  %>

<%= escapeHtml(evilInput) %>