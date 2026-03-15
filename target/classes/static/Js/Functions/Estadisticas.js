async function cargarEstadisticasTablero(){

    try{

        const res = await fetch("/api/estadisticas-tablero",{
            method:"GET",
            credentials:"include"
        });

        const data = await res.json();

        if(!data.success){
            console.warn("No se pudieron cargar las estadísticas");
            return;
        }

        const total = data.total || 0;
        const completadas = data.completadas || 0;
        const pendientes = data.pendientes || 0;
        const vencidas = data.vencidas || 0;

        const porcCompletadas = total ? Math.round((completadas / total) * 100) : 0;
        const porcPendientes = total ? Math.round((pendientes / total) * 100) : 0;
        const porcVencidas = total ? Math.round((vencidas / total) * 100) : 0;

        actualizarBarra("completadas", porcCompletadas);
        actualizarBarra("pendientes", porcPendientes);
        actualizarBarra("vencidas", porcVencidas);

    }catch(error){

        console.error("Error cargando estadísticas:", error);

    }

}


function actualizarBarra(tipo, porcentaje){

    const stat = document.getElementById(`stat-${tipo}`);
    const bar = document.getElementById(`bar-${tipo}`);

    if(stat) stat.innerText = porcentaje + "%";
    if(bar) bar.style.width = porcentaje + "%";

}