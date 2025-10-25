// Reportes Filtro //

document.getElementById("form-reporte").addEventListener("submit", async (e) => {
  e.preventDefault();

  const tipo = document.getElementById("tipo-reporte").value;
  if (!tipo) {
    alert("Selecciona el tipo de reporte.");
    return;
  }

  try {
    // 👇 Usa la ruta según el tipo (tareas o rutinas)
    const resp = await fetch(`http://localhost:8080/reportes/${tipo}`, {
      method: "GET",
      credentials: "include" // importante para mantener sesión
    });

    if (!resp.ok) {
      throw new Error(`Error al generar reporte de ${tipo}`);
    }

    // Convertimos respuesta en Blob para descargar
    const blob = await resp.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `reporte_${tipo}.pdf`;
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);

  } catch (err) {
    console.error("❌ Error generando reporte:", err);
    alert("Error generando reporte.");
  }
});

// Vista previa del reporte

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("form-reporte");
  const btnPreview = document.getElementById("btn-preview");
  const tipoReporte = document.getElementById("tipo-reporte");

  const previewDiv = document.getElementById("preview");
  const previewHeader = document.getElementById("preview-header");
  const previewBody = document.getElementById("preview-body");


  /* ✅ Vista previa
  btnPreview.addEventListener("click", async () => {
    const tipo = tipoReporte.value;
    if (!tipo) {
      alert("Selecciona un tipo de reporte primero");
      return;
    }

    try {
      // Llamamos a la API según lo que seleccionó el usuario
      const response = await fetch(`/reportes/${tipo}`);
      if (!response.ok) throw new Error("Error al cargar datos");

      const data = await response.json();

      // Limpiar tabla
      previewHeader.innerHTML = "";
      previewBody.innerHTML = "";

      if (data.length === 0) {
        previewBody.innerHTML = `<tr><td colspan="5" class="text-center">No hay datos disponibles</td></tr>`;
      } else {
        // Cabeceras dinámicas
        const keys = Object.keys(data[0]);
        keys.forEach(key => {
          const th = document.createElement("th");
          th.textContent = key.charAt(0).toUpperCase() + key.slice(1);
          previewHeader.appendChild(th);
        });

        // Filas
        data.forEach(item => {
          const tr = document.createElement("tr");
          keys.forEach(key => {
            const td = document.createElement("td");
            td.textContent = item[key] ?? "-";
            tr.appendChild(td);
          });
          previewBody.appendChild(tr);
        });
      }

      previewDiv.style.display = "block";
    } catch (error) {
      console.error("Error en vista previa:", error);
      alert("No se pudo generar la vista previa");
    }
  });
});

  // ✅ Exportar PDF (ejemplo)
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    const tipo = tipoReporte.value;
    if (!tipo) {
      alert("Selecciona un tipo de reporte primero");
      return;
    }
    window.open(`/api/reportes/${tipo}`, "_blank"); // tu endpoint para generar PDF
  });

  */
 });
