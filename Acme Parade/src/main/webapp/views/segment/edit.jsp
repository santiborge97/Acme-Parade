<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="segment/brotherhood/edit.do" modelAttribute="segment">
	
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<jstl:if test="${!edit}">
		<form:hidden path="paradeId" />
	</jstl:if>	
	
	<h3><spring:message code="segment.date"/> ${moment}</h3>
	<br/>
	
	<jstl:if test="${complete}">
		<acme:textbox path="origin" code="segment.origin" placeholder="-90,180" obligatory="true"/>
	</jstl:if>	
	
	<acme:textbox path="destination" code="segment.destination" placeholder="90,-180"  obligatory="true"/>
	
	<jstl:if test="${complete}">
		<acme:textbox path="timeOrigin" code="segment.timeOrigin" placeholder="yyyy/MM/dd hh:mm" obligatory="true"/>
	</jstl:if>	
	
	<acme:textbox path="timeDestination" code="segment.timeDestination" placeholder="yyyy/MM/dd hh:mm" obligatory="true"/>
  	
			
	<acme:submit name="${name}" code="segment.save" />	

	<acme:cancel code="parade.cancel" url="parade/brotherhood/list.do" />
	
	<jstl:if test="${segment.id != 0 and delete}">
	<acme:delete confirmation="segment.confirm.delete" code="segment.delete" />
	</jstl:if>	
	

</form:form>