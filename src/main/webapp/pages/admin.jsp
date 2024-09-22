<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %><%-- Created by IntelliJ IDEA. User: jk Date: 2024-09-19 Time: 2:18 p.m. To
change this template use File | Settings | File Templates. --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="/resources/css/admin.css" rel="stylesheet" />
    <script src="/resources/js/admin.js" defer></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <title>QuizApp - Admin</title>
  </head>

  <body>
<% ArrayList<Map<Integer, String>> quizzes = (ArrayList<Map<Integer, String>>) request.getAttribute("quizList");
  for(Map<Integer, String> quiz : quizzes){
    String quizId = quiz.get("id");
    String quizDescription = quiz.get("description");
%>
  <div>
    <p>ID: <%= quizId%> Quiz: <%=quizDescription%></p>
    <form action="editQuiz" method="post" style="display: inline">
      <input type="hidden" name="quizId" value="<%=quizId%>">
      <button type="submit">Edit</button>
    </form>

    <form action="deleteQuiz" method="post" style="display:inline;">
      <input type="hidden" name="quizId" value="<%= quizId %>" />
      <button type="submit">Delete</button>
    </form>

  </div>
 <% }
%>

  </body>
</html>

