<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>

<!-- status is considered safe --> 

<core:forEach items="${someList}" var="someForm" varStatus="status">
			<tr title="row ${status.index+1}">
</core:forEach>