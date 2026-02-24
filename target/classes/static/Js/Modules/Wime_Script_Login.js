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
          mostrarToast("Inicio de sesión exitoso", true, true);

          // 🧠 Detectar el ID correctamente (soporta ambos nombres)
          const idUsuario = data.id_usuario || data.idUsuario;
          const nombreUsuario = data.nombre || data.nombreUsuario || "Usuario";
          const rolUsuario = data.rol || data.rolUsuario || "Usuario";

          if (idUsuario) {
            sessionStorage.setItem("idUsuario", idUsuario);
            console.log("🟢 ID de usuario guardado:", idUsuario);
          } else {
            console.warn("⚠️ No se recibió un ID de usuario en la respuesta del backend.");
          }

          // Guardar otros datos del usuario
          sessionStorage.setItem("nombreUsuario", nombreUsuario);
          sessionStorage.setItem("rolUsuario", rolUsuario);

          console.log("👤 Usuario guardado en sesión:", {
            idUsuario,
            nombreUsuario,
            rolUsuario
          });


          

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

function mostrarToast(mensaje, esExito = true, redirigir = false) {
  const toastEl = document.getElementById("mensaje-flotante");
  const texto = document.getElementById("texto-toast");

  texto.textContent = mensaje;

  toastEl.classList.remove("text-bg-success", "text-bg-danger");
  toastEl.classList.add(esExito ? "text-bg-success" : "text-bg-danger");

  const toast = new bootstrap.Toast(toastEl, {
    delay: 2500
  });

  toast.show();

  toastEl.addEventListener("hidden.bs.toast", function () {
    if (redirigir && esExito) {
      window.location.href = "../../HTML/Interfaces/Wime_interfaz_Tablero.html";
    }
  }, { once: true });
}

function ocultarToast() {
  document.getElementById("mensaje-flotante").classList.add("d-none");
}
