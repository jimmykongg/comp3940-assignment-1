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
    <link href="/resources/css/quizRoom.css" rel="stylesheet" />
</head>
<body>
<h1 id="roomID">Room ID: <%= request.getAttribute("roomID")%></h1>

<div class="wrapper">
    <div class="quizWrapper">
        <div class="mediaWrapper"></div>

        <div class="questionWrapper">
            <span id="question"></span>
            <button id="nextQuestion">Next Question</button>
        </div>

        <div class="answersWrapper"></div>
    </div>

    <div class="chatRoomWrapper">
        <textarea id="chatRoom" rows="5" cols="30"></textarea>
    </div>
</div>


</body>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="/resources/js/quizRoom.js"></script>
</html>
