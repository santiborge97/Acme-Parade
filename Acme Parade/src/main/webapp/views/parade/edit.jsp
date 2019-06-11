<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="parade/brotherhood/edit.do" modelAttribute="parade">
	
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	
	<acme:textbox path="title" code="parade.title" obligatory="true"/>
	
	<acme:textbox path="description" code="parade.description" obligatory="true"/>
	
	<acme:textbox path="maxRow" code="parade.maxRow" obligatory="true"/>
	
	<acme:textbox path="maxColumn" code="parade.maxColumn" obligatory="true"/>
	
	<acme:textbox path="organisationMoment" code="parade.organisationMoment" placeholder="yyyy/MM/dd hh:mm" obligatory="true"/>
  	
  	<acme:choose path="finalMode" code="parade.finalMode" value1="true" value2="false" label1="Final" label2="No Final" />
			
	<acme:submit name="save" code="parade.save" />	

	<acme:cancel code="parade.cancel" url="parade/brotherhood/list.do" />
	
	<jstl:if test="${parade.id != 0}">
	<acme:delete confirmation="parade.confirm.delete" code="parade.delete" />
	</jstl:if>	
	

</form:form>    