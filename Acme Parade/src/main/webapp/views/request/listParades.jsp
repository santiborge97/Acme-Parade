<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table name="parades" id="row" requestURI="${requestURI }" pagesize="5">
	
	<acme:column property="title" titleKey="parade.title" value= "${row.title}: "/>
	
	<acme:column property="brotherhood.title" titleKey="parade.brotherhood" value= "${row.brotherhood.title} "/>
	
	<acme:column property="description" titleKey="parade.description" value= "${row.description}: "/>
	
	<acme:dateFormat titleKey="parade.organisationMoment" value="${row.organisationMoment }" pattern="yyyy/MM/dd HH:mm" />
	
	<acme:url href="request/member/march.do?paradeId=${row.id }" code="request.march" />


</display:table>

	<acme:button name="back" code="parade.back" onclick="javascript: relativeRedir('welcome/index.do');" />