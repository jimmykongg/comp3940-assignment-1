
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%--
 Created by IntelliJ IDEA.
 User: henrytan
 Date: 2024-09-19
 Time: 9:56 p.m.
 To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AutoPlay</title>
    <%
        Integer category = (Integer) request.getAttribute("category");
        Integer curQuizIndex = (Integer) request.getAttribute("quizIndex");
        Map<String, String> quiz = (Map<String, String>) request.getAttribute("quiz");


    %>
    <style>
        .correct-answer {
            background-color: green;
        }
    </style>
    <script>
        function highlightAnswer() {
            let correctAnswer = document.getElementById("correctAnswer");
            if (correctAnswer) {
                correctAnswer.class.add("correct")
            }
        }


        function moveToNextQuestion() {
            window.location.href = '/autoplay?category=<%= category %>';
        }


        setTimeout(highlightAnswer, 10000); // 10s for testing
        setTimeout(moveToNextQuestion, 15000) // 15s for testing


        function loadMedia(mediaId) {
            var xhttp = new XMLHttpRequest();
            xhttp.open('GET', '/WebApp_war/media?mediaId=' + mediaId, true);
            xhttp.onload = function () {
                if (xhttp.status === 200) {
                    const mediaData = JSON.parse(xhttp.responseText);
                    const mediaType = mediaData.mediaType;
                    const filePath = mediaData.filePath;
                    let mediaHTML = '';


                    if (mediaType === 'image') {
                        mediaHTML = `<img src="/webapp/images/${filePath}" alt="Quiz Image" style="max-width: 600px;">`;
                    } else if (mediaType === 'video') {
                        mediaHTML = `<iframe width="600" height="400" src="https://www.youtube.com/embed/" + ${filePath}"></iframe>`;
                    } else if (mediaType === 'audio') {
                        mediaHTML = `<iframe width="600" height="400" src="https://www.youtube.com/embed/" +"${filePath}"></iframe>`;
                    }


                    document.getElementById('mediaDisplay').innerHTML = mediaHTML;
                }
            };
            xhttp.send();
        }


        window.onload = function () {
            const mediaId = '<%= quiz.get("media_id") %>';
            if (mediaId) {
                loadMedia(mediaId);
            }
        };
    </script>
</head>
<body>


<div id="mediaDisplay">Media Section</div>


<h2><%= quiz.get("description") %>
</h2>
<h2>Question:</h2>
<p>${quiz.description}</p>
<h3>Answers:</h3>
<ul>
    <c:forEach var="answer" items="${answers}">
        <li id="${answer.description == correctAnswer ? 'correctAnswer' : ''}">
                ${answer.description}
        </li>
    </c:forEach>
</ul>
</body>
</html>
