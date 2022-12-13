<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>
<%@taglib prefix="w" uri="https://www.example.com/jsp/jstl/whitelist" %>
<%@taglib prefix="xy" uri="https://www.example.com/jsp/jstl/whitelist" %>


${e:forHtmlContent(param.test_param)}

<c:set var = "someVariable1" value = "Some value"/> <!-- The constant value is not tracked. -->

${e:forHtmlContent(someVariable1)} <!-- Currently only based on the escaping function -->

${pageContext.request.contextPath}

${w:safeQuote(param.test_param)}

${w:toSafeJSON(requestScope.test_attribute)}

${xy:toSafeJSON(requestScope.test_attribute)}

${w:safeQuote(unknown)}

${xy:safeQuote(unknown)}

THESE ARE _SAFE_ EXAMPLES