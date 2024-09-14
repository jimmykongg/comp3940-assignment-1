async function handleSubmit() {
  const form = document.getElementById("loginForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(form);
    try {
      console.log("logging in now");
      const res = await axios.post("/api/login", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      console.log(res.data.message);
    } catch (e) {
      console.log("Error", e);
    }
  });
}

handleSubmit();

