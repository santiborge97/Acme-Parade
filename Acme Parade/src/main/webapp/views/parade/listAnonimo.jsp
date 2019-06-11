<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${AreInFinder }">
<security:authorize access="hasRole('MEMBER')">
<form:form action="${requestAction }" modelAttribute="finder"> 

	<form:hidden path="id"/>
	<form:hidden path="version"/>

	<acme:textbox path="keyWord" code="parade.keyWord" />
	
	<acme:textbox path="area" code="parade.area" />
	
	<acme:textbox path="minDate" code="parade.minDate" />
	
	<acme:textbox path="maxDate" code="parade.maxDate" />
	
	<input type="submit" name="find" value="<spring:message code="parade.find"/>"/>
	
</form:form> 
</security:authorize>
</jstl:if>

<br/>

<display:table name="parades" id="row" requestURI="${requestURI }" pagesize="${pagesize }">
	
	
	<acme:column property="title" titleKey="parade.title" value= "${row.title}: "/>
	
	<acme:column property="brotherhood.title" titleKey="parade.brotherhood" value= "${row.brotherhood.title} "/>
	
	<acme:column property="brotherhood.area.name" titleKey="parade.area" value="${row.brotherhood.area.name }" />
	
	<acme:column property="description" titleKey="parade.description" value= "${row.description}: "/>
	
	<acme:dateFormat titleKey="parade.organisationMoment" value="${row.organisationMoment }" pattern="yyyy/MM/dd HH:mm" />
	
	<acme:column property="status" titleKey="parade.status" value= "${row.status}: "/>

	<acme:url href="float/parade/list.do?paradeId=${row.id }" code="parade.float" />
	
	<acme:url href="parade/display.do?paradeId=${row.id }" code="parade.display" />
	
	<security:authorize access="hasRole('SPONSOR')">	
		<acme:url href="sponsorship/sponsor/sponsor.do?paradeId=${row.id}" code="parade.sponsor" />
	</security:authorize>

</display:table>
	
	<br/>
	
	<acme:button name="back" code="parade.back" onclick="javascript: relativeRedir('welcome/index.do');" />
	
<script type="text/javascript">
	var trTags = document.getElementsByTagName("tr");
	for (var i = 1; i < trTags.length; i++) {
	  var tdStatus = trTags[i].children[5];
	  if (tdStatus.innerText == "REJECTED") {
		  trTags[i].style.backgroundColor = "red";
	  } else if (tdStatus.innerText == "ACCEPTED") {
		  trTags[i].style.backgroundColor = "green";
	  } else{
		  trTags[i].style.backgroundColor = "grey";
	  }
	}
</script>