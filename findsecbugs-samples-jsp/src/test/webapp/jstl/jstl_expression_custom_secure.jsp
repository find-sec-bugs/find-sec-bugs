<%@taglib prefix="w" uri="https://www.example.com/jsp/jstl/whitelist" %>

${w:customSafeQuote(param.test_param)}

${w:customToSafeJSON(requestScope.test_attribute)}

${w:customSafeQuote(unknown)}

ABOVE ARE _SAFE_ EXAMPLES WITH CUSTOM WHITE LIST LOADED FROM FILE
