<html>
<body>
<h2>Signup Form</h2>
<form action="/api/signup" method="POST">
    <div>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>
    </div>

    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
    </div>

    <div>
        <button type="submit">Sign Up</button>
    </div>
</form>
</body>
</html>
