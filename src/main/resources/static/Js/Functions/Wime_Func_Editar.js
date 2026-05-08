document.addEventListener("DOMContentLoaded", () => {
  const modalTareaEl = document.getElementById("modalEditarTarea");
  const modalRutinaEl = document.getElementById("modalEditarRutina");

  const modalEditarTarea = modalTareaEl ? new bootstrap.Modal(modalTareaEl) : null;
  const modalEditarRutina = modalRutinaEl ? new bootstrap.Modal(modalRutinaEl) : null;

  if (typeof configurarRestriccionFechasRutina === "function") {
    configurarRestriccionFechasRutina("edit-routine-fecha-inicio", "edit-routine-fecha-fin");
  }

  document.addEventListener("click", async (e) => {
    const btnEditarTarea = e.target.closest(".btn-editar");
    if (btnEditarTarea) {
      const id = btnEditarTarea.dataset.id;
      if (id && modalEditarTarea) await cargarDatosTarea(id, modalEditarTarea);
      return;
    }

    const btnEditarRutina = e.target.closest(".btn-editar-rutina");
    if (btnEditarRutina) {
      const id = btnEditarRutina.dataset.id;
      if (id && modalEditarRutina) await cargarDatosRutina(id, modalEditarRutina);
    }
  });

  document.getElementById("btn-guardar-edicion")?.addEventListener("click", guardarEdicionTarea);
  document.getElementById("btn-compartir-edicion")?.addEventListener("click", compartirTareaDesdeModal);
  document.getElementById("btn-guardar-edicion-rutina")?.addEventListener("click", guardarEdicionRutina);
  document.getElementById("btn-compartir-rutina-edicion")?.addEventListener("click", compartirRutinaDesdeModal);
});

async function cargarDatosTarea(idTarea, modalEditar) {
  try {
    const res = await fetch(`/api/tareas/${idTarea}`, { method: "GET", credentials: "include" });
    const data = await res.json();

    if (!res.ok || !data.success || !data.tarea) {
      mostrarToast("No se pudo cargar la tarea para edición");
      return;
    }

    const tarea = data.tarea;
    document.getElementById("edit-task-id").value = tarea.idTarea;
    document.getElementById("edit-task-titulo").value = tarea.titulo || "";
    document.getElementById("edit-task-descripcion").value = tarea.descripcion || "";
    document.getElementById("edit-task-prioridad").value = normalizarPrioridad(tarea.prioridad);
    document.getElementById("edit-task-fecha").value = tarea.fechaLimite || "";
    document.getElementById("edit-task-estado").value = tarea.estado || "pendiente";
    document.getElementById("edit-task-share-email").value = "";
    modalEditar.show();
  } catch (error) {
    console.error("Error cargando tarea para editar:", error);
    mostrarToast("Error al abrir el editor de tarea");
  }
}

async function cargarDatosRutina(idRutina, modalEditar) {
  try {
    const res = await fetch(`/api/rutinas/${idRutina}`, { method: "GET", credentials: "include" });
    const data = await res.json();
    const rutina = data?.rutina || data;

    if (!res.ok || !rutina || !rutina.idRutina) {
      mostrarToast(data?.message || "No se pudo cargar la rutina para edición");
      return;
    }

    document.getElementById("edit-routine-id").value = rutina.idRutina;
    document.getElementById("edit-routine-nombre").value = rutina.nombreRutina || "";
    document.getElementById("edit-routine-descripcion").value = rutina.descripcion || "";
    document.getElementById("edit-routine-prioridad").value = normalizarPrioridad(rutina.prioridad);
    document.getElementById("edit-routine-frecuencia").value = normalizarFrecuencia(rutina.frecuencia);
    document.getElementById("edit-routine-fecha-inicio").value = rutina.fechaAsignacion || "";
    document.getElementById("edit-routine-fecha-fin").value = rutina.fechaFin || "";
    document.getElementById("edit-routine-estado").value = rutina.estado || "pendiente";
    document.getElementById("edit-routine-share-email").value = "";

    if (typeof configurarRestriccionFechasRutina === "function") {
      configurarRestriccionFechasRutina("edit-routine-fecha-inicio", "edit-routine-fecha-fin");
    }

    modalEditar.show();
  } catch (error) {
    console.error("Error cargando rutina para editar:", error);
    mostrarToast("Error al abrir el editor de rutina");
  }
}

async function guardarEdicionTarea() {
  const id = document.getElementById("edit-task-id").value;
  const payload = {
    titulo: document.getElementById("edit-task-titulo").value.trim(),
    descripcion: document.getElementById("edit-task-descripcion").value.trim(),
    prioridad: document.getElementById("edit-task-prioridad").value,
    fechaLimite: document.getElementById("edit-task-fecha").value || null,
    estado: document.getElementById("edit-task-estado").value || "pendiente"
  };

  if (!id || !payload.titulo) {
    mostrarToast("El título de la tarea es obligatorio");
    return;
  }

    if (payload.fechaLimite && typeof obtenerFechaActualISO === "function" && payload.fechaLimite < obtenerFechaActualISO()) {
    mostrarToast("La fecha límite no puede ser anterior a la fecha actual");
    return;
  }


  try {
    const res = await fetch(`/api/tareas/editar/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(payload)
    });
    const data = await res.json();

    if (!res.ok || !data.success) {
      mostrarToast(data.message || "No se pudo actualizar la tarea");
      return;
    }

    mostrarToast("Tarea actualizada correctamente");
    bootstrap.Modal.getInstance(document.getElementById("modalEditarTarea"))?.hide();
    if (typeof cargarItems === "function") await cargarItems();
  } catch (error) {
    console.error("Error guardando edición:", error);
    mostrarToast("Error al actualizar la tarea");
  }
}

async function guardarEdicionRutina() {
  const id = document.getElementById("edit-routine-id").value;
  const fechaAsignacion = document.getElementById("edit-routine-fecha-inicio").value || null;
  const fechaFin = document.getElementById("edit-routine-fecha-fin").value || null;
  const payload = {
    nombreRutina: document.getElementById("edit-routine-nombre").value.trim(),
    descripcion: document.getElementById("edit-routine-descripcion").value.trim(),
    prioridad: document.getElementById("edit-routine-prioridad").value,
    frecuencia: document.getElementById("edit-routine-frecuencia").value,
    fechaAsignacion,
    fechaFin,
    estado: document.getElementById("edit-routine-estado").value || "pendiente"
  };

  if (!id || !payload.nombreRutina || !fechaAsignacion || !fechaFin) {
    mostrarToast("Debes completar nombre y fechas de la rutina");
    return;
  }

    if (typeof obtenerFechaActualISO === "function" && fechaAsignacion < obtenerFechaActualISO()) {
    mostrarToast("La fecha de inicio no puede ser anterior a la fecha actual");
    return;
  }

  if (typeof obtenerFechaActualISO === "function" && fechaFin < obtenerFechaActualISO()) {
    mostrarToast("La fecha final no puede ser anterior a la fecha actual");
    return;
  }


  if (fechaFin < fechaAsignacion) {
    mostrarToast("La fecha final no puede ser anterior a la fecha de inicio");
    return;
  }

  try {
    const res = await fetch(`/api/rutinas/editar/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(payload)
    });
    const data = await res.json();

    if (!res.ok || !data.success) {
      mostrarToast(data.message || "No se pudo actualizar la rutina");
      return;
    }

    mostrarToast("Rutina actualizada correctamente");
    bootstrap.Modal.getInstance(document.getElementById("modalEditarRutina"))?.hide();
    if (typeof cargarItems === "function") {
      await cargarItems();
      await cargarEstadisticasTablero();
    }
  } catch (error) {
    console.error("Error guardando edición de rutina:", error);
    mostrarToast("Error al actualizar la rutina");
  }
}

async function compartirTareaDesdeModal() {
  const id = document.getElementById("edit-task-id").value;
  const emails = document.getElementById("edit-task-share-email").value.trim();
  if (!id || !emails) {
    mostrarToast("Debes ingresar al menos un correo para compartir");
    return;
  }

  try {
    const res = await fetch(`/api/tareas/compartir/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ emails })
    });
    const data = await res.json();

    if (!res.ok || !data.success) {
      mostrarToast(data.message || "No se pudo compartir la tarea");
      return;
    }

    document.getElementById("edit-task-share-email").value = "";
    mostrarToast("Tarea compartida correctamente");
  } catch (error) {
    console.error("Error compartiendo tarea:", error);
    mostrarToast("Error al compartir la tarea");
  }
}

async function compartirRutinaDesdeModal() {
  const id = document.getElementById("edit-routine-id").value;
  const emails = document.getElementById("edit-routine-share-email").value.trim();
  if (!id || !emails) {
    mostrarToast("Debes ingresar al menos un correo para compartir");
    return;
  }

  try {
    const res = await fetch(`/api/rutinas/compartir/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ emails })
    });
    const data = await res.json();

    if (!res.ok || !data.success) {
      mostrarToast(data.message || "No se pudo compartir la rutina");
      return;
    }

    document.getElementById("edit-routine-share-email").value = "";
    mostrarToast("Rutina compartida correctamente");
  } catch (error) {
    console.error("Error compartiendo rutina:", error);
    mostrarToast("Error al compartir la rutina");
  }
}

function normalizarPrioridad(valor) {
  const prioridad = (valor || "").toLowerCase();
  if (prioridad === "rojo") return "alta";
  if (prioridad === "amarillo") return "media";
  if (prioridad === "verde") return "baja";
  return ["alta", "media", "baja"].includes(prioridad) ? prioridad : "media";
}

function normalizarFrecuencia(valor) {
  const frecuencia = (valor || "").toLowerCase();
  if (["diaria", "diario"].includes(frecuencia)) return "diario";
  if (["semanal", "mensual"].includes(frecuencia)) return frecuencia;
  return "diario";
}