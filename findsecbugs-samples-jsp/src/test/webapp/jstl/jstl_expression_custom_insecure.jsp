<%@taglib prefix="w" uri="https://www.example.com/jsp/jstl/whitelist" %>
<%@taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>

${w:notSafeQuote(param.test_param)}

${w:toNotSafeJSON(requestScope.test_attribute)}

${w:notSafeQuote(unknown)}

THESE ARE _SAFE_ EXAMPLES WITH CUSTOM WHITE LIST
