<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<display:table name="chapters" id="row" requestURI="${requestURI }" pagesize="5">

	<acme:column property="title" titleKey="chapter.title" value= "${row.title}: "/>
	
	<acme:column property="area.name" titleKey="chapter.area" value= "${row.area.name}: "/>
	
	<jstl:if test="${row.area!= null}">
	<acme:url href="area/list.do?chapterId=${row.id }" code="chapter.area" />
	</jstl:if>
	
</display:table>

<br/>
<br/>

<acme:button name="back" code="chapter.back" onclick="javascript: relativeRedir('welcome/index.do');" />