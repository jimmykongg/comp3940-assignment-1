<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %><%-- Created by IntelliJ IDEA. User: jk Date: 2024-09-19 Time: 2:18 p.m. To
change this template use File | Settings | File Templates. --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <link href="/resources/css/admin.css" rel="stylesheet" />
    <script src="/resources/js/admin.js" defer></script>
    <title>QuizApp - Admin</title>
  </head>

  <body>
  <a href="/createQuiz">Create a Quiz</a>

<% ArrayList<Map<Integer, String>> quizzes = (ArrayList<Map<Integer, String>>) request.getAttribute("quizList");
  for(Map<Integer, String> quiz : quizzes){
    String quizId = quiz.get("id");

    String quizDescription = quiz.get("description");
%>
  <div>
    <p>ID: <%= quizId%> Quiz: <%=quizDescription%></p>
    <form action="/editQuiz" method="get" style="display: inline">
      <input type="hidden" name="quizId" value="<%=quizId%>">
      <button type="submit">Edit</button>
    </form>

    <button type="button" onclick="deleteQuiz('<%= quizId %>')">Delete</button>

  </div>
 <% }
%>
  <script>
    function deleteQuiz(quizId) {
      if (confirm('Are you sure you want to delete this quiz?')) {
        axios.delete(`/deleteQuiz/delete/id=${quizId}`)
                .then(function (response) {
                  if (response.status === 200) {
                    alert('Quiz deleted successfully');
                    window.location.reload();
                  }
                })
                .catch(function (error) {
                  alert('Error deleting quiz: ' + error.response.data.message);
                });
      }
    }
  </script>

  </body>
</html>

