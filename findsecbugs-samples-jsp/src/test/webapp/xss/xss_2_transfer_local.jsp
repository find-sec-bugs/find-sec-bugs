<%@page import="org.owasp.esapi.ESAPI" pageEncoding="UTF-8"%>
<%
    Object taintedInput = request.getAttribute("input");
    //Tainted input transfer to another local variable
    String castTaintedInput = (String) taintedInput;
%>


<%=
castTaintedInput
%>

<%=
ESAPI.encoder().encodeForHTML(castTaintedInput)
%>