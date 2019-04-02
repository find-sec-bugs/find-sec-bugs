<%@page import="org.owasp.esapi.ESAPI" pageEncoding="UTF-8"%>
<%
    Object taintedInput = request.getAttribute("input");
    //Tainted input transfer to another local variable
    String castTaintedInput = (String) taintedInput;
    String transfert1 = castTaintedInput;
    String transfert2 = transfert1;
    String transfert3 = transfert2;
%>


<%=
transfert3
%>

<%=
ESAPI.encoder().encodeForHTML(transfert3)
%>