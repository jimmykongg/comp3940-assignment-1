async function handleSubmit() {
  const form = document.getElementById("signupForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(form);
    try {
      const res = await axios.post("/api/signup", formData, {
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

