<%
    String safeValue1 = "safe1";
    String taintedInput1 = (String) request.getAttribute("input1");
    String safeValue2 = "safe2";
    Object taintedInput2 = request.getAttribute("input2");
    String safeValue3 = "safe3";
%>

<%= safeValue1 %>
<%= safeValue2 %>
<%= safeValue3 %>