<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ziqi
  Date: 2024-09-21
  Time: 4:29 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<% List<Map<String, String>> categories = (List<Map<String, String>>) request.getAttribute("categories");%>
<form action="/createQuiz" method="post">
    <!-- Ask user for the description of question-->
    <label for="description">Description: </label>
    <input type="text" id="description" name="description" required>

    <br/><br/>
    <label for="quizType">Quiz Type:</label>
    <select id="quizType" name="quizType" onchange=" toggleQuestionFields()">
        <option value="multi">Multiple Choice</option>
        <option value="trueFalse">True or False</option>
    </select>

    <br/><br/>
    <div id="multipleChoiceAnswers" style="display: block">
        <label for="correctAnswer">Correct Answer:</label>
        <input type="text" id="correctAnswer" name="correctAnswer"><br/>
        <label for="falseAnswer1">False Answer1:</label>
        <input type="text" id="falseAnswer1" name="falseAnswer1"><br/>
        <label for="falseAnswer2">False Answer2:</label>
        <input type="text" id="falseAnswer2" name="falseAnswer2"><br/>
        <label for="falseAnswer3">False Answer3:</label>
        <input type="text" id="falseAnswer3" name="falseAnswer3"><br/>
    </div>

    <div id="trueFalseFields" style="display: none;">
        <label for="trueFalseAnswer">Correct Answer:</label><br/>
        <select id="trueFalseAnswer" name="trueFalseAnswer">
            <option value="true">True</option>
            <option value="false">False</option>
        </select>
    </div>

    <!--Ask user ro select the category of this quiz
        toggle the new category field for filed name
        if user select new category option
    -->
    <br/>
    <label for="category">Category:</label>
    <select id="category" name="category" onchange="toggleNewCategoryField()">
        <%for(Map<String, String> category : categories ) {%>
        <option value="<%= category.get("id")%>"><%=category.get("name")%></option>
        <%}%>
        <option value="newCategory">New Category</option>
    </select>

    <br/>
    <div id="createNewCategory" style="display: none">
        <label for="newCategoryName">New Category Name:</label>
        <input type="text" id="newCategoryName" name="newCategoryName">
    </div>

    <br/>
    <label for="mediaType">Media Type:</label>
    <input type="text" id="mediaType" name="mediaType">
    <label for="mediaPath">Media Path:</label>
    <input type="text" id="mediaPath" name="mediaPath">

    <br/>
    <button type="submit">Create Quiz</button>
</form>


<script src="/resources/js/createQuiz.js"></script>
</body>
</html>
