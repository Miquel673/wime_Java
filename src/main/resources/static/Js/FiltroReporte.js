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
