<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ziqi
  Date: 2024-10-03
  Time: 4:15 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="/lobby">back</a>
<%
    List<Map<String, String>> categories = (List<Map<String, String>>) request.getAttribute("categories");
%>
<form action="/createRoom" method="post">

<label for="category">Category:</label>
<select id="category" name="category">
    <%for(Map<String, String> category : categories) {%>
    <option value="<%= category.get("id")%>"><%=category.get("name")%></option>
    <%}%>
</select>
<br/>

<label for="roomID">Room ID:</label>
<input id="roomID" name="roomID" required>
<button type="submit">Create Room</button>
</form>

</body>
</html>
