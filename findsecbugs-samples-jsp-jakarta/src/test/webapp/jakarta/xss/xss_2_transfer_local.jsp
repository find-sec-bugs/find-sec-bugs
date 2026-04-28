<%@page pageEncoding="UTF-8"%>
<%
    Object taintedInput = request.getAttribute("input");
    //Tainted input transfer to another local variable
    String castTaintedInput = (String) taintedInput;
%>


<%=
castTaintedInput
%>

<%=
"safe string"
%>
