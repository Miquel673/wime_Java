const addItemAnterior = window.addItem;

function obtenerFechaActualISO() {
  return new Date().toISOString().split("T")[0];
}


function configurarRestriccionFechasRutina(inicioId, finId) {
  const inicio = document.getElementById(inicioId);
  const fin = document.getElementById(finId);
  if (!inicio || !fin) return;

const actualizarMinimo = () => {
    const fechaActual = obtenerFechaActualISO();
    inicio.min = fechaActual;
    fin.min = inicio.value || fechaActual;

    if (inicio.value && inicio.value < fechaActual) {
      inicio.value = fechaActual;
    }

    if (fin.value && fin.value < fin.min) {
      fin.value = fin.min;
    }
  };

  inicio.addEventListener("change", actualizarMinimo);
  actualizarMinimo();
}

document.addEventListener("DOMContentLoaded", () => {
  configurarRestriccionFechasRutina("routine-start-date", "routine-end-date");
});

window.addItem = async function(tipo) {
  if (tipo !== "rutina") {
    if (typeof addItemAnterior === "function") return addItemAnterior(tipo);
    return;
  }

  const nombreRutina = document.getElementById("routine-name")?.value.trim();
  const descripcion = document.getElementById("routine-desc")?.value.trim();
  const fechaAsignacion = document.getElementById("routine-start-date")?.value;
  const fechaFin = document.getElementById("routine-end-date")?.value;
  const prioridad = document.getElementById("routine-priority")?.value || "media";
  const frecuencia = document.getElementById("routine-frequency")?.value || "diario";
  const emails = document.getElementById("routine-share-email")?.value.trim();

  if (!nombreRutina || !fechaAsignacion || !fechaFin) {
    mostrarToast("Debes completar nombre, fecha de asignación y fecha final", "error");
    return;
  }

    if (fechaAsignacion < obtenerFechaActualISO()) {
    mostrarToast("La fecha de inicio no puede ser anterior a la fecha actual", "error");
    return;
  }

  if (fechaFin < obtenerFechaActualISO()) {
    mostrarToast("La fecha final no puede ser anterior a la fecha actual", "error");
    return;
  }

  if (fechaFin < fechaAsignacion) {
    mostrarToast("La fecha final no puede ser anterior a la fecha de inicio", "error");
    return;
  }

  const payload = { nombreRutina, descripcion: descripcion || null, fechaAsignacion, fechaFin, prioridad, frecuencia, emails };

  try {
    const response = await fetch("/api/rutinas/crear", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(payload)
    });

    const data = await response.json();
    if (!response.ok || !data.success) {
      mostrarToast(data.message || "No se pudo crear la rutina", "error");
      return;
    }

    mostrarToast(data.message || "Rutina creada correctamente", "success");
    limpiarModalRutina();
    bootstrap.Modal.getInstance(document.getElementById("modalRutina"))?.hide();
    if (typeof cargarItems === "function") {
      await cargarItems();
      await cargarEstadisticasTablero();
    }
  } catch (error) {
    console.error("Error creando rutina:", error);
    mostrarToast("Error al conectar con el servidor", "error");
  }
};

function limpiarModalRutina() {
  ["routine-name", "routine-desc", "routine-start-date", "routine-end-date", "routine-share-email"].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.value = "";
  });
  if (document.getElementById("routine-priority")) document.getElementById("routine-priority").value = "media";
  if (document.getElementById("routine-frequency")) document.getElementById("routine-frequency").value = "diario";
  configurarRestriccionFechasRutina("routine-start-date", "routine-end-date");
}