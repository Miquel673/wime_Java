const usuario = JSON.parse(localStorage.getItem("usuario"));

let db = [];
let navDate = new Date();
let filtroActual = "todas";
let filtroRendimientoActual = "todas";

const MAP_PRIORIDAD_RUTINA = {
  alta: "danger",
  media: "warning",
  baja: "success",
  rojo: "danger",
  amarillo: "warning",
  verde: "success"
};

async function verificarSesionTablero() {
  try {
    const res = await fetch("/api/auth/check-session", {
      method: "GET",
      credentials: "include",
      cache: "no-store"
    });
    const data = await res.json();

    if (!data.active) {
      window.location.replace("/");
      return null;
    }

    return data;
  } catch (err) {
    console.error(" Error verificando sesión:", err);
    window.location.replace("/");
    return null;
  }
}

window.addEventListener("pageshow", () => {
  verificarSesionTablero();
});

document.addEventListener("DOMContentLoaded", async () => {
  cargarEstadisticasTablero();

  const data = await verificarSesionTablero();
  if (!data) return;

  const bienvenida = document.getElementById("bienvenida");
  if (bienvenida) bienvenida.innerText = `Bienvenido, ${data.usuario}`;

  const idUsuario = data.idUsuario || sessionStorage.getItem("idUsuario");
  if (idUsuario) {
    sessionStorage.setItem("idUsuario", idUsuario);
    cargarFotoPerfil(idUsuario);
  }  cargarEstadisticasTablero();


  initCalendar();
  cargarItems();

  const fechaActual = document.getElementById("fecha-actual");
  if (fechaActual) {
    fechaActual.textContent = new Date().toLocaleDateString("es-ES", {
      weekday: "long",
      day: "numeric",
      month: "long"
    });
  }

  if (localStorage.getItem("theme") === "dark") {
    document.body.classList.add("dark");
    document.getElementById("themeIcon").className = "bi bi-moon-stars-fill";
  }

  document.getElementById("themeToggle")?.addEventListener("click", () => {
    const isDark = document.body.classList.toggle("dark");
    localStorage.setItem("theme", isDark ? "dark" : "light");
    document.getElementById("themeIcon").className = isDark ? "bi bi-moon-stars-fill" : "bi bi-sun";
    renderItems();
  });

  document.getElementById("btn-rendimiento-todas")?.addEventListener("click", () => cambiarFiltroRendimiento("todas"));
  document.getElementById("btn-rendimiento-rutina")?.addEventListener("click", () => cambiarFiltroRendimiento("rutina"));
  document.getElementById("btn-rendimiento-tarea")?.addEventListener("click", () => cambiarFiltroRendimiento("tarea"));
});

async function cargarFotoPerfil(idUsuario) {
  try {
    const res = await fetch(`/api/usuarios/${idUsuario}/foto`, { credentials: "include" });
    const data = await res.json();
    if (data.fotoPerfil && document.getElementById("fotoPerfil")) {
      document.getElementById("fotoPerfil").src = data.fotoPerfil + "?t=" + Date.now();
    }
  } catch (e) {
    console.error(" Error cargando foto de perfil", e);
  }
}

async function cargarItems() {
  try {
    const [resTareas, resRutinas] = await Promise.all([
      fetch("/api/tareas/listar", { credentials: "include" }),
      fetch("/api/rutinas/listar", { credentials: "include" })
    ]);

    const tareasJSON = await resTareas.json();
    const rutinasJSON = await resRutinas.json();
    const tareas = tareasJSON.tareas || [];
    const rutinas = rutinasJSON.rutinas || [];

    db = [
      ...tareas.map(t => ({
        id: t.idTarea ?? t.id,
        tipo: "tarea",
        titulo: t.titulo,
        descripcion: t.descripcion,
        prioridad: t.prioridad,
        estado: normalizarEstado(t.estado),
        fechaLimite: t.fechaLimite || null,
        fechaAsignacion: t.fechaAsignacion || null,
        frecuencia: null,
        esCompartida: t.esCompartida || false,
        nombreCreador: t.nombreCreador || null,
        imagenPerfilCreador: t.imagenPerfilCreador || null
      })),
      ...rutinas.map(r => ({
        id: r.idRutina ?? r.id,
        tipo: "rutina",
        titulo: r.nombreRutina ?? r.titulo,
        descripcion: r.descripcion,
        prioridad: r.prioridad || "baja",
        estado: normalizarEstado(r.estado),
        fechaLimite: r.fechaFin || null,
        fechaAsignacion: r.fechaAsignacion || null,
        frecuencia: r.frecuencia || null,
        esCompartida: r.esCompartida || false,
        nombreCreador: r.nombreCreador || null,
        imagenPerfilCreador: r.imagenPerfilCreador || null
      }))
    ];

    renderItems();
    updateStats();
  } catch (error) {
    console.error("Error cargando items:", error);
  }
}

function filtrar(tipo, boton) {
  filtroActual = tipo;
  document.querySelectorAll("#filter-group .btn").forEach(btn => btn.classList.remove("active"));
  boton?.classList.add("active");
  renderItems();
}

function cambiarFiltroRendimiento(tipo) {
  filtroRendimientoActual = tipo;
  document.querySelectorAll("#performance-filter-group .btn").forEach(btn => btn.classList.remove("active"));
  document.getElementById(`btn-rendimiento-${tipo}`)?.classList.add("active");
  updateStats();
}

function getColorPorPrioridad(prioridad) {
  return MAP_PRIORIDAD_RUTINA[(prioridad || "").toLowerCase()] || "secondary";
}

function getColorPorEstado(estado) {
  const map = {
    pendiente: "secondary",
    "en progreso": "primary",
    en_progreso: "primary",
    completada: "success",
    vencida: "danger"
  };
  return map[(estado || "").toLowerCase()] || "secondary";
}

function normalizarEstado(estado) {
  return (estado || "pendiente").toLowerCase().replaceAll("_", " ");
}

function formatearEstado(estado) {
  const normalizado = normalizarEstado(estado);
  return normalizado.charAt(0).toUpperCase() + normalizado.slice(1);
}

function formatearFrecuencia(frecuencia) {
  const mapa = { diario: "Diaria", diaria: "Diaria", semanal: "Semanal", mensual: "Mensual" };
  return mapa[(frecuencia || "").toLowerCase()] || "No definida";
}

function filtrarItemsActuales() {
  if (filtroActual === "todas") return db;
  return db.filter(item => item.tipo === filtroActual);
}

function renderItems() {
  const contenedor = document.getElementById("contenedor-items");
  if (!contenedor) return;

  const items = filtrarItemsActuales();
  contenedor.innerHTML = "";

  if (!items.length) {
    contenedor.innerHTML = '<div class="col-12"><p class="text-center mb-0">No hay elementos para este filtro.</p></div>';
    return;
  }

  items.forEach(item => {
    const tarjeta = document.createElement("div");
    tarjeta.className = "col mb-3";

    const creadorHtml = item.esCompartida
      ? `<div class="d-flex align-items-center mb-2">
          <img src="${item.imagenPerfilCreador || 'https://ui-avatars.com/api/?name=' + (item.nombreCreador || 'Usuario')}" class="rounded-circle me-2" width="30" height="30">
          <small class="text-muted">Compartida por ${item.nombreCreador || "Usuario no encontrado"}</small>
        </div>`
      : "";

    const camposRutina = item.tipo === "rutina"
      ? `
      <p class="card-text"><strong>Frecuencia:</strong> ${formatearFrecuencia(item.frecuencia)}</p>
      <p class="card-text"><strong>Fecha de asignación:</strong> ${item.fechaAsignacion || "N/A"}</p>
      <p class="card-text"><strong>Fecha fin:</strong> ${item.fechaLimite || "N/A"}</p>`
      : `
      <p class="card-text"><strong>Prioridad:</strong> ${item.prioridad || "N/A"}</p>
      <p class="card-text"><strong>Fecha límite:</strong> ${item.fechaLimite || "N/A"}</p>`;

    const acciones = item.tipo === "tarea"
      ? item.esCompartida
        ? `<button class="btn btn-sm btn-outline-warning btn-remover" data-id="${item.id}" title="Quitar de mi lista"><i class="bi bi-person-dash"></i></button>`
        : `<div class="d-flex gap-2 align-items-center">
            <button class="btn-icon-editar btn-editar" data-id="${item.id}" title="Editar tarea"><i class="bi bi-pencil-square"></i></button>
            <button class="btn-icon-eliminar btn-eliminar" data-id="${item.id}" title="Eliminar tarea"><i class="bi bi-trash"></i></button>
          </div>`
      : item.esCompartida
        ? `<button class="btn btn-sm btn-outline-warning btn-remover-rutina" data-id="${item.id}" title="Quitar de mi lista"><i class="bi bi-person-dash"></i></button>`
        : `<div class="d-flex gap-2 align-items-center">
            <button class="btn-icon-editar btn-editar-rutina" data-id="${item.id}" title="Editar rutina"><i class="bi bi-pencil-square"></i></button>
            <button class="btn-icon-eliminar btn-eliminar-rutina" data-id="${item.id}" title="Eliminar rutina"><i class="bi bi-trash"></i></button>
          </div>`;

    const onClickEstado = item.tipo === "tarea" ? "cambiarEstadoTarea" : "cambiarEstadoRutina";

    tarjeta.innerHTML = `
      <div class="card shadow-sm h-100 wime-card" id="${item.tipo}-${item.id}">
        <div class="wime-card-header">${item.titulo}</div>
        <div class="wime-card-body">
          ${creadorHtml}
          ${camposRutina}
          <p class="card-text">${item.descripcion || "Sin descripción"}</p>
          <div class="dropdown mt-2 d-flex justify-content-between align-items-center gap-2">
            <button class="badge border-0 bg-${getColorPorEstado(item.estado)} dropdown-toggle" style="font-size:0.9rem; cursor:pointer;" data-bs-toggle="dropdown">${formatearEstado(item.estado)}</button>
            ${acciones}
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" onclick="${onClickEstado}(${item.id}, 'pendiente')">Pendiente</a></li>
              <li><a class="dropdown-item" onclick="${onClickEstado}(${item.id}, 'en_progreso')">En progreso</a></li>
              <li><a class="dropdown-item" onclick="${onClickEstado}(${item.id}, 'completada')">Completada</a></li>
            </ul>
          </div>
        </div>
      </div>`;

    if (item.estado === "vencida") tarjeta.querySelector(".card")?.classList.add("vencida");
    if (item.esCompartida) tarjeta.querySelector(".card")?.classList.add("wime-card-compartida");
    contenedor.appendChild(tarjeta);
  });
}

async function cambiarEstadoTarea(id, nuevoEstado) {
  await cambiarEstadoGenerico(`/api/tareas/${id}/estado`, nuevoEstado);
}

async function cambiarEstadoRutina(id, nuevoEstado) {
  await cambiarEstadoGenerico(`/api/rutinas/${id}/estado`, nuevoEstado);
}

async function cambiarEstadoGenerico(url, nuevoEstado) {
  try {
    const res = await fetch(url, {
      method: "PUT",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ estado: nuevoEstado })
    });
    const data = await res.json();
    if (!data.success) throw new Error(data.message || "No se pudo actualizar");
    await cargarItems();
    await cargarEstadisticasTablero();
  } catch (error) {
    console.error("Error cambiando estado:", error);
    if (typeof mostrarToast === "function") mostrarToast("No se pudo cambiar el estado", "error");
  }
}

document.addEventListener("click", async (e) => {
  const btnEliminar = e.target.closest(".btn-eliminar");
  if (btnEliminar) {
    const id = btnEliminar.dataset.id;
    if (!confirm("¿Eliminar esta tarea?")) return;
    const res = await fetch(`/api/tareas/eliminar/${id}`, { method: "DELETE", credentials: "include" });
    const data = await res.json();
    if (data.success) {
      await cargarItems();
      await cargarEstadisticasTablero();
    } else {
      alert(data.message);
    }
    return;
  }

  const btnRemover = e.target.closest(".btn-remover");
  if (btnRemover) {
    const id = btnRemover.dataset.id;
    if (!confirm("¿Quitar esta tarea de tu lista?")) return;
    const res = await fetch(`/api/tareas/remover-compartida/${id}`, { method: "DELETE", credentials: "include" });
    const data = await res.json();
    if (data.success) {
      await cargarItems();
      await cargarEstadisticasTablero();
    } else {
      alert(data.message);
    }
    return;
  }

  const btnEliminarRutina = e.target.closest(".btn-eliminar-rutina");
  if (btnEliminarRutina) {
    const id = btnEliminarRutina.dataset.id;
    if (!confirm("¿Eliminar esta rutina?")) return;
    const res = await fetch(`/api/rutinas/eliminar/${id}`, { method: "DELETE", credentials: "include" });
    const data = await res.json();
    if (data.success) {
      await cargarItems();
      await cargarEstadisticasTablero();
    } else {
      alert(data.message);
    }
    return;
  }

  const btnRemoverRutina = e.target.closest(".btn-remover-rutina");
  if (btnRemoverRutina) {
    const id = btnRemoverRutina.dataset.id;
    if (!confirm("¿Quitar esta rutina de tu lista?")) return;
    const res = await fetch(`/api/rutinas/remover-compartida/${id}`, { method: "DELETE", credentials: "include" });
    const data = await res.json();
    if (data.success) {
      await cargarItems();
      await cargarEstadisticasTablero();
    } else {
      alert(data.message);
    }
  }
});

function updateStats() {
  const items = filtroRendimientoActual === "todas" ? db : db.filter(item => item.tipo === filtroRendimientoActual);
  const total = items.length;
  const completadas = items.filter(item => item.estado === "completada").length;
  const pendientes = items.filter(item => item.estado === "pendiente" || item.estado === "en progreso").length;
  const hoy = new Date();
  hoy.setHours(0, 0, 0, 0);
  const vencidas = items.filter(item => {
    if (item.tipo !== "tarea" || !item.fechaLimite) return false;
    const fecha = new Date(item.fechaLimite);
    fecha.setHours(0, 0, 0, 0);
    return fecha < hoy && item.estado !== "completada";
  }).length;

  pintarStats(total, completadas, pendientes, vencidas);
}

async function cargarEstadisticasTablero() {
  try {
    const res = await fetch(`/api/estadisticas-tablero?tipo=${filtroRendimientoActual}`, { credentials: "include" });
    const data = await res.json();
    if (!data.success) return;
    pintarStats(data.total || 0, data.completadas || 0, data.pendientes || 0, data.vencidas || 0);
  } catch (error) {
    console.error("Error cargando estadísticas:", error);
  }
}

function pintarStats(total, completadas, pendientes, vencidas) {
  const porcCompletadas = total ? Math.round((completadas / total) * 100) : 0;
  const porcPendientes = total ? Math.round((pendientes / total) * 100) : 0;
  const porcVencidas = total ? Math.round((vencidas / total) * 100) : 0;

  document.getElementById("stat-completadas").innerText = porcCompletadas + "%";
  document.getElementById("bar-completadas").style.width = porcCompletadas + "%";
  document.getElementById("stat-pendientes").innerText = porcPendientes + "%";
  document.getElementById("bar-pendientes").style.width = porcPendientes + "%";
  document.getElementById("stat-vencidas").innerText = porcVencidas + "%";
  document.getElementById("bar-vencidas").style.width = porcVencidas + "%";
}

async function cerrarSesion() {
  if (!confirm("¿Cerrar sesión?")) return;

  try {
    await fetch("/api/auth/logout", {
      method: "POST",
      credentials: "include",
      cache: "no-store"
    });
  } catch (e) {
    console.warn("No se pudo invalidar sesión en backend:", e);
  }

  sessionStorage.clear();
  localStorage.removeItem("usuario");
  window.location.replace("/");
}