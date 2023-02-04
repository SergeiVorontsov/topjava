<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">
    <title>Meals list</title>
</head>
<jsp:useBean id="meals" scope="request" type="java.util.List"/>
<body>
<div class="container" style="margin-top: 30px">
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    <div class="d-flex align-items-center highlight-toolbar ps-2  py-1">
                        <h2 class="">Meals list</h2>
                        <div class="d-flex ms-auto gap-2">
                            <button type="button" class="btn btn btn-outline-success"
                                    onclick="location.href='meals?&action=create'"> Добавить
                                <i class="bi bi-plus-circle"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Description</th>
                            <th>Calories</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${meals}" var="meal">
                            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
                            <tr class="${meal.excess ? 'text-danger' : 'text-success'}">
                            <td>${meal.date} ${meal.time}</td>
                            <td>${meal.description}</td>
                            <td>${meal.calories}</td>
                            <td>
                                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                    <button type="button" class="btn btn-outline-danger"
                                            onclick="location.href='meals?id=${meal.id}&action=delete'">
                                        <i class="bi bi-trash3-fill"></i>
                                    </button>
                                    <button type="button" class="btn btn btn-outline-success"
                                            onclick="location.href='meals?id=${meal.id}&action=edit'">
                                        <i class="bi bi-pencil-square"></i>
                                    </button>
                                </div>
                            </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>