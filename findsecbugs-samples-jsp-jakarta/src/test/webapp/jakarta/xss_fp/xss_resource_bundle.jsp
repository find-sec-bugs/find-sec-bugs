<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Arrays"%>

<%
ResourceBundle bundle = (ResourceBundle) application.getAttribute("bundle");
%>

<%= request.getAttribute("TP") %>

<%= bundle.getString("description") %> <!-- FP -->
<%= Arrays.toString(bundle.getStringArray("titles")) %> <!-- FP -->
<%= bundle.getObject("description") %> <!-- FP -->

<!-- Additional test for Arrays.toString() -->
<%= Arrays.toString(new Object[] {request.getAttribute("TP")}) %>
<%= Arrays.toString(new String[] {"test"}) %> <!-- FP -->