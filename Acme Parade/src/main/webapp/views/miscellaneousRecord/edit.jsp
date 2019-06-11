<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form modelAttribute="miscellaneousRecord" action="miscellaneousRecord/brotherhood/edit.do">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="miscellaneousRecord.title" path="title" obligatory="true"/>
	
	<acme:textbox code="miscellaneousRecord.description" path="description" obligatory="true"/>
	

	<acme:submit name="save" code="miscellaneousRecord.save" />
	
	
	<acme:cancel code="periodRecord.cancel" url="/history/brotherhood/display.do?brotherhoodId=${id}" />

	<jstl:if test="${miscellaneousRecord.id != 0}">
	<acme:submit name="delete" code="miscellaneousRecord.delete" />
	</jstl:if>

</form:form>
