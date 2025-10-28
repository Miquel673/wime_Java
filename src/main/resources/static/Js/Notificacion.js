document.addEventListener("DOMContentLoaded", () => {
  // 🧠 Obtener el ID del usuario logueado desde sessionStorage
  const idUsuario = sessionStorage.getItem("idUsuario");
  console.log("🧩 ID del usuario logueado:", idUsuario);

  // 🚨 Validar si el usuario está logueado
  if (!idUsuario) {
    console.error("❌ No se encontró el ID del usuario en sessionStorage.");
    alert("No se detectó sesión activa. Por favor, inicia sesión nuevamente.");
    return;
  }

  // ✅ Solicitar permiso para notificaciones de escritorio
  solicitarPermisoNotificaciones();

  // 🔹 Cargar las notificaciones al iniciar
  cargarNotificaciones(idUsuario);

  // 🕒 Verificar cada 30 segundos si hay nuevas notificaciones
  setInterval(() => verificarNuevasNotificaciones(idUsuario), 30000);

  // 🟢 Asignar eventos a los botones globales
  const btnMarcarLeidas = document.getElementById("btnMarcarLeidas");
  const btnEliminarNotificaciones = document.getElementById("btnEliminarNotificaciones");

  if (btnMarcarLeidas) {
    btnMarcarLeidas.addEventListener("click", () => marcarComoLeidas(idUsuario));
  } else {
    console.warn("⚠️ No se encontró el botón 'Marcar como leídas'");
  }

  if (btnEliminarNotificaciones) {
    btnEliminarNotificaciones.addEventListener("click", () => eliminarNotificaciones(idUsuario));
  } else {
    console.warn("⚠️ No se encontró el botón 'Eliminar notificaciones'");
  }
});


// 🔹 Cargar notificaciones del usuario
async function cargarNotificaciones(idUsuario) {
  try {
    const response = await fetch(`http://localhost:8080/api/notificaciones/${idUsuario}`);
    if (!response.ok) throw new Error("Error al obtener notificaciones");

    const notificaciones = await response.json();
    console.log("📩 Notificaciones recibidas:", notificaciones);
    renderNotificaciones(notificaciones);
  } catch (error) {
    console.error("❌ Error al cargar notificaciones:", error);
  }
}


// 🟢 Marcar todas como leídas
async function marcarComoLeidas(idUsuario) {
  try {
    const response = await fetch(`http://localhost:8080/api/notificaciones/${idUsuario}/marcar-leidas`, {
      method: "PUT",
    });

    if (!response.ok) throw new Error("Error al marcar como leídas");
    console.log("✅ Notificaciones marcadas como leídas");
    cargarNotificaciones(idUsuario);
  } catch (error) {
    console.error("❌ Error al marcar como leídas:", error);
  }
}


// 🔴 Eliminar todas las notificaciones
async function eliminarNotificaciones(idUsuario) {
  if (!confirm("¿Seguro que quieres eliminar todas las notificaciones?")) return;

  try {
    const response = await fetch(`http://localhost:8080/api/notificaciones/${idUsuario}/eliminar-todas`, {
      method: "DELETE",
    });

    if (!response.ok) throw new Error("Error al eliminar notificaciones");
    console.log("🗑️ Notificaciones eliminadas correctamente");
    cargarNotificaciones(idUsuario);
  } catch (error) {
    console.error("❌ Error al eliminar notificaciones:", error);
  }
}


// 🧩 Renderizar las notificaciones en pantalla
function renderNotificaciones(notificaciones) {
  const contenedor = document.getElementById("listaNotificaciones");
  contenedor.innerHTML = "";

  if (!notificaciones || notificaciones.length === 0) {
    contenedor.innerHTML = "<p>No tienes notificaciones.</p>";
    return;
  }

  // 🔹 Ordenar las notificaciones por fecha (más recientes primero)
  notificaciones.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));

  // 🔹 Crear los elementos visuales
  notificaciones.forEach(n => {
    const item = document.createElement("div");
    item.classList.add("notificacion");

    item.innerHTML = `
      <strong>${n.tipo}</strong>
      <p>${n.mensaje}</p>
      <small>${new Date(n.fecha).toLocaleString()}</small>
      <p style="color:${n.leida ? "green" : "red"};">
        ${n.leida ? "Leída" : "No leída"}
      </p>
      <hr>
    `;

    contenedor.appendChild(item);
  });
}



// 🟢 Pedir permiso de notificación al navegador
function solicitarPermisoNotificaciones() {
  if (!("Notification" in window)) {
    console.warn("⚠️ El navegador no soporta notificaciones de escritorio");
    return;
  }

  if (Notification.permission === "default") {
    Notification.requestPermission().then((permiso) => {
      if (permiso === "granted") {
        console.log("✅ Permiso de notificaciones concedido");
      } else {
        console.warn("🚫 Permiso de notificaciones denegado");
      }
    });
  }
}


// 🔔 Mostrar una notificación de escritorio
function mostrarNotificacion(titulo, mensaje) {
  if (Notification.permission === "granted") {
    const opciones = {
      body: mensaje,
      icon: "/IMG/logo.png", // Cambia por el icono de tu proyecto
      vibrate: [200, 100, 200],
      tag: "wime-notif",
    };

    new Notification(titulo, opciones);
  }
}


// 🔍 Verificar nuevas notificaciones
async function verificarNuevasNotificaciones(idUsuario) {
  try {
    const resp = await fetch(`http://localhost:8080/api/notificaciones/${idUsuario}`);
    if (!resp.ok) throw new Error("Error al obtener nuevas notificaciones");

    const notificaciones = await resp.json();

    // Solo mostrar las no leídas
    notificaciones.forEach((n) => {
      if (!n.leida) {
        mostrarNotificacion(n.tipo, n.mensaje);
      }
    });
  } catch (error) {
    console.error("❌ Error verificando nuevas notificaciones:", error);
  }
}
