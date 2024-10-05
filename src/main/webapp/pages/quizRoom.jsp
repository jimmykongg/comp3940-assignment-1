<%--
  Created by IntelliJ IDEA.
  User: ziqi
  Date: 2024-10-03
  Time: 5:17 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1 id="roomID">Room ID: <%= request.getAttribute("roomID")%></h1>

<form id="test_form">
    <label for="text">what is in your mind?</label>
    <input id="text" name="text">
    <button type="submit">send</button>
</form>
<div id="test_window"></div>


</body>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="/resources/js/quizRoom.js"></script>
</html>
