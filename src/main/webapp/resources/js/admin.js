// async function insertRows() {
//   const tableBody = document.getElementById("tableBody");
//   await checkRole();
//
//   const res = await axios.get("/api/admin");
//   const quizzes = res?.data;
//
//   quizzes.forEach((quiz) => {
//     const [id, description] = Object.entries(quiz)[0];
//
//     tableBody.insertAdjacentHTML(
//       "beforeend",
//       `<tr><td>${id}</td></td>${description}</td></tr>`
//     );
//   });
// }
//
// async function checkRole() {
//   try {
//     const res = await axios.get("/api/check-auth");
//     const role = res.data?.role;
//
//     if (role !== "admin") {
//       window.location.href = "/";
//     }
//   } catch (e) {
//     console.log("Error", e);
//   }
// }
//
// insertRows();
//
