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

    <div class="page-header">
        <form class="form-signin" id="configForm">

            <h2 class="form-signin-heading">ProjectName</h2>
            <label for="inputEmail" class="sr-only">ProjectName</label>
            <input type="text" name="projectName" id="projectName" class="form-control" placeholder="一个打包工程的标识" value="${config.projectName}">

            <h2 class="form-signin-heading">SVN Path</h2>
            <label for="inputEmail" class="sr-only">SVN path full path</label>
            <input type="text" name="svnpath" id="inputEmail" class="form-control" placeholder="SVN path full path" value="${config.svnpath}">

            <h2 class="form-signin-heading">用户名</h2>
            <label for="inputEmail" class="sr-only">username</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="username" value="${config.username}">

            <h2 class="form-signin-heading">密码</h2>
            <label for="pwd" class="sr-only">Password</label>
            <input type="password" id="pwd" name="pwd" class="form-control" placeholder="Password" value="${config.pwd}">

            <h2 class="form-signin-heading">scheme</h2>
            <label for="scheme" class="sr-only">scheme</label>
            <input type="text" id="scheme" name="scheme" class="form-control" placeholder="scheme" value="${config.scheme}">

            <h2 class="form-signin-heading">target</h2>
            <label for="target" class="sr-only">target</label>
            <input type="text" id="target" name="target" class="form-control" placeholder="target" value="${config.target}">

            <h2 class="form-signin-heading">Configuration</h2>
            <label for="Configuration" class="sr-only">Configuration</label>
            <input type="text" id="Configuration" name="configuration" class="form-control" placeholder="Configuration" value="${config.configuration}">

            <h2 class="form-signin-heading">P12密码</h2>
            <label for="Configuration" class="sr-only">p12pwd</label>
            <input type="text" id="p12pwd" name="confp12pwdiguration" class="form-control" placeholder="Configuration" value="${config.p12pwd}">


        </form>
    </div>


    <p>
        <button type="button" id="configSaveBtn" class="btn btn-sm btn-primary">保存</button>
    </p>


    <div class="page-header">
        <h1>上传证书</h1>

    </div>
    <p>
        <form id="uploadCerForm" action="${pageContext.request.contextPath}/ios/uploadCer.do" method="post" enctype="multipart/form-data">
            <input type="file" name="file" id="certificateFile"/>
            <input type="hidden" name="projectName" value="${config.projectName}"/>
        </form>
    </p>
    <p>
        <button type="button" id="uploadCerBtn" class="btn btn-sm btn-primary">上传</button>
    </p>

    <div class="page-header">
        <h1>Provisioning文件</h1>

    </div>
    <p>
        <form id="uploadProForm" action="${pageContext.request.contextPath}/ios/uploadProvision.do" method="post" enctype="multipart/form-data">
            <input type="file" name="file" id="provisioning"/>
            <input type="hidden" name="projectName" value="${config.projectName}"/>
        </form>

    </p>
    <p>
        <button type="button" id="uploadProBtn" class="btn btn-sm btn-primary">上传</button>
    </p>

    <p>
        <button class="btn btn-lg btn-primary btn-block" id="packageBtn" type="submit">打包</button>
    </p>


</div> <!-- /container -->


</body>
</html>