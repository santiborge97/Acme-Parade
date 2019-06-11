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

<acme:display code="segment.origin" property="${segment.origin}" />

<acme:display code="segment.destination" property="${segment.destination}" />

<acme:display code="segment.timeOrigin" property="${segment.timeOrigin}" />

<acme:display code="segment.timeDestination" property="${segment.timeDestination}" />

<acme:display code="segment.parade" property="${segment.parade.title}" />

<acme:button name="back" code="back" onclick="javascript: relativeRedir('segment/brotherhood/path.do?paradeId=${segment.parade.id}')" />