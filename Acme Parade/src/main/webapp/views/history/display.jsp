<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:display code="history.inceptionRecord" property=""/>
<fieldset>
<div>

<security:authorize access="hasRole('BROTHERHOOD')">
<jstl:if test="${history == null and owner}">
	<acme:button name="create" code="inceptionRecord.create" onclick="javascript: relativeRedir('inceptionRecord/brotherhood/create.do');"/>
	</jstl:if>
</security:authorize>

<jstl:if test="${history != null}">
<acme:display code="history.inceptionRecord.title" property="${history.inceptionRecord.title }" />

<acme:display code="history.inceptionRecord.description" property="${history.inceptionRecord.description }" />

<spring:message code="history.inceptionRecord.photos" />: <jstl:out value="${history.inceptionRecord.photos }"></jstl:out><br>



</jstl:if>

<security:authorize access="hasRole('BROTHERHOOD')">
<jstl:if test="${history.id!=0 and history != null and owner}">
	
	<acme:button name="edit" code="inceptionRecord.edit" onclick="javascript: relativeRedir('inceptionRecord/brotherhood/edit.do?inceptionRecordId=${history.inceptionRecord.id}');"/>
	</jstl:if>
</security:authorize>
</div>
</fieldset>


<acme:display code="history.legalRecords" property=""/>
<fieldset>
<div>
<display:table pagesize="5" name="history.legalRecords" id="row1" 
requestURI="${requestURI }" class="displaytag">

	<acme:column property="title" titleKey="legalRecord.title" value= "${row1.title}: "/>
	
	<acme:column property="description" titleKey="legalRecord.description" value= "${row1.description}: "/>
	
	<acme:column property="legalName" titleKey="legalRecord.legalName" value= "${row1.legalName}: "/>
	
	<acme:column property="VATNumber" titleKey="legalRecord.VATNumber" value= "${row1.VATNumber}: "/>
	
	<display:column titleKey="legalRecord.laws">
	<jstl:out value="${row1.laws }"></jstl:out>
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${owner }">
	<acme:url href="legalRecord/brotherhood/edit.do?legalRecordId=${row1.id}" code="legalRecord.edit"/>
	</jstl:if>
	</security:authorize>	
</display:table>
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${owner }">
	<acme:button name="create" code="legalRecord.create" onclick="javascript: relativeRedir('legalRecord/brotherhood/create.do');"/>
	</jstl:if>
	</security:authorize>
</div>
</fieldset>


<acme:display code="history.periodRecords" property=""/>
<fieldset>
<div>
<display:table pagesize="5" name="history.periodRecords" id="row2" 
requestURI="${requestURI }" class="displaytag">

	<acme:column property="title" titleKey="periodRecord.title" value= "${row2.title}: "/>
	
	<acme:column property="description" titleKey="periodRecord.description" value= "${row2.description}: "/>
	
	<acme:dateFormat titleKey="periodRecord.startYear" pattern="yyyy/MM/dd" value="${row2.startYear}"/>
	
	<acme:dateFormat titleKey="periodRecord.endYear" pattern="yyyy/MM/dd" value="${row2.endYear}"/>
	
	<display:column titleKey="periodRecord.photos">
	<jstl:out value="${row2.photos }"></jstl:out>
	</display:column>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${owner }">
	<acme:url href="periodRecord/brotherhood/edit.do?periodRecordId=${row2.id}" code="periodRecord.edit"/>
	</jstl:if>
	</security:authorize>	
</display:table>
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${owner }">
	<acme:button name="create" code="periodRecord.create" onclick="javascript: relativeRedir('periodRecord/brotherhood/create.do');"/>
	</jstl:if>
	</security:authorize>
</div>
</fieldset>

<acme:display code="history.linkRecords" property=""/>
<fieldset>
<div>
<display:table pagesize="5" name="history.linkRecords" id="row3" 
requestURI="${requestURI }" class="displaytag">

	<acme:column property="title" titleKey="linkRecord.title" value= "${row3.title}: "/>
	
	<acme:column property="description" titleKey="linkRecord.description" value= "${row3.description}: "/>
	
	<acme:column property="link" titleKey="linkRecord.link" value= "${row3.link}: "/>
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${owner }">	
	<acme:url href="linkRecord/brotherhood/edit.do?linkRecordId=${row3.id}" code="linkRecord.edit"/>
	</jstl:if>
	</security:authorize>	
</display:table>
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${owner }">
	<acme:button name="create" code="linkRecord.create" onclick="javascript: relativeRedir('linkRecord/brotherhood/create.do');"/>
	</jstl:if>
	</security:authorize>
</div>
</fieldset>


<acme:display code="history.miscellaneousRecords" property=""/>
<fieldset>
<div>
<display:table pagesize="5" name="history.miscellaneousRecords" id="row4" 
requestURI="${requestURI }" class="displaytag">

	<acme:column property="title" titleKey="miscellaneousRecord.title" value= "${row4.title}: "/>
	
	<acme:column property="description" titleKey="miscellaneousRecord.description" value= "${row4.description}: "/>
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${owner }">
	<acme:url href="miscellaneousRecord/brotherhood/edit.do?miscellaneousRecordId=${row4.id}" code="miscellaneousRecord.edit"/>
	</jstl:if>
	</security:authorize>
</display:table>
	<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${owner }">
	<acme:button name="create" code="miscellaneousRecord.create" onclick="javascript: relativeRedir('miscellaneousRecord/brotherhood/create.do');"/>
	</jstl:if>
	</security:authorize>
</div>
</fieldset>

<acme:cancel code="history.back" url="/brotherhood/list.do" />