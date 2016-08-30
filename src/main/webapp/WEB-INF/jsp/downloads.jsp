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

    function autorefresh() {
        window.history.go(0);
    }

    $(function () {

        $("#downloadCrt").bind('click', function () {
            window.location.href = "${pageContext.request.contextPath}/builds/my.crt";
        })

        $("#configListBtn").bind('click', function () {
            window.location.href = "${pageContext.request.contextPath}/ios/configList.do";
        })

        window.setInterval("autorefresh()", 5000);
    });

    function deletePackage(name) {
        window.location.href = "${pageContext.request.contextPath}/ios/deletePackage.do?packageName=" + name;
    }

</script>

<body role="document">

<!-- Fixed navbar -->
<nav class="navbar navbar-inverse navbar-fixed-top">
</nav>

<div class="container theme-showcase" role="main">

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
        <h1>IPA下载</h1>
        <p>按时间倒序排列, 选择自己需要的包进行下载</p>
        <p>如没看到自己的包, 请稍等, 自动刷新</p>
    </div>

    <%--<a href="${pageContext.request.contextPath}/builds/my.crt">下载证书</a>--%>
    <p>
        <button type="button" id="downloadCrt" class="btn btn-lg btn-warning">不能下载请先下载证书</button>
        &nbsp;&nbsp;&nbsp;
        <button type="button" id="configListBtn" class="btn btn-lg btn-warning">配置列表</button>
    </p>


    <c:forEach var="name" items="${filenames}">
        <c:if test="${name ne 'empty'}">
            <p>
                <a class="btn btn-lg btn-primary" href="itms-services://?action=download-manifest&url=${downloadBaseUrl}/${name}" role="button">
                        ${fn:substring(name, 0, fn:indexOf(name, "."))}
                </a>

                <a class="btn btn-sm btn-warning" onclick="deletePackage('${fn:substring(name, 0, fn:indexOf(name, "."))}')" role="button">
                    删除包
                </a>
            </p>
        </c:if>
    </c:forEach>

</div> <!-- /container -->


</body>
</html>