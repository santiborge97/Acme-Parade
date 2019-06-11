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

<form:form action="request/brotherhood/accept.do" modelAttribute="request">

<form:hidden path="id" />
<form:hidden path="version" />

<acme:display code="request.parade" property="${request.parade.title}" />

<acme:display code="request.status" property="${request.status}" />

<acme:display code="request.member.name" property="${request.member.name}" />

<acme:display code="request.member.surname" property="${request.member.surname}" />

<acme:textbox path="columnNumber" code="request.columnNumber" />

<acme:textbox path="rowNumber" code="request.rowNumber" />

<acme:submit name="accept" code="request.accept" />

<acme:button name="back" code="back" onclick="javascript: relativeRedir('request/brotherhood/listPending.do');" />

</form:form>

