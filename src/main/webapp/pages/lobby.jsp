<%--
  Created by IntelliJ IDEA.
  User: ziqi
  Date: 2024-10-03
  Time: 4:08 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<jsp:include page="./template/header.jsp"/>
<%
    Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
    if (isAdmin != null && isAdmin) {
%>
<a href="/createRoom">Create a quiz room</a>
<%
    }
%>
<a href="/joinRoom">Join a quiz room</a>

</body>
</html>
