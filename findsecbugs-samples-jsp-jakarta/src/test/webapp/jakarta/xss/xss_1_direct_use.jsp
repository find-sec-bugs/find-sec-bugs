<%@page pageEncoding="UTF-8" %>

<%
    String evilInput = (String) request.getAttribute("input");
%>


<%
//XSS
out.print(evilInput);
//OK - safe constant
out.print("safe string");
%>

<%= evilInput  %>

<%= "safe string" %>
