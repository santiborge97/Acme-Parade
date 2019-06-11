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


<form:form modelAttribute="periodRecord" action="periodRecord/brotherhood/edit.do">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="periodRecord.title" path="title" obligatory="true"/>
	
	<acme:textbox code="periodRecord.description" path="description" obligatory="true"/>
	
	<acme:textbox code="periodRecord.startYear" path="startYear" obligatory="true"/>
	
	<acme:textbox code="periodRecord.endYear" path="endYear" obligatory="true"/>
	
	<acme:textbox code="periodRecord.photos" size="100"  pattern="^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:]{0,1})+([a-zA-Z/]+)*(,http(s*)://(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+)*$" placeholder="http://www.___.com,https://www.___.com,..." path="photos" obligatory="false"/>
	
	<acme:submit name="save" code="periodRecord.save" />
		
	<acme:cancel code="periodRecord.cancel" url="/history/brotherhood/display.do?brotherhoodId=${id}" />


	<jstl:if test="${periodRecord.id != 0}">
	<acme:submit name="delete" code="periodRecord.delete" />
	</jstl:if>

</form:form>