let fechaActual = new Date();
let tareasCalendario = [];

async function initCalendar() {

    await cargarTareasCalendario();

    const grid = document.getElementById("calGrid");
    grid.innerHTML = "";

    document.getElementById("calMonthYear").innerText =
        new Intl.DateTimeFormat("es-ES", {
            month: "long",
            year: "numeric"
        }).format(navDate);

    let first = new Date(navDate.getFullYear(), navDate.getMonth(), 1).getDay();
    let total = new Date(navDate.getFullYear(), navDate.getMonth() + 1, 0).getDate();

    const hoy = new Date();

    for (let s = 0; s < first; s++) grid.appendChild(document.createElement("div"));

    for (let d = 1; d <= total; d++) {

        const day = document.createElement("div");
        day.innerText = d;

        const year = navDate.getFullYear();
        const month = navDate.getMonth();

        const fechaFormateada =
            `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;

        // marcar hoy
        if (
            d === hoy.getDate() &&
            month === hoy.getMonth() &&
            year === hoy.getFullYear()
        ) {
            day.classList.add("cal-today");
        }

        // buscar tareas de ese día
        const tareasDelDia =
            tareasCalendario.filter(t => t.fechaLimite === fechaFormateada);

        if (tareasDelDia.length > 0) {

            const hayCompletada =
                tareasDelDia.some(t => t.estado === "completada");

            if (hayCompletada) {
                day.style.backgroundColor = "#28a745";
                day.style.color = "#fff";
            } else {
                day.style.backgroundColor = "#ffc107";
                day.style.color = "#000";
            }

            day.title =
                tareasDelDia.map(t => `• ${t.titulo}`).join("\n");
        }

        grid.appendChild(day);
    }
}
window.moveMonth = (offset) => {
    navDate.setMonth(navDate.getMonth() + offset);
    initCalendar();
};


async function cargarTareasCalendario() {

    try {

        const response = await fetch("/api/tareas/listar");
        const data = await response.json();

        if (data.success) {
            tareasCalendario = data.tareas.filter(t => t.fechaLimite !== null);
        }

    } catch (error) {
        console.error("Error cargando tareas calendario:", error);
    }

}


function moveMonth(step) {

  calFechaActual.setMonth(calFechaActual.getMonth() + step);
  renderSidebarCalendar();

}

