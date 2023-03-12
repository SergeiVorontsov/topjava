<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<spring:message code="meal.title.form.create" scope="request" var="create"/>
<spring:message code="meal.title.form.edit" scope="request" var="edit"/>
<spring:message code="meal.dateTime" scope="request" var="dateTime"/>
<spring:message code="meal.description" scope="request" var="description"/>
<spring:message code="meal.calories" scope="request" var="calories"/>
<spring:message code="common.cancel" scope="request" var="cancel"/>
<spring:message code="common.save" scope="request" var="save"/>
<section>
    <h3>${requestScope.get("org.springframework.web.util.UrlPathHelper.PATH") == "/meals/create" ? create : edit}</h3>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="save">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>${dateTime}</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt>${description}:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt>${calories}:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit">${save}</button>
        <button onclick="window.history.back()" type="button">${cancel}</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>