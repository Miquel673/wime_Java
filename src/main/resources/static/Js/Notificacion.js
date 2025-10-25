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

  // 🔹 Cargar las notificaciones al iniciar
  cargarNotificaciones(idUsuario);

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
