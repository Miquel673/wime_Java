document.addEventListener("DOMContentLoaded", () => {

  const themeSwitch = document.getElementById("themeSwitch");
  const themeLabel = document.getElementById("themeLabel");

  if (themeSwitch && themeLabel) {
    const temaGuardado = localStorage.getItem("tema") || "claro";

    if (temaGuardado === "oscuro") {
      document.body.classList.add("dark-mode");
      themeSwitch.checked = true;
      themeLabel.textContent = "Modo oscuro";
    }

    themeSwitch.addEventListener("change", () => {
      const oscuro = themeSwitch.checked;
      document.body.classList.toggle("dark-mode", oscuro);
      themeLabel.textContent = oscuro ? "Modo oscuro" : "Modo claro";
      localStorage.setItem("tema", oscuro ? "oscuro" : "claro");
    });
  }

});

const notifSwitch = document.getElementById("notifSwitch");
const notifLabel = document.getElementById("notifLabel");

if (notifSwitch && notifLabel) {
  const estado = localStorage.getItem("notificaciones") === "true";
  notifSwitch.checked = estado;
  notifLabel.textContent = estado
    ? "Notificaciones activadas"
    : "Notificaciones desactivadas";

  notifSwitch.addEventListener("change", async () => {
    const activo = notifSwitch.checked;
    localStorage.setItem("notificaciones", activo);
    notifLabel.textContent = activo
      ? "Notificaciones activadas"
      : "Notificaciones desactivadas";

    if (activo && "Notification" in window) {
      const permiso = await Notification.requestPermission();
      if (permiso === "granted") {
        new Notification("🔔 Notificaciones activadas");
      }
    }
  });
}

document.addEventListener("DOMContentLoaded", () => {

  const idUsuario = sessionStorage.getItem("idUsuario");

  const fotoPerfil = document.getElementById("fotoPerfil");
  const inputFoto = document.getElementById("inputFoto");
  const btnSubirFoto = document.getElementById("btnSubirFoto");
  const btnEliminarFoto = document.getElementById("btnEliminarFoto");

  const nombreInput = document.getElementById("nombreUsuario");
  const btnGuardarNombre = document.getElementById("btnGuardarNombre");
  const msgNombre = document.getElementById("msgNombre");

  if (!idUsuario) {
    console.warn("⚠️ Usuario no autenticado");
    return;
  }

  /* ======================
     FOTO DE PERFIL
  ====================== */

  async function cargarFotoPerfil() {
    try {
      const res = await fetch(
        `/api/usuarios/${idUsuario}/foto`,
        { credentials: "include" }
      );

      const data = await res.json();
      fotoPerfil.src = data.fotoPerfil + `?t=${Date.now()}`;

    } catch (e) {
      console.error("❌ Error cargando foto", e);
    }
  }

  btnSubirFoto.addEventListener("click", () => inputFoto.click());

  inputFoto.addEventListener("change", async () => {
    const file = inputFoto.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("idUsuario", idUsuario);

    const res = await fetch(
      "/api/usuarios/subir-foto",
      {
        method: "POST",
        body: formData,
        credentials: "include"
      }
    );

    const data = await res.json();
    if (data.success) {
      fotoPerfil.src = data.urlFoto + `?t=${Date.now()}`;
    }
  });

  btnEliminarFoto.addEventListener("click", async () => {
    if (!confirm("¿Eliminar foto de perfil?")) return;

    await fetch(
      `/api/usuarios/${idUsuario}/eliminar-foto`,
      { method: "DELETE", credentials: "include" }
    );

    fotoPerfil.src =
      "/IMG/vector-de-perfil-avatar-predeterminado-foto-usuario-medios-sociales-icono-183042379.jpeg";
  });

  /* ======================
     NOMBRE DE USUARIO
  ====================== */

  async function cargarNombre() {
    const res = await fetch(
      "/api/auth/check-session",
      { credentials: "include" }
    );

    const data = await res.json();
    if (data.active) {
      nombreInput.value = data.usuario;
    }
  }

  btnGuardarNombre.addEventListener("click", async () => {
    const nuevoNombre = nombreInput.value.trim();

    if (!nuevoNombre) {
      msgNombre.textContent = "⚠️ El nombre no puede estar vacío";
      msgNombre.style.color = "red";
      return;
    }

    const res = await fetch(
      `/api/usuarios/${idUsuario}/actualizar-nombre`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ nombre: nuevoNombre })
      }
    );

    const data = await res.json();
    if (data.success) {
      msgNombre.textContent = "✅ Nombre actualizado";
      msgNombre.style.color = "green";
    } else {
      msgNombre.textContent = data.message;
      msgNombre.style.color = "red";
    }
  });

  /* INIT */
  cargarFotoPerfil();
  cargarNombre();
});
