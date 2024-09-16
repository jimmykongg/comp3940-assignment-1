async function insertHTML() {
    const wrapper = document.querySelector(".buttonWrapper");
    const hasLoggedIn = await checkAuth();

    if (hasLoggedIn) {
        wrapper.innerHTML = `
            <form action="/categories" method="GET">
                <button type="submit">Start playing quizzes</button>
            </form>

            <form action="/api/logout" method="POST">
                <button type="submit">Logout</button>
            </form>
        `
    } else {
        wrapper.innerHTML = `
            <form action="/login" method="GET">
                <button type="submit">Log in</button>
            </form>
        `
    }
}

async function checkAuth() {
    try {
        const res = await axios.get("/api/check-auth");
        const data = res.data;

        return data.loggedIn;
    } catch (e) {
        console.log("Error", e);
    }
}

insertHTML();

