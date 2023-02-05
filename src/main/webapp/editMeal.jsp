<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    <c:set var="title" value="${param.get('action') == 'edit' ? 'Edit meal' : 'Add meal'}"/>
    <title>${title}</title>
</head>
<body>
<div class="container" style="margin-top: 30px">
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header"><h2>${title}</h2></div>
                <div class="card-body">
                    <form class="form-floating mb-3 " method="post" action="meals"
                          enctype="application/x-www-form-urlencoded">
                        <input type="hidden" name="id" value="${meal.id}">
                        <div class="list-group">
                            <div class="d-flex w-100 justify-content-between">
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label" for="date">DateTime</label>
                                <div class="col-sm-6">
                                    <input type="datetime-local" class="form-control" name="dateTime" id="date" required
                                           value="${meal.dateTime}">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label" for="desc">Description</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control" name="description" id="desc" required
                                           value="${meal.description}">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label" for="cal">Calories</label>
                                <div class="col-sm-6">
                                    <input type="number" class="form-control" name="calories" id="cal" required
                                           value="${meal.calories}"
                                           min="1" max="2147483647">
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                        <button type="reset" onclick="window.history.back()" class="btn btn-primary">Отменить</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>