document.addEventListener("DOMContentLoaded", () => {
  const themeSwitch = document.getElementById("themeSwitch");
  const notifSwitch = document.getElementById("notifSwitch");
  const themeLabel = document.getElementById("themeLabel");
  const notifLabel = document.getElementById("notifLabel");
  const btnCuenta = document.getElementById("btnCuenta");

  // 🔹 Cargar preferencias guardadas
  const temaGuardado = localStorage.getItem("tema") || "claro";
  const notificacionesActivas = localStorage.getItem("notificaciones") === "true";

  // Aplicar el tema
  if (temaGuardado === "oscuro") {
    document.body.classList.add("dark-mode");
    themeSwitch.checked = true;
    themeLabel.textContent = "Modo oscuro";
  }

  // Aplicar estado de notificaciones
  notifSwitch.checked = notificacionesActivas;
  notifLabel.textContent = notificacionesActivas
    ? "Notificaciones activadas"
    : "Notificaciones desactivadas";

  // 🌓 Cambiar tema
  themeSwitch.addEventListener("change", () => {
    if (themeSwitch.checked) {
      document.body.classList.add("dark-mode");
      themeLabel.textContent = "Modo oscuro";
      localStorage.setItem("tema", "oscuro");
    } else {
      document.body.classList.remove("dark-mode");
      themeLabel.textContent = "Modo claro";
      localStorage.setItem("tema", "claro");
    }
  });

  // 🔔 Activar o desactivar notificaciones
  notifSwitch.addEventListener("change", async () => {
    if (notifSwitch.checked) {
      localStorage.setItem("notificaciones", "true");
      notifLabel.textContent = "Notificaciones activadas";
      if ("Notification" in window) {
        const permiso = await Notification.requestPermission();
        if (permiso === "granted") {
          new Notification("🔔 Notificaciones activadas", {
            body: "Recibirás alertas de nuevas tareas o rutinas.",
            icon: "/img/logo.png",
          });
        }
      }
    } else {
      localStorage.setItem("notificaciones", "false");
      notifLabel.textContent = "Notificaciones desactivadas";
    }
  });

  // 👤 Ir a configuración de cuenta
  btnCuenta.addEventListener("click", () => {
    window.location.href = "config_cuenta.html"; // aquí irá la interfaz de cambio de nombre/contraseña
  });
});



// 📸 Gestión de foto de perfil
const API_BASE = "http://localhost:8080/api/usuarios";
const idUsuario = sessionStorage.getItem("idUsuario"); // asegúrate que esté en sesión
const fotoPerfil = document.getElementById("fotoPerfil");
const inputFoto = document.getElementById("inputFoto");
const btnSubir = document.getElementById("btnSubirFoto");
const btnEliminar = document.getElementById("btnEliminarFoto");

// 🟢 Mostrar foto actual
async function cargarFotoPerfil() {
  try {
    const response = await fetch(`${API_BASE}/${idUsuario}/foto`);
    const data = await response.json();
    if (data.success) {
      fotoPerfil.src = data.urlFoto;
      console.log("📸 Foto cargada:", data.urlFoto);
    } else {
      console.warn("⚠️ No se encontró foto:", data.message);
    }
  } catch (error) {
    console.error("❌ Error al cargar foto:", error);
  }
}

// 🟡 Subir nueva foto
async function subirFoto(file) {
  const idUsuario = sessionStorage.getItem("idUsuario");

  if (!idUsuario) {
    console.error("⚠️ No se encontró idUsuario en sessionStorage");
    alert("No se ha identificado el usuario. Inicia sesión nuevamente.");
    return;
  }

  const formData = new FormData();
  formData.append("file", file);
  formData.append("idUsuario", idUsuario);

  console.log("📤 Enviando FormData:", [...formData.entries()]);
  
  try {
    const response = await fetch("http://localhost:8080/api/usuarios/subir-foto", {
      method: "POST",
      body: formData,
      credentials: "include"
    });

    const data = await response.json();

    if (data.success) {
      console.log("✅ Foto subida correctamente:", data.urlFoto);

      const avatarImg = document.getElementById("foto-perfil");
      if (avatarImg) {
        avatarImg.src = data.urlFoto.startsWith("http")
          ? data.urlFoto
          : `http://localhost:8080/${data.urlFoto}`;
      }
    } else {
      console.error("❌ Error del servidor:", data.message);
    }
  } catch (error) {
    console.error("❌ Error al subir la foto:", error);
  }
}

// 🔴 Eliminar foto
async function eliminarFoto() {
  if (!confirm("¿Seguro que deseas eliminar la foto de perfil?")) return;
  try {
    const response = await fetch(`${API_BASE}/${idUsuario}/eliminar-foto`, {
      method: "DELETE"
    });

    if (response.ok) {
      alert("🗑️ Foto eliminada.");
      cargarFotoPerfil();
    } else {
      console.error("❌ Error al eliminar la foto:", response.status);
    }
  } catch (error) {
    console.error("❌ Error en la eliminación:", error);
  }
}

// 🎯 Listeners
btnSubir.addEventListener("click", () => inputFoto.click());
inputFoto.addEventListener("change", (e) => {
  const file = e.target.files[0];
  if (file) subirFoto(file);
});
btnEliminar.addEventListener("click", eliminarFoto);

// 🚀 Inicializar
document.addEventListener("DOMContentLoaded", cargarFotoPerfil);



async function cargarDatosUsuario(idUsuario) {
  try {
    const response = await fetch(`http://localhost:8080/api/usuarios/${idUsuario}`, {
      credentials: "include"
    });
    const data = await response.json();

    const avatarImg = document.getElementById("foto-perfil");
    if (data.fotoPerfil && avatarImg) {
      avatarImg.src = data.fotoPerfil.startsWith("http")
        ? data.fotoPerfil
        : `http://localhost:8080/${data.fotoPerfil}`;
    }
  } catch (error) {
    console.error("❌ Error al cargar los datos del usuario:", error);
  }
}

