<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <table>
        <tr>
            <td>id</td>
            <td>name</td>
        </tr>
        <%
            List<Map<String, String>> categories = (List<Map<String, String>>) request.getAttribute("categories");
            for(Map<String, String> category : categories){
        %>
        <tr>
            <td><%=category.get("id")%></td>
            <td><%=category.get("name")%></td>
        </tr>
        <%
            }
        %>
    </table>
</div>
</body>
</html>