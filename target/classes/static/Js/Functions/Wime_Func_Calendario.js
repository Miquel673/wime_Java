let fechaActual = new Date();
let tareasCalendario = [];
let fechaNavegacionCalendario = new Date();

async function initCalendar() {

    const grid = document.getElementById("calGrid");
    const monthYear = document.getElementById("calMonthYear");

    if (!grid || !monthYear) return;

    await cargarTareasCalendario();

    grid.innerHTML = "";

    monthYear.innerText =
        new Intl.DateTimeFormat("es-ES", {
            month: "long",
            year: "numeric"
        }).format(fechaNavegacionCalendario);

    let first = new Date(
        fechaNavegacionCalendario.getFullYear(),
        fechaNavegacionCalendario.getMonth(),
        1
    ).getDay();

    let total = new Date(
        fechaNavegacionCalendario.getFullYear(),
        fechaNavegacionCalendario.getMonth() + 1,
        0
    ).getDate();

    const hoy = new Date();

    for (let s = 0; s < first; s++) grid.appendChild(document.createElement("div"));

    for (let d = 1; d <= total; d++) {

        const day = document.createElement("div");
        day.innerText = d;

        const year = fechaNavegacionCalendario.getFullYear();
        const month = fechaNavegacionCalendario.getMonth();

        const fechaFormateada =
            `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;

        if (
            d === hoy.getDate() &&
            month === hoy.getMonth() &&
            year === hoy.getFullYear()
        ) {
            day.classList.add("cal-today");
        }

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
    fechaNavegacionCalendario.setMonth(fechaNavegacionCalendario.getMonth() + offset);
    initCalendar();
};

async function cargarTareasCalendario() {

    try {

        const response = await fetch("/api/tareas/listar", {
            credentials: "include"
        });

        if (!response.ok) {
            tareasCalendario = [];
            return;
        }

        const data = await response.json();

        if (data.success) {
            tareasCalendario = data.tareas.filter(t => t.fechaLimite !== null);
        } else {
            tareasCalendario = [];
        }

    } catch (error) {
        tareasCalendario = [];
        console.error("Error cargando tareas calendario:", error);
    }

}

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("calGrid") && document.getElementById("calMonthYear")) {
        initCalendar();
    }
});
