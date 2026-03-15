const usuario = JSON.parse(localStorage.getItem("usuario"));

let db = [];

let navDate = new Date();
let filtroActual = "todas";

document.addEventListener("DOMContentLoaded", () => {

    cargarEstadisticasTablero();

    fetch("/api/auth/check-session", {
        method: "GET",
        credentials: "include"
    })
    .then(res => res.json())
    .then(data => {

        if (data.active) {

            console.log("Sesión activa:", data.usuario);

            const bienvenida = document.getElementById("bienvenida");

            if (bienvenida) {
                bienvenida.innerText = `Bienvenido, ${data.usuario}`;
            }

            // Obtener id del usuario guardado
            const idUsuario = sessionStorage.getItem("idUsuario");

            if (idUsuario) {
                cargarFotoPerfil(idUsuario);
            }

        } else {

            console.warn("No hay sesión activa, redirigiendo...");
            window.location.href = "../../index.html";

        }

    })
    .catch(err => {

        console.error("❌ Error verificando sesión:", err);
        window.location.href = "../../index.html";

    });

});

async function cargarFotoPerfil(idUsuario) {

    try {

        const res = await fetch(`/api/usuarios/${idUsuario}/foto`, {
            credentials: "include"
        });

        const data = await res.json();

        if (data.fotoPerfil) {

            const foto = document.getElementById("fotoPerfil");

            if (foto) {
                foto.src = data.fotoPerfil + "?t=" + Date.now();
            }

        }

    } catch (e) {

        console.error("❌ Error cargando foto de perfil", e);

    }

}

async function cargarItems(){

    try{

        const resTareas = await fetch("/api/tareas/listar", {
            credentials: "include"
        });

        const resRutinas = await fetch("/api/rutinas/listar", {
            credentials: "include"
        });

        const tareasJSON = await resTareas.json();
        const rutinasJSON = await resRutinas.json();

        console.log("Tareas backend:", tareasJSON);
        console.log("Rutinas backend:", rutinasJSON);

        const tareas = tareasJSON.tareas || [];
        const rutinas = rutinasJSON.rutinas || [];

        db = [

            ...tareas.map(t => ({
                id: t.idTarea ?? t.id,
                tipo: "tarea",
                titulo: t.titulo,
                descripcion: t.descripcion,
                prioridad: t.prioridad,
                estado: t.estado,
                fechaLimite: t.fechaLimite || null,
                esCompartida: t.esCompartida || false,
                nombreCreador: t.nombreCreador || null,
                imagenPerfilCreador: t.imagenPerfilCreador || null
            })),

            ...rutinas.map(r => ({
                id: r.idRutina ?? r.id,
                tipo: "rutina",
                titulo: r.nombreRutina ?? r.titulo,
                descripcion: r.descripcion,
                prioridad: "verde",
                estado: r.estado,
                hora: r.hora,
                fechaLimite: null
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

<div class="card shadow-sm h-100 wime-card" id="tarea-${item.id}">



  <div class="wime-card-header">
    ${item.titulo}
  </div>

  

  <div class="wime-card-body">

${item.esCompartida ? `
<div class="d-flex align-items-center mb-2">

  <img src="${item.imagenPerfilCreador || 
  'https://ui-avatars.com/api/?name=' + item.nombreCreador}"
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

    <div class="dropdown mt-2">

    <button 
        class="badge border-0 bg-${getColorPorEstado(item.estado)} dropdown-toggle"
        style="font-size:0.9rem; cursor:pointer;"
        data-bs-toggle="dropdown">

        ${item.estado || "pendiente"}

    </button>
    
${item.esCompartida 
  ? `<button class="btn btn-sm btn-outline-warning btn-remover" data-id="${item.id}" title="Quitar de mi lista">
       <i class="bi bi-person-dash"></i>
     </button>` 
  : `<button class="btn-icon-eliminar btn-eliminar" data-id="${item.id}" title="Eliminar tarea">
       <i class="bi bi-trash"></i>
     </button>`
     
}

    <ul class="dropdown-menu">

        <li>
        <a class="dropdown-item"
        onclick="cambiarEstadoTarea(${item.id}, 'pendiente')">
        Pendiente
        </a>
        </li>

        <li>
        <a class="dropdown-item"
        onclick="cambiarEstadoTarea(${item.id}, 'en_progreso')">
        En progreso
        </a>
        </li>

        <li>
        <a class="dropdown-item"
        onclick="cambiarEstadoTarea(${item.id}, 'completada')">
        Completada
        </a>
        </li>

    </ul>

    </div>


  </div>

</div>
`;

    }

    contenedor.appendChild(tarjeta);

    if(item.estado?.toLowerCase() === "vencida"){
    tarjeta.querySelector(".card").classList.add("vencida");
}
  if(item.esCompartida){
    tarjeta.querySelector(".card").classList.add("wime-card-compartida");
}
  });



}

async function cambiarEstadoTarea(id, nuevoEstado){

    try{

        const res = await fetch(`/api/tareas/${id}/estado`,{
            method:"PUT",
            credentials:"include",
            headers:{
                "Content-Type":"application/json"
            },
            body: JSON.stringify({
                estado:nuevoEstado
            })
        });

        const data = await res.json();

        if(!data.success){
            throw new Error("No se pudo actualizar");
        }

        console.log(" Estado actualizado:", data);

        // 🔄 Recargar tareas del tablero
        await cargarItems();

        // 📊 Actualizar estadísticas del panel
        if(typeof cargarEstadisticasTablero === "function"){
            await cargarEstadisticasTablero();
        }

        // Si aún usas el cálculo local en lugar del endpoint
        if(typeof updateStats === "function"){
            updateStats();
        }

    }catch(error){

        console.error("Error cambiando estado:",error);
        alert("No se pudo cambiar el estado");

    }

}


document.addEventListener("click", async (e) => {

    // 🗑️ eliminar tarea (creador)
    const btnEliminar = e.target.closest(".btn-eliminar");

    if(btnEliminar){

        const id = btnEliminar.dataset.id;

        if(!confirm("¿Eliminar esta tarea?")) return;

        try{

            const res = await fetch(`/api/tareas/eliminar/${id}`,{
                method:"DELETE",
                credentials:"include"
            });

            const data = await res.json();

            if(data.success){
                await cargarItems();
            }else{
                alert(data.message);
            }

        }catch(err){
            console.error(err);
        }

        return;
    }

    // ❌ quitar tarea compartida
    const btnRemover = e.target.closest(".btn-remover");

    if(btnRemover){

        const id = btnRemover.dataset.id;

        if(!confirm("¿Quitar esta tarea de tu lista?")) return;

        try{

            const res = await fetch(`/api/tareas/remover-compartida/${id}`,{
                method:"DELETE",
                credentials:"include"
            });

            const data = await res.json();

            if(data.success){
                await cargarItems();
            }else{
                alert(data.message);
            }

        }catch(err){
            console.error(err);
        }

        return;
    }

});

function updateStats() {

    if (!Array.isArray(db)) return;

    // solo contar tareas
    const tareas = db.filter(item => item.tipo === "tarea");

    const total = tareas.length;

    const completadas = tareas.filter(item =>
        item.estado === "completada"
    ).length;

    const pendientes = tareas.filter(item =>
        item.estado === "pendiente" || item.estado === "en_progreso"
    ).length;

    const hoy = new Date();
    hoy.setHours(0,0,0,0);

    const vencidas = tareas.filter(item => {

        if (!item.fechaLimite) return false;

        const fechaLimite = new Date(item.fechaLimite);
        fechaLimite.setHours(0,0,0,0);

        return fechaLimite < hoy && item.estado !== "completada";

    }).length;

}

async function cargarEstadisticasTablero() {

    try {

        const res = await fetch("/api/estadisticas-tablero", {
            credentials: "include"
        });

        const data = await res.json();

        if (!data.success) return;

        const total = data.total || 0;
        const completadas = data.completadas || 0;
        const pendientes = data.pendientes || 0;
        const vencidas = data.vencidas || 0;

        const porcCompletadas = total ? Math.round((completadas / total) * 100) : 0;
        const porcPendientes = total ? Math.round((pendientes / total) * 100) : 0;
        const porcVencidas = total ? Math.round((vencidas / total) * 100) : 0;

        document.getElementById("stat-completadas").innerText = porcCompletadas + "%";
        document.getElementById("bar-completadas").style.width = porcCompletadas + "%";

        document.getElementById("stat-pendientes").innerText = porcPendientes + "%";
        document.getElementById("bar-pendientes").style.width = porcPendientes + "%";

        document.getElementById("stat-vencidas").innerText = porcVencidas + "%";
        document.getElementById("bar-vencidas").style.width = porcVencidas + "%";

    } catch (error) {

        console.error("Error cargando estadísticas:", error);

    }

}



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