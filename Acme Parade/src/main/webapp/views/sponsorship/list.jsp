<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table name="sponsorships" id="row" requestURI="${requestURI }" pagesize="5">

	
	<acme:column property="banner" titleKey="sponsorship.banner" value= "${row.banner}: "/>
	
	<acme:column property="targetUrl" titleKey="sponsorship.targetUrl" value= "${row.targetUrl}: "/>
	
	<acme:column property="cost" titleKey="sponsorship.cost" value= "${row.cost}: "/>
	
	<acme:column property="parade.title" titleKey="sponsorship.parade.title" value= "${row.parade.title}: "/>
	
	<acme:column property="creditCard.number" titleKey="sponsorship.creditCard.number" value= "${row.creditCard.number}: "/>
	
	<acme:column property="activated" titleKey="sponsorship.activated" value= "${row.activated}: "/>
		
	<acme:url href="sponsorship/sponsor/edit.do?sponsorshipId=${row.id }" code="sponsorship.edit" />
	
	<acme:url href="sponsorship/sponsor/display.do?sponsorshipId=${row.id }" code="sponsorship.display" />

	</display:table>
		
	<acme:button name="back" code="float.back" onclick="javascript: relativeRedir('welcome/index.do');" />




