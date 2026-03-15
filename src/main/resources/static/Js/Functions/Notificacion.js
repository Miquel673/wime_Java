document.addEventListener("DOMContentLoaded", () => {

  const idUsuario = sessionStorage.getItem("idUsuario");

  if (!idUsuario) {
    console.error("No hay sesión activa");
    return;
  }

  const btnMarcarLeidas = document.getElementById("btnMarcarLeidas");
  const btnEliminarNotificaciones = document.getElementById("btnEliminarNotificaciones");

  if (btnMarcarLeidas) {

    btnMarcarLeidas.addEventListener("click", () => {
      console.log("Marcando notificaciones como leídas...");
      marcarComoLeidas(idUsuario);
    });

  }

  if (btnEliminarNotificaciones) {

    btnEliminarNotificaciones.addEventListener("click", () => {
      console.log("Eliminando notificaciones...");
      eliminarNotificaciones(idUsuario);
    });

  }

});



// 🔹 Cargar notificaciones del usuario
async function cargarNotificaciones(idUsuario) {
  try {
    const response = await fetch(`/api/notificaciones/${idUsuario}`);
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
    const response = await fetch(`/api/notificaciones/${idUsuario}/marcar-leidas`, {
      method: "PUT",
    });

    if (!response.ok) throw new Error("Error al marcar como leídas");
    console.log(" Notificaciones marcadas como leídas");
    cargarNotificaciones(idUsuario);
  } catch (error) {
    console.error("❌ Error al marcar como leídas:", error);
  }
}


// 🔴 Eliminar todas las notificaciones
async function eliminarNotificaciones(idUsuario) {
  if (!confirm("¿Seguro que quieres eliminar todas las notificaciones?")) return;

  try {
    const response = await fetch(`/api/notificaciones/${idUsuario}/eliminar-todas`, {
      method: "DELETE",
    });

    if (!response.ok) throw new Error("Error al eliminar notificaciones");
    console.log("🗑️ Notificaciones eliminadas correctamente");
    cargarNotificaciones(idUsuario);
  } catch (error) {
    console.error("❌ Error al eliminar notificaciones:", error);
  }
}


// 🧩 Renderizar las notificaciones en pantalla (más recientes primero y con estilo)
function renderNotificaciones(notificaciones) {

  const contenedor = document.getElementById("listaNotificaciones");
  contenedor.innerHTML = "";

  if (!notificaciones || notificaciones.length === 0) {

    contenedor.innerHTML = `
      <p class="text-center text-muted small">
        No tienes notificaciones.
      </p>
    `;
    return;
  }

  // ordenar por fecha más reciente
  notificaciones.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));

const limite = 10;

notificaciones
  .sort((a, b) => new Date(b.fecha) - new Date(a.fecha))
  .slice(0, limite)
  .forEach(n => {

    let icono = "bi-bell";
    let color = "text-secondary";

    // 🎨 iconos según tipo
    if (n.tipo.toLowerCase().includes("tarea")) {
      icono = "bi-check-circle";
      color = "text-success";
    }

    if (n.tipo.toLowerCase().includes("rutina")) {
      icono = "bi-clock";
      color = "text-primary";
    }

    if (n.tipo.toLowerCase().includes("alerta")) {
      icono = "bi-exclamation-circle";
      color = "text-warning";
    }

    const item = document.createElement("div");

    item.className = `
      p-3 mb-2 d-flex align-items-center notification-item
      ${!n.leida ? "bg-light rounded" : ""}
    `;

    item.innerHTML = `
      <i class="bi ${icono} ${color} me-3 fs-4"></i>

      <div class="flex-grow-1">
        <h6 class="m-0 fw-bold small">
          ${n.tipo}:
          <span class="fw-normal">${n.mensaje}</span>
        </h6>

        <small class="text-muted">
          ${new Date(n.fecha).toLocaleString()}
        </small>
      </div>
    `;

    contenedor.appendChild(item);
  });

  if (notificaciones.length > 10) {

  const aviso = document.createElement("p");
  aviso.className = "text-center text-muted small mt-2";
  aviso.innerText = "Mostrando las últimas 10 notificaciones";

  contenedor.appendChild(aviso);
}


}

const modalNotif = document.getElementById("modalNotif");

if (modalNotif) {

  modalNotif.addEventListener("show.bs.modal", () => {

    const idUsuario = sessionStorage.getItem("idUsuario");

    if (idUsuario) {
      cargarNotificaciones(idUsuario);
    }

  });

}


// 🔔 Mostrar una notificación de escritorio  !!! REVISION POR CONSUMO DE RECURSOS !!!
function mostrarNotificacion(titulo, mensaje) {
  if (Notification.permission === "granted") {
    const opciones = {
      body: mensaje,
      icon: "../../IMG/logo_Wime.png", // Cambia por el icono de tu proyecto
      vibrate: [200, 100, 200],
      tag: "wime-notif",
    };

    new Notification(titulo, opciones);
  }
}


// 🔍 Verificar nuevas notificaciones
async function verificarNuevasNotificaciones(idUsuario) {
  try {
    const resp = await fetch(`/api/notificaciones/${idUsuario}`);
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

