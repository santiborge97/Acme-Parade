<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Search Parade --%>

<display:table name="parades" id="row" requestURI="${requestURI }" pagesize="5">
	
	
	<acme:column property="title" titleKey="parade.title" value= "${row.title}: "/>
	
	<acme:column property="brotherhood.title" titleKey="parade.brotherhood" value= "${row.brotherhood.title} "/>
	
	<acme:column property="brotherhood.area.name" titleKey="parade.area" value="${row.brotherhood.area.name }" />
	
	<acme:column property="description" titleKey="parade.description" value= "${row.description}: "/>
	
	<acme:dateFormat titleKey="parade.organisationMoment" value="${row.organisationMoment }" pattern="yyyy/MM/dd HH:mm" />
	
	<acme:column property="status" titleKey="parade.status" value= "${row.status}: "/>
	
	<acme:column property="rejectedComment" titleKey="parade.rejectedComment" value= "${row.rejectedComment}: "/>
		
	<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:column property="ticker" titleKey="parade.ticker" value= "${row.ticker}: "/>
	</security:authorize> 
	
	<jstl:if test="${autoridad == 'brotherhood'}">
		<acme:column property="finalMode" titleKey="parade.finalMode" value="${row.finalMode }" />
	</jstl:if>

	
	<%-- <security:authorize access="isAnonymous()"> 
	<acme:url href="float/parade/list.do?paradeId=${row.id }" code="parade.float" />
 	</security:authorize> --%>
	
	<security:authorize access="hasRole('CHAPTER')">
		<jstl:if test="${row.status == 'SUBMITTED'}">	
			<acme:url href="parade/chapter/accept.do?paradeId=${row.id }" code="parade.accept" />
			<acme:url href="parade/chapter/reject.do?paradeId=${row.id }" code="parade.reject" />
		</jstl:if>
	</security:authorize>
		
	<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:url href="parade/brotherhood/display.do?paradeId=${row.id }" code="parade.display" />
	<acme:url href="float/brotherhood/listByParade.do?paradeId=${row.id }" code="parade.floats" />
	<acme:url href="float/brotherhood/floatAddParade.do?paradeId=${row.id }" code="parade.addFloat" />	
	<acme:url href="segment/brotherhood/path.do?paradeId=${row.id }" code="parade.path" />
	<acme:url href="parade/brotherhood/copy.do?paradeId=${row.id }" code="parade.copy" />
	<div></div>
	</security:authorize> 

</display:table>
	<security:authorize access="hasRole('BROTHERHOOD')">
	<a href="parade/brotherhood/create.do"><spring:message code="parade.create"/></a>
	</security:authorize>
	
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