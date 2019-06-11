<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table name="floats" id="row" requestURI="${requestURI }" pagesize="5">

	
	<acme:column property="title" titleKey="float.title" value= "${row.title}: "/>
	
	<acme:column property="brotherhood.title" titleKey="float.brotherhood" value= "${row.brotherhood.title}: "/>
	
	<acme:column property="description" titleKey="float.description" value= "${row.description}: "/>
	
	<display:column titleKey="float.pictures">
	<jstl:out value="${row.pictures }" />
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:url href="float/brotherhood/edit.do?floatId=${row.id }" code="float.edit" />
	<acme:url href="float/brotherhood/display.do?floatId=${row.id }" code="float.display" />
	</security:authorize>

	</display:table>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	<a href="float/brotherhood/create.do"><spring:message code="float.create"/></a>
	</security:authorize>
		
	<acme:button name="back" code="float.back" onclick="javascript: relativeRedir('welcome/index.do');" />




