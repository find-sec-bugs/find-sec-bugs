<%@page import="static org.apache.commons.lang.StringEscapeUtils.*" pageEncoding="UTF-8"%>

<%
    String language = (String) request.getParameter("lang");
%>

<a href="?pageId=12345&lang=<%= language %>"></a>