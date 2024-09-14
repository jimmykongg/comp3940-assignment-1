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
    <div>For media</div>
    <%
      Integer category = (Integer) request.getAttribute("category");
      Integer curQuizIndex = (Integer) request.getAttribute("quizIndex");



      Map<String, String> quiz = (Map<String, String>) request.getAttribute("quiz");
    %>
    <p><%= quiz.get("description") %></p>
    <p>In variable: <%= curQuizIndex %>, In session: <%= session.getAttribute("quizIndex") %></p>
    <form id="answerForm" action="/WebApp_war/play" method="post">
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
</script>

</body>
</html>
