<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ziqi
  Date: 2024-09-14
  Time: 3:05 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>playQuizzes</title>
</head>
<body>
<%
    Integer category = (Integer) request.getAttribute("category");
    Integer curQuizIndex = (Integer) request.getAttribute("quizIndex");
    Map<String, String> quiz = (Map<String, String>) request.getAttribute("quiz");

    // This is the media_ID.
    // I have to comment them out because they are all null
    // And it will break the code if I try to make them an integer.
    // Integer mediaID = Integer.parseInt(quiz.get("media_id"));

%>
    <div id="mediaDisplay"></div>

    <p><%= quiz.get("description") %></p>
    <p>In variable: <%= curQuizIndex %>, In session: <%= session.getAttribute("quizIndex") %></p>
    <form id="answerForm" action="/play" method="post">
        <input type="hidden" name="selectedAnswer" id="selectedAnswer" value=""/>
        <input type="hidden" name="category" id="category" value="<%= category %>"/>
        <input type="hidden" name="curQuizIndex" id="curQuizIndex" value="<%=curQuizIndex%>"/>
    <%
      List<Map<String, String>> answers = (List<Map<String, String>>) request.getAttribute("answers");
      for(Map<String, String> answer : answers) {
    %>
      <button onclick="submitForm('<%= answer.get("id")%>')"><%= answer.get("description") %><%= answer.get("id")%></button>
    <%
      }
    %>
    </form>
<script>
  function submitForm(selectedId){
    document.querySelector("#selectedAnswer").value = selectedId;
    document.querySelector("#answerForm").submit();
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
      const mediaId = '<%= quiz.get("media_id") %>';
      let url = '/media?mediaId=' + mediaId;
      if (url) {
          loadMedia(url);
      }
  };
</script>

</body>
</html>