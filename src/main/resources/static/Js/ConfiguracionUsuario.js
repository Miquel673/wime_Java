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

