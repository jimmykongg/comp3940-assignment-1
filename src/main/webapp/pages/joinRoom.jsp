<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.quizapp.quizroom.Room" %>
<html>
<head>
  <title>Join Quiz Room</title>
</head>
<body>
<h1>Available Quiz Rooms</h1>
<ul>
  <%
    // Retrieve the quizRooms map that was set as a request attribute
    Map<String, Room> quizRooms = (Map<String, Room>) request.getAttribute("quizRooms");

    // Check if the map is not null and iterate through it
    if (quizRooms != null) {
      for (Map.Entry<String, Room> entry : quizRooms.entrySet()) {
        String roomID = entry.getValue().getRoomID();
        String categoryID = entry.getValue().getCategoryID();
  %>
  <li>
    <a href="/quizRoom?categoryID=<%= categoryID %>&roomID=<%= roomID %>">
      Room: <%= roomID %>
    </a>
  </li>
  <%
    }
  } else {
  %>
  <li>No quiz rooms available at the moment.</li>
  <%
    }
  %>
</ul>
</body>
</html>