<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>




<form:form action="parade/chapter/reject.do"
	modelAttribute="parade">
	
	<form:hidden path="id" />
	<form:hidden path="version" /> 
	
	<acme:textbox path="rejectedComment" code="parade.rejectedComment"/>
	
	<br />	

	<acme:submit name="saveReject" code ="parade.save" />
	
	<acme:button name="cancel" code="parade.cancel" onclick="javascript: relativeRedir('parade/chapter/list.do');"/>

</form:form>