<%@taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<%
    String tainted = (String) request.getAttribute("input");
%>

<e:forHtml value="${tainted}" />

