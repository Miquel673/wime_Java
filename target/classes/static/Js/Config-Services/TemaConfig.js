
document.addEventListener("DOMContentLoaded", () => {
  const tema = localStorage.getItem("tema") || "claro";

  if (tema === "oscuro") {
    document.body.classList.add("dark-mode");
  } else {
    document.body.classList.remove("dark-mode");
  }
});
