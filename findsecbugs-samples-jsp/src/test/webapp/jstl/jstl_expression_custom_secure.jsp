<%@taglib prefix="w" uri="https://www.example.com/jsp/jstl/whitelist" %>
<%@taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

${w:safeQuote(param.test_param)}

${w:toSafeJSON(requestScope.test_attribute)}

${w:safeQuote(unknown)}

THESE ARE _SAFE_ EXAMPLES WITH CUSTOM WHITE LIST
