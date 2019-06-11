<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table name="proclaims" id="row" requestURI="${requestURI }" pagesize="5">

	
	<acme:dateFormat titleKey="proclaim.moment" pattern="yyyy/MM/dd HH:mm" value="${row.moment}"/>

	<acme:column property="chapter.title" titleKey="proclaim.chapter" value= "${row.chapter.title}: "/>
	
	<acme:column property="description" titleKey="proclaim.description" value= "${row.description}: "/>

	
</display:table>
		
	<acme:button name="back" code="proclaim.back" onclick="javascript: relativeRedir('welcome/index.do');" />