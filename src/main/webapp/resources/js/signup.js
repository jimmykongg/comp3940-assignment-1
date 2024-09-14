async function handleSubmit() {
  const form = document.getElementById("signupForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(form);
    const formBody = new URLSearchParams(formData).toString();

    try {
      const res = await axios.post("/api/signup", formBody, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      });

      console.log(res.data.message);
    } catch (e) {
      console.log("Error", e);
    }
  });
}

handleSubmit();

