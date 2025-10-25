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
  fetch("http://localhost:8080/api/tareas/listar")
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

    if (!Array.isArray(tareas) || tareas.length === 0) {
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
          <button class="btn btn-danger btn-sm btn-eliminar" data-id="${tarea.idTarea}">Eliminar</button>
          <button class="btn btn-sm btn-outline-secondary bg-primary text-light btn-editar" data-id="${tarea.idTarea}">Editar</button>
        </div>
      </div>
    </div>
  </div>
`;

    contenedor.appendChild(tarjeta);
  });
}

//Rutinas//

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

// Delegación de eventos para botones
document.addEventListener("click", (e) => {
  // ✅ Eliminar tarea
  if (e.target.matches(".btn-eliminar")) {
    const id = e.target.dataset.id;
    if (!confirm("¿Estás seguro de eliminar esta tarea?")) return;

    fetch(`/api/tareas/eliminar/${id}`, { method: "DELETE" })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          alert(data.message); // 🔄 reemplazo temporal
          setTimeout(() => cargarTareas(), 800);
        } else {
          alert(data.message);
        }
      })
      .catch(err => alert("❌ Error: " + err));
  }

  // ✅ Editar tarea (redirección con ID en la URL)
if (e.target.matches(".btn-editar")) {
  const id = e.target.dataset.id;
  window.location.href = `/HTML/Wime_interfaz_Modulo_ETareas.html?id=${id}`;
}
});


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


//eliminacion de rutina

async function eliminarRutina(id) {
  if (!confirm("¿Eliminar esta rutina?")) return;

  const res = await fetch(`/api/rutinas/eliminar/${id}`, { method: "DELETE" });
  const data = await res.json();

  alert(data.message);
  if (data.success) {
    cargarRutinas();
  }
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


