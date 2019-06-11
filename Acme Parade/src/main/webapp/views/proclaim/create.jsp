<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="proclaim/chapter/create.do" modelAttribute="proclaim">
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textarea code="proclaim.description" path="description" obligatory="true"/>
	

	<input type="submit" name="save" value="<spring:message code="proclaim.save"/>" onclick="return confirm('<spring:message code="proclaim.saveConfirm" />')"/>
	
	<acme:cancel code="proclaim.cancel" url="welcome/index.do" />

</form:form>    