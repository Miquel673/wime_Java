// ======================================================
// LOGIN DE USUARIO - WIME
// ======================================================

document.addEventListener("DOMContentLoaded", function () {

  console.log("✅ Script de login cargado correctamente");

  const form = document.getElementById("form-login");

  if (!form) {
    console.error("❌ No se encontró el formulario con ID form-login.");
    return;
  }

  form.addEventListener("submit", function (e) {

    e.preventDefault();

    const email = document.getElementById("email").value.trim();
    const contrasena = document.getElementById("contrasena").value;

    if (!email || !contrasena) {
      mostrarToast("Debes llenar todos los campos.", false);
      return;
    }

    fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `email=${encodeURIComponent(email)}&contrasena=${encodeURIComponent(contrasena)}`,
      credentials: "include"
    })
    .then(response => {
      if (!response.ok) {
        throw new Error("Error en la respuesta del servidor: " + response.status);
      }
      return response.json();
    })
    .then(data => {

      console.log("📦 Respuesta del servidor:", data);

      if (data.success) {

        const idUsuario = data.id_usuario || data.idUsuario;
        const nombreUsuario = data.nombre || data.nombreUsuario || "Usuario";
        const rolUsuario = data.rol || data.rolUsuario || "Usuario";

        if (idUsuario) {
          sessionStorage.setItem("idUsuario", idUsuario);
        }

        sessionStorage.setItem("nombreUsuario", nombreUsuario);
        sessionStorage.setItem("rolUsuario", rolUsuario);

        mostrarToast("Inicio de sesión exitoso", true, "irTablero");

      } else {
        mostrarToast(data.message || "Usuario o contraseña incorrectos", false);
      }

    })
    .catch(error => {
      console.error("❌ Error en la solicitud:", error);
      mostrarToast("Error de conexión con el servidor", false);
    });

  });

});