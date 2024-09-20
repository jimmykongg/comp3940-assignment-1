<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Categories</title>
</head>
<body>
<div>

    <%
        List<Map<String, String>> categories = (List<Map<String, String>>) request.getAttribute("categories");
        for (Map<String, String> category : categories) {
    %>
    <div>
        <a href="/WebApp_war/play?category=<%= category.get("id") %>"><%= category.get("name") %> (Regular)</a>
        &nbsp&nbsp&nbsp&nbsp&nbsp
        <a href="/WebApp_war/autoplay?category=<%= category.get("id") %>"><%= category.get("name") %> (AutoPlay)</a>
    </div>
    <%
        }
    %>

</div>
</body>
</html>