<%!
    static String getDummyValue() {
        return "";
    }
%>

<%
    String safeValue1 = getDummyValue();
    String taintedInput1 = (String) request.getAttribute("input1");
    String safeValue2 = getDummyValue();
    Object taintedInput2 = request.getAttribute("input2");
    String safeValue3 = getDummyValue();
%>

<%= safeValue1 %>
<%= safeValue2 %>
<%= safeValue3 %>