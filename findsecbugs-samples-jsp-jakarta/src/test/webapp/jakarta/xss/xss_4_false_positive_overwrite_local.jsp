<%
    String taintedInput1 = (String) request.getAttribute("input1");
    String dummyValue = "123456";
%>

<%
    taintedInput1 = "test"; //What if the store is conditional..
%>

<%= taintedInput1 %>