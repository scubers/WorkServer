<%--
  Created by IntelliJ IDEA.
  User: J
  Date: 16/8/23
  Time: 上午9:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<script type="application/javascript">
    function upload() {
        window.document.getElementById("form").submit()
    }
</script>

<html>
<head>
    <title>download</title>
</head>
<body>

<form id="form" method="post" enctype="multipart/form-data" action="uploadFile.do">
    <input type="file" name="file"/>
</form>

<input type="button" onclick="upload()" value="上传"/>

</body>
</html>
