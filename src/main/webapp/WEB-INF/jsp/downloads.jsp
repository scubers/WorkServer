<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

        $("#configSaveBtn").bind("click", function () {
            $.ajax({
                cache: true,
                type: "POST",
                url:"${pageContext.request.contextPath}/ios/saveConfig.do",
                data:$('#configForm').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    console.log(request)
                },
                success: function(data, data2) {
                    alert(data)
                    $("[name=projectName]").each(function () {
                        $(this).val($("#projectName").val())
                    })
                }
            })
        })

        $("#uploadCerBtn").bind("click", function () {
            $("#uploadCerForm").submit()
        })

        $("#uploadProBtn").bind("click", function () {
            $("#uploadProForm").submit()
        })

        $("#packageBtn").bind("click", function () {
            $.ajax({
                type: "POST",
                data: {projectName: $("#projectName").val()},
                url: "${pageContext.request.contextPath}/ios/package.do",
                error: function (err) {
                    console.log(err)
                },
                success: function (data, data1) {
                    console.log(data)
                }
            });
        })

    });

</script>

<body role="document">

<!-- Fixed navbar -->
<nav class="navbar navbar-inverse navbar-fixed-top">
</nav>

<div class="container theme-showcase" role="main">

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
        <h1>iOS打包配置</h1>
        <p>This is a template showcasing the optional theme stylesheet included in Bootstrap. Use it as a starting point to create something more unique by building on or modifying it.</p>
    </div>

    <a href="https://192.168.31.152:8443/work/builds/my.crt">下载证书</a>

    <c:forEach var="name" items="${filenames}">
        <p>
            <a class="btn btn-lg btn-primary" href="itms-services://?action=download-manifest&url=${downloadBaseUrl}/${name}" role="button">${name}</a>
        </p>
    </c:forEach>

</div> <!-- /container -->


</body>
</html>