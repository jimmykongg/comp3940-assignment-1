<%-- Created by IntelliJ IDEA. User: jk Date: 2024-09-13 Time: 3:08 p.m. To
change this template use File | Settings | File Templates. --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="/resources/css/login.css" rel="stylesheet" />
    <title>QuizApp - Login</title>
  </head>

  <body>
    <div class="wrapper">
    <h2>Login</h2>

    <form id="loginForm">
      <div>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required />
      </div>

      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required />
      </div>

      <div>
        <button type="submit">Login</button>
      </div>
    </form>

      <div>
        <button id="signup">Signup</button>
      </div>
    </div>
    <script src="/resources/js/login.js" defer></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
  </body>
</html>

