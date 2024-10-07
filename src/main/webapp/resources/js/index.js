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

            <button id="logoutButton">Logout</button>
        `;

    if (role === "admin") {
      wrapper.insertAdjacentHTML(
        "beforeend",
        `<form action="/admin" method="GET">
            <button type="submit">Manage quizzes</button>
         </form>`
      );
    }

    document.getElementById('logoutButton').addEventListener('click', async () => {
      try {
        await axios.post('/api/logout');
        window.location.href = "/";
      } catch (e) {
        console.log("Error", e);
      }
    });

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

