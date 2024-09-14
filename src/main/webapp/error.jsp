<%--
  Created by IntelliJ IDEA.
  User: ziqi
  Date: 2024-09-13
  Time: 9:55 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
<h1> This is an error page. </h1>
<p><%=request.getAttribute("message")%></p>
</body>
</html>
