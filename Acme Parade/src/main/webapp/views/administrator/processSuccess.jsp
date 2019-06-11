<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<jstl:if test="${numOfDeactivations > 0}">
<p><spring:message code="sponsorship.success.begin" /> <jstl:out value="${numOfDeactivations}" /> <spring:message code="sponsorship.success.end" /></p>

</jstl:if>

<jstl:if test="${numOfDeactivations == 0}">
<p><spring:message code="sponsorship.success.empty" /></p>

<acme:button name="back" code="actor.back" onclick="javascript: relativeRedir('welcome/index.do');" />

</jstl:if>