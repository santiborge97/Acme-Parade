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


<form:form modelAttribute="inceptionRecord" action="inceptionRecord/brotherhood/edit.do">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="inceptionRecord.title" path="title" obligatory="true"/>
	
	<acme:textbox code="inceptionRecord.description" path="description" obligatory="true"/>
	
	<acme:textbox code="inceptionRecord.photos" size="100" pattern="^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:]{0,1})+([a-zA-Z/]+)*(,http(s*)://(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+)*$" placeholder="http://www.___.com,https://www.___.com,..." path="photos" obligatory="true"/>
	
	<jstl:if test="${inceptionRecord.id==0}">
	<acme:submit name="save1" code="inceptionRecord.save1" />
	</jstl:if>
	
	<jstl:if test="${inceptionRecord.id!=0}">
	<acme:submit name="save2" code="inceptionRecord.save2" />
	</jstl:if>

	<acme:cancel code="inceptionRecord.cancel" url="/history/brotherhood/display.do?brotherhoodId=${id2}" />


</form:form>

