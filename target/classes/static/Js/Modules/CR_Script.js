document.getElementById("form-rutina").addEventListener("submit", async (e) => {
  e.preventDefault();

  const data = Object.fromEntries(new FormData(e.target).entries());

  const res = await fetch("/api/rutinas/crear", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  const result = await res.json();
  alert(result.message);

  if (result.success) {
    window.location.href = "../../HTML/Interfaces/Wime_interfaz_Tablero.html";
  }
});
