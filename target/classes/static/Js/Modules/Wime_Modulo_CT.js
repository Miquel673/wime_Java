// =============================
// CREAR TAREA DESDE MODAL
// =============================

async function addItem(tipo) {

  if (tipo !== "tarea") return;

  const titulo = document.getElementById("task-name")?.value.trim();
  const descripcion = document.getElementById("task-desc")?.value.trim();
  const prioridad = document.getElementById("task-priority")?.value;
  const compartirCon = document.getElementById("task-share-email")?.value.trim();
  const fechaLimite = document.getElementById("task-date")?.value;

  const mapPrioridad = {
    rojo: "alta",
    amarillo: "media",
    verde: "baja"
  };

  const prioridadBackend = mapPrioridad[prioridad];

  // -----------------------------
  // Validación básica
  // -----------------------------
  if (!titulo || !prioridad || !fechaLimite) {
    mostrarToast("Debes completar Nombre, Prioridad y Fecha", "error");
    return;
  }

  if (compartirCon && !compartirCon.includes("@")) {
    mostrarToast("Ingresa un correo válido", "error");
    return;
  }

  try {

    const payload = {
      titulo: titulo,
      descripcion: descripcion || null,
      prioridad: prioridadBackend,
      fechaLimite: fechaLimite
    };

    if (compartirCon) {
      payload.compartirCon = compartirCon;
    }

    console.log("📤 Enviando al backend:", payload);

    const response = await fetch("/api/tareas/crear", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      credentials: "include",
      body: JSON.stringify(payload)
    });

    // -----------------------------------
    // Leer respuesta primero como texto
    // -----------------------------------
    const text = await response.text();

    let data = null;

    try {
      data = text ? JSON.parse(text) : null;
    } catch (err) {
      console.warn(" La respuesta no es JSON:", text);
    }

    console.log("📥 Respuesta backend:", data);

    // -----------------------------------
    // Si el backend respondió OK
    // -----------------------------------
    if (response.ok) {

      mostrarToast(
        data?.message || " Tarea creada correctamente",
        "success"
      );

      limpiarModalTarea();

      const modalElement = document.getElementById("modalTarea");
      const modal = bootstrap.Modal.getInstance(modalElement);

      if (modal) {
        modal.hide();
      }

      if (typeof cargarItems === "function") {
        await cargarItems();
      }

      return;
    }

    // -----------------------------------
    // Si el backend respondió error
    // -----------------------------------
    mostrarToast(
      data?.message || "No se pudo crear la tarea",
      "error"
    );

  } catch (error) {

    console.error("❌ Error real de conexión:", error);

    mostrarToast(
      "❌ Error al conectar con el servidor",
      "error"
    );

  }
}


// =============================
// LIMPIAR MODAL
// =============================

function limpiarModalTarea() {

  const name = document.getElementById("task-name");
  const desc = document.getElementById("task-desc");
  const priority = document.getElementById("task-priority");
  const email = document.getElementById("task-share-email");
  const date = document.getElementById("task-date");

  if (name) name.value = "";
  if (desc) desc.value = "";
  if (priority) priority.value = "verde";
  if (email) email.value = "";
  if (date) date.value = "";
}


// =============================
// TOAST SIMPLE
// =============================

function mostrarToast(mensaje, tipo = "info") {

  console.log(`[${tipo.toUpperCase()}]`, mensaje);

  // temporal
  alert(mensaje);
}