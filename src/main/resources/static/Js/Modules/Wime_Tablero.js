const usuario = JSON.parse(localStorage.getItem("usuario"));

let db = [];

let navDate = new Date();
let filtroActual = "todas";

document.addEventListener("DOMContentLoaded", () => {

    fetch("/api/auth/check-session", {
        method: "GET",
        credentials: "include"
    })
    .then(res => res.json())
    .then(data => {

        if (data.active) {

            console.log("✅ Sesión activa:", data.usuario);

            const bienvenida = document.getElementById("bienvenida");

            if (bienvenida) {
                bienvenida.innerText = `Bienvenido, ${data.usuario}`;
            }

        } else {

            console.warn("⚠️ No hay sesión activa, redirigiendo...");
            window.location.href = "../../Login.html";

        }

    })
    .catch(err => {

        console.error("❌ Error verificando sesión:", err);
        window.location.href = "../../Login.html";

    });

});

async function cargarItems(){

    try{

        const resTareas = await fetch("/api/tareas/listar");
        const resRutinas = await fetch("/api/rutinas/listar");

        const tareasJSON = await resTareas.json();
        const rutinasJSON = await resRutinas.json();

        console.log("Tareas backend:", tareasJSON);
        console.log("Rutinas backend:", rutinasJSON);

        const tareas = tareasJSON.tareas || [];
        const rutinas = rutinasJSON.rutinas || [];

        // Dentro de cargarItems()
    db = [
    ...tareas.map(t => ({
        id: t.idTarea ?? t.id,
        tipo: "tarea",
        titulo: t.titulo,
        descripcion: t.descripcion,
        prioridad: t.prioridad,
        estado: t.estado,
        fechaLimite: t.fechaLimite,
        esCompartida: t.esCompartida,
        nombreCreador: t.nombreCreador,
        imagenPerfilCreador: t.imagenPerfilCreador
    })),

    ...rutinas.map(r => ({
        id: r.idRutina ?? r.id,
        tipo: "rutina",
        titulo: r.titulo,
        descripcion: r.descripcion,
        prioridad: "verde",
        estado: r.estado,
        hora: r.hora
    }))
    ];
        console.log("DB actual:", db);

        renderItems();
        updateStats();

    }catch(error){
        console.error("Error cargando items:", error);
    }

}

function getColorPorPrioridad(prioridad){

  const map = {
    alta: "danger",
    media: "warning",
    baja: "success",
    rojo: "danger",
    amarillo: "warning",
    verde: "success"
  };

  return map[prioridad?.toLowerCase()] || "secondary";
}

function getColorPorEstado(estado){

  const map = {
    pendiente: "secondary",
    en_progreso: "primary",
    completada: "success",
    vencida: "danger"
  };

  return map[estado?.toLowerCase()] || "secondary";
}

function renderItems(){

  const contenedor = document.getElementById("contenedor-items");
  contenedor.innerHTML = "";

  db.forEach(item => {

    const tarjeta = document.createElement("div");
    tarjeta.className = "col mb-3";

    if(item.tipo === "tarea"){

      tarjeta.innerHTML = `

<div class="card shadow-sm h-100 wime-card">

  <div class="wime-card-header">
    ${item.titulo}
  </div>

  <div class="wime-card-body">

    ${item.esCompartida ? `
      <div class="d-flex align-items-center mb-2">
        <img src="${item.imagenPerfilCreador || 'https://ui-avatars.com/api/?name=' + item.nombreCreador}"
          class="rounded-circle me-2"
          width="30"
          height="30">

        <small class="text-muted">
          Compartida por ${item.nombreCreador || "Usuario"}
        </small>
      </div>
    ` : ''}

    <p class="card-text">
      <strong>Prioridad:</strong> ${item.prioridad || "N/A"}
    </p>

    <p class="card-text">
      <strong>Fecha límite:</strong> ${item.fechaLimite || "N/A"}
    </p>

    <p class="card-text">
      ${item.descripcion || "Sin descripción"}
    </p>

    <span class="badge bg-${getColorPorEstado(item.estado)}">
      ${item.estado || "Pendiente"}
    </span>

    <button class="btn btn-sm btn-outline-dark w-100 mt-2"
      data-bs-toggle="collapse"
      data-bs-target="#opciones-${item.id}">
      ▼ Ver opciones
    </button>

  </div>

</div>
`;

    }

    contenedor.appendChild(tarjeta);

  });

}

function toggleItem(id) {
    const item = db.find(i => i.id === id);
    if (item) {
        item.completa = !item.completa;
        renderItems();
    }
}

function addItem(tipo) {

    const id = Date.now();
    let nuevo;

    if (tipo === "tarea") {

        const nombre = document.getElementById("task-name").value;
        const desc = document.getElementById("task-desc").value;
        const prio = document.getElementById("task-priority").value;

        if (!nombre) return alert("Nombre requerido");

        nuevo = {
            id,
            tipo,
            nombre,
            desc,
            prioridad: prio,
            meta: "Hoy",
            completa: false
        };

        bootstrap.Modal.getInstance(document.getElementById("modalTarea")).hide();

    } else {

        const nombre = document.getElementById("routine-name").value;
        const desc = document.getElementById("routine-desc").value;
        const hora = document.getElementById("routine-time").value;

        if (!nombre || !hora) return alert("Completa los campos");

        nuevo = {
            id,
            tipo,
            nombre,
            desc,
            prioridad: "verde",
            meta: hora,
            completa: false
        };

        bootstrap.Modal.getInstance(document.getElementById("modalRutina")).hide();
    }

    db.push(nuevo);
    renderItems();

    document.querySelectorAll("input, textarea").forEach(i => i.value = "");
}

function compartirPorCorreo(tipo) {

    let titulo, desc, detalle, email;

    if (tipo === "tarea") {

        titulo = document.getElementById("task-name").value;
        desc = document.getElementById("task-desc").value;
        detalle = "Prioridad: " + document.getElementById("task-priority").selectedOptions[0].text;
        email = document.getElementById("task-share-email").value;

    } else {

        titulo = document.getElementById("routine-name").value;
        desc = document.getElementById("routine-desc").value;
        detalle = "Hora: " + document.getElementById("routine-time").value;
        email = document.getElementById("routine-share-email").value;
    }

    if (!titulo || !email || !email.includes("@"))
        return alert("Escribe el nombre y un correo válido");

    const asunto = encodeURIComponent(`WIME: Te han compartido una ${tipo}`);

    const cuerpo = encodeURIComponent(
`Hola!

Se ha compartido contigo la siguiente ${tipo} de WIME:

📌 Título: ${titulo}
📝 Nota: ${desc || "N/A"}
⏰ Detalle: ${detalle}

¡Sigue enfocado!`
    );

    window.location.href = `mailto:${email}?subject=${asunto}&body=${cuerpo}`;
}

function deleteItem(id) {
    if (confirm("¿Eliminar?")) {
        db = db.filter(x => x.id !== id);
        renderItems();
    }
}

function updateStats() {

    const total = db.length;
    const hechas = db.filter(i => i.completa).length;

    const porc = total ? Math.round((hechas / total) * 100) : 0;

    document.getElementById("stat-completadas").innerText = porc + "%";
    document.getElementById("bar-completadas").style.width = porc + "%";

    document.getElementById("stat-pendientes").innerText = (100 - porc) + "%";
    document.getElementById("bar-pendientes").style.width = (100 - porc) + "%";
}

function filtrar(tipo, btn) {

    filtroActual = tipo;

    document.querySelectorAll("#filter-group button")
        .forEach(b => b.classList.remove("active"));

    btn.classList.add("active");

    renderItems();
}

function initCalendar() {

    const grid = document.getElementById("calGrid");
    grid.innerHTML = "";

    document.getElementById("calMonthYear").innerText =
        new Intl.DateTimeFormat("es-ES", {
            month: "long",
            year: "numeric"
        }).format(navDate);

    let first = new Date(navDate.getFullYear(), navDate.getMonth(), 1).getDay();
    let total = new Date(navDate.getFullYear(), navDate.getMonth() + 1, 0).getDate();

    for (let s = 0; s < first; s++) grid.appendChild(document.createElement("div"));

    for (let d = 1; d <= total; d++) {

        const day = document.createElement("div");
        day.innerText = d;

        if (
            d === new Date().getDate() &&
            navDate.getMonth() === new Date().getMonth()
        ) {
            day.classList.add("cal-today");
        }

        grid.appendChild(day);
    }
}

window.moveMonth = (offset) => {
    navDate.setMonth(navDate.getMonth() + offset);
    initCalendar();
};

document.getElementById("themeToggle").addEventListener("click", () => {

    const isDark = document.body.classList.toggle("dark");

    localStorage.setItem("theme", isDark ? "dark" : "light");

    document.getElementById("themeIcon").className =
        isDark ? "bi bi-moon-stars-fill" : "bi bi-sun";
});

document.addEventListener("DOMContentLoaded", () => {

    initCalendar();
    cargarItems();

    document.getElementById("fecha-actual").textContent =
        new Date().toLocaleDateString('es-ES', {
            weekday: 'long',
            day: 'numeric',
            month: 'long'
        });


    if (localStorage.getItem("theme") === "dark") {
        document.body.classList.add("dark");
        document.getElementById("themeIcon").className = "bi bi-moon-stars-fill";
    }
});

function cerrarSesion() {
    if (confirm("¿Cerrar sesión?")) {
        window.location.href = "../../index.html";
    }
}