// 📌 Visualización de tareas y rutinas en el tablero
document.addEventListener("DOMContentLoaded", () => {
  showContent("tareas"); // Mostrar sección de tareas al inicio
  cargarTareas();
  cargarRutinas();
});

// ------------------------
// FUNCIONES DE CARGA
// ------------------------

function cargarTareas() {
  fetch("/api/tareas/listar")
    .then(response => response.json())
    .then(data => {
      console.log("📌 Respuesta del backend (tareas):", data); // 👈
      if (data.success) {
        mostrarTareas(data.tareas);
      } else {
        console.error("⚠️ Error cargando tareas:", data.message);
      }
    })
    .catch(error => console.error("❌ Error en fetch:", error));
}

function cargarRutinas() {
  fetch("http://localhost:8080/api/rutinas/listar")
    .then(response => response.json())
    .then(data => {
      console.log("📌 Respuesta del backend (rutinas):", data); // 👈
      if (data.success) {
        mostrarRutinas(data.rutinas);
      } else {
        console.error("⚠️ Error cargando rutinas:", data.message);
      }
    })
    .catch(error => console.error("❌ Error en fetch:", error));
}
 

// ------------------------
// FUNCIONES DE VISUALIZACIÓN
// ------------------------

function mostrarTareas(tareas) {
  const contenedor = document.getElementById("contenedor-tareas");
  if (!contenedor) return;

  contenedor.innerHTML = "";

  if (tareas.length === 0) {
    contenedor.innerHTML = `<p class="text-center w-100">No hay tareas disponibles.</p>`;
    return;
  }

  tareas.forEach(tarea => {
    const tarjeta = document.createElement("div");
    tarjeta.className = "col";

    tarjeta.innerHTML = `
      <div class="card shadow-sm h-100">
        <div class="card-body">
          <h5 class="card-title titulo-tarea text-white bg-${getColorPorPrioridad(tarea.prioridad)}">${tarea.titulo}</h5>
          <p class="card-text"><strong>Prioridad:</strong> ${tarea.prioridad}</p>
          <p class="card-text"><strong>Fecha límite:</strong> ${tarea.fechaLimite || "N/A"}</p>
          <p class="card-text">${tarea.descripcion || "Sin descripción."}</p>
          <span class="badge bg-${getColorPorEstado(tarea.estado)}">${tarea.estado || "Pendiente"}</span>

          <button class="btn btn-sm btn-outline-dark w-100 mt-2" data-bs-toggle="collapse" data-bs-target="#opciones-${tarea.idTarea}">
            ▼ Ver opciones
          </button>

          <div class="collapse mt-2" id="opciones-${tarea.idTarea}">
            <label class="pb-2"><strong>Estado:</strong></label>
            <div class="dropdown mb-2">
              <button class="btn btn-sm dropdown-toggle text-white bg-${getColorPorEstado(tarea.estado)}" type="button" data-bs-toggle="dropdown">
                ${tarea.estado || "Pendiente"}
              </button>
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="#" onclick="cambiarEstadoTarea(${tarea.idTarea}, 'pendiente')">Pendiente</a></li>
                <li><a class="dropdown-item" href="#" onclick="cambiarEstadoTarea(${tarea.idTarea}, 'en progreso')">En progreso</a></li>
                <li><a class="dropdown-item" href="#" onclick="cambiarEstadoTarea(${tarea.idTarea}, 'completada')">Completada</a></li>
              </ul>
            </div>

            <div class="d-flex justify-content-between">
              <button class="btn btn-danger btn-sm" onclick="eliminarTarea(${tarea.idTarea})">Eliminar</button>
              <a href="/HTML/Wime_interfaz_Modulo_ETareas.html?id=${tarea.idTarea}" class="btn btn-sm btn-outline-secondary bg-primary text-light">Editar</a>
            </div>
          </div>
        </div>
      </div>
    `;
    contenedor.appendChild(tarjeta);
  });
}

function mostrarRutinas(rutinas) {
  const contenedor = document.getElementById("contenedor-rutinas");
  if (!contenedor) return;

  contenedor.innerHTML = "";

  if (!Array.isArray(rutinas) || rutinas.length === 0) {
    contenedor.innerHTML = `<p class="text-center w-100">No hay rutinas disponibles.</p>`;
    return;
  }

  rutinas.forEach(rutina => {
    const tarjeta = document.createElement("div");
    tarjeta.className = "col";

    tarjeta.innerHTML = `
      <div class="card shadow-sm h-100">
        <div class="card-body">
          <h5 class="card-title titulo-tarea bg-${getColorPorEstado(rutina.estado)} text-white">${rutina.nombreRutina}</h5>
          <p class="card-text"><strong>Prioridad:</strong> ${rutina.prioridad}</p>
          <p class="card-text"><strong>Frecuencia:</strong> ${rutina.frecuencia}</p>
          <p class="card-text"><strong>Fecha de Asignación:</strong> ${rutina.fechaAsignacion}</p>
          <p class="card-text"><strong>Fecha Fin:</strong> ${rutina.fechaFin}</p>
          <p class="card-text">${rutina.descripcion || "Sin descripción."}</p>

          <button class="btn btn-sm btn-outline-primary w-100 mt-2" data-bs-toggle="collapse" data-bs-target="#opciones-rutina-${rutina.idRutina}">▼ Ver opciones</button>
          
          <div class="collapse mt-2" id="opciones-rutina-${rutina.idRutina}">
            <label><strong>Estado:</strong></label>
            <div class="dropdown mb-2">
              <button class="btn btn-sm dropdown-toggle text-white bg-${getColorPorEstado(rutina.estado)}" type="button" data-bs-toggle="dropdown">
                ${rutina.estado || "Pendiente"}
              </button>
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="#" onclick="cambiarEstadoRutina(${rutina.idRutina}, 'pendiente')">Pendiente</a></li>
                <li><a class="dropdown-item" href="#" onclick="cambiarEstadoRutina(${rutina.idRutina}, 'en progreso')">En progreso</a></li>
                <li><a class="dropdown-item" href="#" onclick="cambiarEstadoRutina(${rutina.idRutina}, 'completada')">Completada</a></li>
              </ul>
            </div>

            <button class="btn btn-danger btn-sm" onclick="eliminarRutina(${rutina.idRutina})">Eliminar</button>

            <div class="d-flex justify-content-end mt-3">
              <a href="/HTML/Wime_interfaz_Modulo_ERutinas.html?id=${rutina.idRutina}" class="btn btn-sm btn-outline-secondary">Editar</a>
            </div>
          </div>
        </div>
      </div>
    `;
    contenedor.appendChild(tarjeta);
  });
}

// ------------------------
// FUNCIONES AUXILIARES
// ------------------------

function getColorPorPrioridad(prioridad) {
  if (!prioridad) return "secondary";
  switch (prioridad.toLowerCase()) {
    case "alta": return "danger";
    case "media": return "warning";
    case "baja": return "success";
    default: return "secondary";
  }
}

function getColorPorEstado(estado) {
  switch (estado?.toLowerCase()) {
    case "pendiente": return "secondary";
    case "en progreso": return "warning";
    case "completada": return "success";
    default: return "dark";
  }
}

function mostrarError(tipo, mensaje) {
  const contenedor = document.getElementById(`contenedor-${tipo}`);
  if (contenedor) {
    contenedor.innerHTML = `<p class="text-center text-danger w-100">${mensaje}</p>`;
  }
}

// ------------------------
// CRUD - TAREAS
// ------------------------

function eliminarTarea(id) {
  if (!confirm("¿Estás seguro de eliminar esta tarea?")) return;

  fetch(`/api/tareas/${id}`, { method: "DELETE" })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        alert("✅ Tarea eliminada");
        cargarTareas();
      } else {
        alert("❌ Error al eliminar: " + data.message);
      }
    })
    .catch(err => console.error("❌ Error:", err));
}

function cambiarEstadoTarea(id, nuevoEstado) {
  fetch(`/api/tareas/${id}/estado`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ estado: nuevoEstado })
  })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        cargarTareas();
      } else {
        alert(data.message || "❌ Error al cambiar el estado.");
      }
    });
}

// ------------------------
// CRUD - RUTINAS
// ------------------------

function eliminarRutina(id) {
  if (!confirm("¿Estás seguro de eliminar esta rutina?")) return;

  fetch(`/api/rutinas/${id}`, { method: "DELETE" })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        alert("✅ Rutina eliminada");
        cargarRutinas();
      } else {
        alert("❌ Error al eliminar la rutina.");
      }
    })
    .catch(err => console.error("❌ Error eliminando rutina:", err));
}

function cambiarEstadoRutina(id, nuevoEstado) {
  fetch(`/api/rutinas/${id}/estado`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ estado: nuevoEstado })
  })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        cargarRutinas();
      } else {
        alert("❌ Error al cambiar el estado de la rutina.");
      }
    })
    .catch(err => console.error("❌ Error:", err));
}

// ------------------------
// TABS VISUALIZACIÓN
// ------------------------

document.addEventListener("DOMContentLoaded", () => {
  const tabs = document.querySelectorAll(".tab");
  const contenedorTareas = document.getElementById("contenedor-tareas");
  const contenedorRutinas = document.getElementById("contenedor-rutinas");

  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      tabs.forEach(t => t.classList.remove("active"));
      tab.classList.add("active");

      const tipo = tab.dataset.tipo;
      if (tipo === "tareas") {
        contenedorTareas.style.display = "flex";
        contenedorRutinas.style.display = "none";
      } else {
        contenedorTareas.style.display = "none";
        contenedorRutinas.style.display = "flex";
      }
    });
  });
});

function showContent(tipo) {
  const tareas = document.getElementById("contenedor-tareas");
  const rutinas = document.getElementById("contenedor-rutinas");
  if (!tareas || !rutinas) return;

  if (tipo === "tareas") {
    tareas.style.display = "flex";
    rutinas.style.display = "none";
  } else {
    tareas.style.display = "none";
    rutinas.style.display = "flex";
  }

  document.querySelectorAll(".tab").forEach(tab => tab.classList.remove("active"));
  const activeTab = document.querySelector(`.tab[data-tipo="${tipo}"]`);
  if (activeTab) activeTab.classList.add("active");
}
