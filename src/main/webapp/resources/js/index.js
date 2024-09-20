async function insertHTML() {
  const wrapper = document.querySelector(".buttonWrapper");
  const [hasLoggedIn, role] = await checkAuth();

  console.log(hasLoggedIn);
  console.log(role);

  if (hasLoggedIn) {
    wrapper.innerHTML = `
            <form action="/categories" method="GET">
                <button type="submit">Start playing quizzes</button>
            </form>

            <form action="/api/logout" method="POST">
                <button type="submit">Logout</button>
            </form>
        `;

    if (role === "admin") {
      wrapper.insertAdjacentHTML(
        "beforeend",
        `<form action="/admin" method="GET">
            <button type="submit">Manage quizzes</button>
         </form>`
      );
    }
  } else {
    wrapper.innerHTML = `
        <form action="/login" method="GET">
            <button type="submit">Log in</button>
        </form>
    `;
  }
}

async function checkAuth() {
  try {
    const res = await axios.get("/api/check-auth");
    const data = res.data;

    return [data.loggedIn, data?.role];
  } catch (e) {
    console.log("Error", e);
  }
}

insertHTML();

