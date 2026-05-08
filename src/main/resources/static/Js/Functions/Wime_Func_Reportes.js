document.addEventListener("DOMContentLoaded", () => {

  const idUsuario = sessionStorage.getItem("idUsuario");

  if (!idUsuario) {
    console.warn("⚠️ Usuario no autenticado");
    return;
  }

  inicializarExportacionReportes();

});

// =============================
// EXPORTACIÓN DE REPORTES PDF
// =============================
function inicializarExportacionReportes(){

    const formReporte = document.getElementById("form-reporte");
    const tipoReporte = document.getElementById("tipo-reporte");
    const btnExportPdf = document.getElementById("menu-export-pdf");
    const btnExportCsv = document.getElementById("menu-export-csv");
    const btnImportCsv = document.getElementById("btn-import");
    const fileImport = document.getElementById("file-import");

    if(!formReporte || !tipoReporte) return;

    if(btnExportPdf){
        btnExportPdf.addEventListener("click", async () => {
            const tipo = tipoReporte.value;

            if(!tipo){
                mostrarMensajeReporte("Selecciona el tipo de reporte");
                return;
            }

            try{

                const resp = await fetch(`/reportes/${tipo}`, {
                    method: "GET",
                    credentials: "include"
                });

                if(!resp.ok){
                    throw new Error(`Error al generar reporte de ${tipo}`);
                }

                const blob = await resp.blob();
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement("a");

                a.href = url;
                a.download = `reporte_${tipo}.pdf`;

                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);

                mostrarMensajeReporte("Reporte exportado correctamente");

            }catch(err){
                console.error(" Error generando reporte", err);
                mostrarMensajeReporte("No se pudo generar el reporte");
            }

        });
    }

    if(btnExportCsv){
        btnExportCsv.addEventListener("click", () => {
            const tipo = tipoReporte.value;

            if(!tipo){
                mostrarMensajeReporte("Selecciona el tipo de reporte");
                return;
            }

            window.location.href = `/import-export/export/${tipo}`;
        });
    }

    if(btnImportCsv && fileImport){
        btnImportCsv.addEventListener("click", async () => {
            const tipo = tipoReporte.value;
            const file = fileImport.files[0];

            if(!tipo){
                mostrarMensajeReporte("Selecciona el tipo de reporte");
                return;
            }

            if(!file){
                mostrarMensajeReporte("Selecciona un archivo CSV para importar");
                return;
            }

            const formData = new FormData();
            formData.append("file", file);

            try{
                const resp = await fetch(`/import-export/import/${tipo}`, {
                    method: "POST",
                    body: formData,
                    credentials: "include"
                });

                const msg = await resp.text();

                if(!resp.ok){
                    throw new Error(msg || "No se pudo importar el archivo");
                }

                mostrarMensajeReporte(msg || "Datos importados correctamente");
                fileImport.value = "";

            }catch(err){
                console.error(" Error importando CSV", err);
                mostrarMensajeReporte("Ocurrió un error al importar los datos");
            }
        });
    }

}



function mostrarMensajeReporte(mensaje){
  if (typeof mostrarToast === "function") {
    mostrarToast(mensaje);
    return;
  }
  alert(mensaje);
}