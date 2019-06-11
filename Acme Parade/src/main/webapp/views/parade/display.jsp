<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:display property="${parade.title }" code="parade.title" />

<acme:display property="${parade.description }" code="parade.description" />

<acme:display property="${parade.maxRow }" code="parade.maxRow" />

<acme:display property="${parade.maxColumn }" code="parade.maxColumn" />

<acme:display property="${parade.brotherhood.title }" code="parade.brotherhood" />

<acme:display property="${parade.ticker }" code="parade.ticker" />

<spring:message code="parade.organisationMoment"/>: 
<fmt:formatDate value="${parade.organisationMoment}" pattern="yyyy/MM/dd HH:mm"/>
<br> 

<security:authorize access="hasRole('BROTHERHOOD')">
<acme:display property="${parade.finalMode }" code="parade.finalMode" />

<jstl:if test="${!parade.finalMode }">
<acme:button name="edit" code="parade.edit" onclick="javascript: relativeRedir('parade/brotherhood/edit.do?paradeId=${parade.id }');" />
</jstl:if>
</security:authorize>

<acme:button name="back" code="parade.back" onclick="javascript: relativeRedir('welcome/index.do');" />

<jstl:if test="${find}">
	<fieldset>
		<img src="${bannerSponsorship}" alt="Banner" width="200" height="100"/>
	</fieldset>
</jstl:if>


