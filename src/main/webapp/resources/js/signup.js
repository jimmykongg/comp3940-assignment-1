async function handleSubmit() {
  console.log("signup form submitted");
  const form = document.getElementById("signupForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(form);
    const formBody = new URLSearchParams(formData).toString();

    try {
      console.log("calling api");
      const res = await axios.post("/WebApp_war/api/signup", formBody, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      });
      console.log("successfully call the api")

      console.log(res.data.message);
      window.location.href="/WebApp_war/categories";
    } catch (e) {
      console.log("Error", e);
    }
  });
}

handleSubmit();

