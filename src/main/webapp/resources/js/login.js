async function handleSubmit() {
  const form = document.getElementById("loginForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(form);
    const formBody = new URLSearchParams(formData).toString();

    try {
      const res = await axios.post("/api/login", formBody, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      });

      window.location.href="/categories";
    } catch (e) {
      console.log("Error", e);
    }
  });
}

function handleRedirect() {
  const signupButton = document.getElementById("signup");

  signupButton.addEventListener("click", () => window.location.href = "/signup")
}

handleSubmit();
handleRedirect();

