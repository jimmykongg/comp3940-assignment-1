<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Auto Play Quizzes</title>
    <%
        Integer category = (Integer) request.getAttribute("category");
        Integer curQuizIndex = (Integer) request.getAttribute("quizIndex");
        Map<String, String> quiz = (Map<String, String>) request.getAttribute("quiz");
    %>
    <script>
        let delayBeforeShowingAnswer = 10000; // 10 seconds before showing the correct answer
        let delayBeforeNextQuestion = 14000; // 14 seconds after showing the correct answer

        function showCorrectAnswer() {
            let correctAnswerElement = document.getElementById('correctAnswerMessage');
            correctAnswerElement.style.display = 'block';
        }

        function proceedToNextQuestion() {
            document.getElementById('nextQuestionForm').submit();
        }

        function loadMedia(url) {
            var xhttp = new XMLHttpRequest();
            xhttp.open('GET', url, true);
            xhttp.onload = function() {
                if (xhttp.status === 200) {
                    const mediaData = JSON.parse(xhttp.responseText);
                    const mediaType = mediaData.mediaType;
                    const filePath = mediaData.filePath;
                    let mediaHTML = '';

                    if (mediaType === 'image') {
                        mediaHTML = `<img src="/images/${filePath}" alt="Quiz Image" style="max-width: 600px;">`;
                    } else if (mediaType === 'video') {
                        mediaHTML = `<iframe width="600" height="400" src="https://www.youtube.com/embed/${filePath}?autoplay=1&mute=1" allow="autoplay"; encrypted-media"></iframe>`;
                    } else if (mediaType === 'audio') {
                        mediaHTML = `<iframe width="600" height="400" src="https://www.youtube.com/embed/${filePath}?autoplay=1&mute=1" allow="autoplay"; encrypted-media></iframe>`;
                    }

                    document.getElementById('mediaDisplay').innerHTML = mediaHTML;
                }
            };
            xhttp.send();
        }

        window.onload = function() {
            setTimeout(function() {
                showCorrectAnswer();
                setTimeout(proceedToNextQuestion, delayBeforeNextQuestion);
            }, delayBeforeShowingAnswer);

            const mediaId = '<%= quiz.get("media_id") %>';
            let url = '/media?mediaId=' + mediaId;
            if (url) {
                loadMedia(url);
            }
        };
    </script>
</head>
<body>
<div id="mediaDisplay"></div>

<%-- Code for testing --%>
<%--<p>In variable: <%= curQuizIndex %>, In session: <%= session.getAttribute("quizIndex") %></p>--%>

<p><%= quiz.get("description") %></p>

<!-- Display quiz answers -->
<%
    List<Map<String, String>> answers = (List<Map<String, String>>) request.getAttribute("answers");
    for (Map<String, String> answer : answers) {
%>
<button><%= answer.get("description") %></button>
<%
    }
%>

<form id="nextQuestionForm" action="/autoplay" method="post" style="display: none;">
    <input type="hidden" name="curQuizIndex" id="curQuizIndex" value="<%=curQuizIndex%>"/>
    <input type="hidden" name="selectedAnswer" value="0"/> <!-- Default, update as needed -->
    <input type="hidden" name="category" id="category" value="<%= category %>"/>
</form>

<%
    Map<String, String> correctAnswer = null;
    for(Map<String, String> answer : answers){
        if(answer.get("right_answer").equals("t")) correctAnswer = answer;
    }
%>

<!-- Hidden element to display correct answer -->
<div id="correctAnswerMessage" style="display: none; margin-top: 20px;">
    <p>The correct answer is: <strong><%= correctAnswer.get("description") %></strong></p>
</div>

</body>
</html>