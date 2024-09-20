<%-- Created by IntelliJ IDEA. User: jk Date: 2024-09-19 Time: 2:18 p.m. To
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
    <h1>Manage Quiz</h1>

    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Description</th>
        </tr>
      </thead>

      <tbody id="tableBody">
        <!-- <tr><td>id</td></td>description</td></tr> -->
      </tbody>
    </table>
  </body>
</html>

