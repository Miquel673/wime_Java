let fechaActual = new Date();
let tareasCalendario = [];

async function inicializarCalendario() {

  const btnPrev = document.getElementById("prev");
  const btnNext = document.getElementById("next");

  await cargarTareasCalendario();
  renderizarCalendario();

  btnPrev.addEventListener("click", async () => {
    fechaActual.setMonth(fechaActual.getMonth() - 1);
    renderizarCalendario();
  });

  btnNext.addEventListener("click", async () => {
    fechaActual.setMonth(fechaActual.getMonth() + 1);
    renderizarCalendario();
  });
}

function renderizarCalendario() {

  const diasContainer = document.getElementById("dias");
  const mesAnio = document.getElementById("mes-anio");

  diasContainer.innerHTML = "";

  const year = fechaActual.getFullYear();
  const month = fechaActual.getMonth();
  const hoy = new Date();

  const primerDia = new Date(year, month, 1).getDay();
  const ultimoDia = new Date(year, month + 1, 0).getDate();

  mesAnio.textContent = fechaActual.toLocaleDateString("es-ES", {
    month: "long",
    year: "numeric"
  });

  // Espacios vacíos
  for (let i = 0; i < primerDia; i++) {
    const vacio = document.createElement("div");
    vacio.classList.add("dia", "vacio");
    diasContainer.appendChild(vacio);
  }

  // Días del mes
  for (let d = 1; d <= ultimoDia; d++) {

    const dia = document.createElement("div");
    dia.classList.add("dia");
    dia.textContent = d;

    const fechaFormateada =
      `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;

    // Marcar hoy
    if (
      d === hoy.getDate() &&
      month === hoy.getMonth() &&
      year === hoy.getFullYear()
    ) {
      dia.classList.add("hoy");
    }

    const tareasDelDia =
      tareasCalendario.filter(t => t.fechaLimite === fechaFormateada);

    if (tareasDelDia.length > 0) {

      const hayCompletada =
        tareasDelDia.some(t => t.estado === "completada");

      if (hayCompletada) {
        dia.style.backgroundColor = "#28a745";
        dia.style.color = "#fff";
      } else {
        dia.style.backgroundColor = "#ffc107";
        dia.style.color = "#000";
      }

      dia.title =
        tareasDelDia.map(t => `• ${t.titulo}`).join("\n");
    }

    diasContainer.appendChild(dia);
  }
}

async function cargarTareasCalendario() {
  try {
    const response = await fetch("/api/tareas/listar");
    const data = await response.json();

    if (data.success) {
      tareasCalendario = data.tareas.filter(t => t.fechaLimite !== null);
    }

  } catch (error) {
    console.error("Error cargando tareas:", error);
  }
}




/*document.addEventListener('DOMContentLoaded', function () {
  if (typeof FullCalendar === 'undefined') {
     alert("❌ FullCalendar no está cargado"); 
    console.error("Error: FullCalendar no está definido. ¿Se cargó bien el archivo main.min.js?");
    return;
  }*/

  const calendarioEl = document.getElementById('miCalendario');

  /*if (!calendarioEl) {
    alert("❌ No se encontró el contenedor con ID 'miCalendario'");
    return;
  }

  const calendar = new FullCalendar.Calendar(calendarioEl, {
    initialView: 'dayGridMonth',
    locale: 'es',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,listWeek'
    },
    events: [
      {
        title: '📝 Tarea de prueba',
        start: '2025-07-24',
        color: '#2E86C1'
      },
      {
        title: '🔁 Rutina de ejemplo',
        start: '2025-07-26',
        color: '#28B463'
      }
    ],
    eventClick: function (info) {
      alert(`📌 ${info.event.title}\n📅 ${info.event.start.toLocaleDateString()}`);
    }
  });

  calendar.render();
  console.log(typeof FullCalendar);

});*/


// Exportar globalmente si la sidebar se carga por fetch
window.inicializarCalendario = inicializarCalendario;
window.renderizarCalendario = renderizarCalendario;
window.cargarTareasCalendario = cargarTareasCalendario;