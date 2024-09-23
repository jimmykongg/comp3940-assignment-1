<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Quiz</title>
</head>
<body>
<%
    Map<String, String> quiz = (Map<String, String>) request.getAttribute("quiz");
    List<Map<String, String>> answers = (List<Map<String, String>>) request.getAttribute("answers");
    List<Map<String, String>> categories = (List<Map<String, String>>) request.getAttribute("categories");
%>

<form action="/editQuiz" method="post">
    <!-- Hidden field for quiz ID -->
    <input type="hidden" name="quizId" value="<%= quiz.get("id") %>">

    <!-- Edit Quiz Description -->
    <label for="description">Description: </label>
    <input type="text" id="description" name="description" value="<%= quiz.get("description") %>" required>

    <br/><br/>


    <% if (answers.size() == 4) {
        String correctAnswerId = null;
        List<String> falseAnswersId = new ArrayList<>();
        for(Map<String, String> answer : answers){
            if(answer.get("right_answer").equals("t")) correctAnswerId = answer.get("id");
            else falseAnswersId.add(answer.get("id"));
        }
    %>
    <input type="hidden" id="quizType" name="quizType" value="multi">
    <input type="hidden" id="correctAnswer_id" name="correctAnswer_id" value=<%=correctAnswerId%>>
    <input type="hidden" id="falseAnswer1_id" name="falseAnswer1_id" value=<%=falseAnswersId.get(0)%>>
    <input type="hidden" id="falseAnswer2_id" name="falseAnswer2_id" value=<%=falseAnswersId.get(1)%>>
    <input type="hidden" id="falseAnswer3_id" name="falseAnswer3_id" value=<%=falseAnswersId.get(2)%>>
    <div id="multipleChoiceAnswers">
        <label for="correctAnswer">Correct Answer:</label>
        <input type="text" id="correctAnswer" name="correctAnswer" value=<%= answers.get(0).get("description")%>><br/>

        <label for="falseAnswer1">False Answer 1:</label>
        <input type="text" id="falseAnswer1" name="falseAnswer1" value=<%= answers.get(1).get("description")%>><br/>

        <label for="falseAnswer2">False Answer 2:</label>
        <input type="text" id="falseAnswer2" name="falseAnswer2" value=<%= answers.get(2).get("description")%>><br/>

        <label for="falseAnswer3">False Answer 3:</label>
        <input type="text" id="falseAnswer3" name="falseAnswer3" value=<%= answers.get(3).get("description")%>><br/>
    </div>
    <%} else {%>
    <!-- True/False Answer Field -->
    <input type="hidden" id="quizType" name="quizType" value="trueFalse">
    <div id="trueFalseFields">
        <label for="trueFalseAnswer">Correct Answer:</label><br/>
        <select id="trueFalseAnswer" name="trueFalseAnswer">
            <option value="true" <%= Boolean.parseBoolean(answers.get(0).get("right_answer")) ? "selected" : "" %>>True</option>
            <option value="false" <%= Boolean.parseBoolean(answers.get(1).get("right_answer")) ? "selected" : "" %>>False</option>
        </select>
    </div>

    <%}%>



    <br/>


    <label for="category">Category:</label>
    <select id="category" name="category" onchange="toggleNewCategoryField()">
        <% for (Map<String, String> category : categories) { %>
        <option value="<%= category.get("id") %>" <%= category.get("id").equals(quiz.get("category_id")) ? "selected" : "" %> >
            <%= category.get("name") %>
        </option>
        <% } %>
        <option value="newCategory">New Category</option>
    </select>

    <!-- New Category Field -->
    <div id="createNewCategory" style="display: none;">
        <label for="newCategoryName">New Category Name:</label>
        <input type="text" id="newCategoryName" name="newCategoryName">
    </div>

    <br/>


    <label for="mediaType">Media Type:</label>
    <input type="text" id="mediaType" name="mediaType" value="<%= quiz.get("media_type") != null ? quiz.get("media_type") : "" %>">

    <label for="mediaPath">Media Path:</label>
    <input type="text" id="mediaPath" name="mediaPath" value="<%= quiz.get("file_path") != null ? quiz.get("file_path") : "" %>">

    <br/><br/>
    <button type="submit">Update Quiz</button>
</form>


<script>

    function toggleNewCategoryField() {
        var category = document.getElementById("category").value;
        document.getElementById("createNewCategory").style.display = category === "newCategory" ? "block" : "none";
    }
</script>
</body>
</html>
