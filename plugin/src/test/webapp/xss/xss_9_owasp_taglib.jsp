<%@taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

<%
    String tainted = (String) request.getAttribute("input");
%>

<!-- safe -->
<e:forHtml value="${tainted}" />

<!-- currently a false positive -->
${e:forHtml(tainted)}
