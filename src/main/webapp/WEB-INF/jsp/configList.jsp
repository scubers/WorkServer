<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: J
  Date: 16/8/25
  Time: 上午11:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>配置</title>


    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap theme -->
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.1.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
</head>

<script type="application/javascript">

    $(function () {

        $("#add").bind('click', function () {
            window.location.href = "${pageContext.request.contextPath}/ios/config.do";
        })

    });

    function deleteConfig(name) {
        if (confirm("是否确认删除")) {
            window.location.href = "${pageContext.request.contextPath}/ios/deleteConfig.do?projectName=" + name;
        }
    }

</script>

<body role="document">

<!-- Fixed navbar -->
<nav class="navbar navbar-inverse navbar-fixed-top">
</nav>

<div class="container theme-showcase" role="main">

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
        <h1>Project 配置列表</h1>
        <p>点击修改或者打包</p>
    </div>

    <p>
        <button type="button" class="btn btn-lg btn-success" id="add">添加配置</button>
    </p>

    <p>

    </p>
    <p>

    </p>

    <c:forEach var="name" items="${names}">

        <p>
            <a class="btn btn-lg btn-primary" href="${pageContext.request.contextPath}/ios/config.do?projectName=${name}" role="button">
                    ${name}
            </a>

            <a class="btn btn-sm btn-warning" onclick="deleteConfig(${name})" role="button">
                    删除配置
            </a>
        </p>

    </c:forEach>

</div> <!-- /container -->


</body>
</html>