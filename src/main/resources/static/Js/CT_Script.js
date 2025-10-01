document.addEventListener("DOMContentLoaded", () => {
  const formTarea = document.getElementById("form-Tarea");

  if (!formTarea) {
    console.error("⚠️ No se encontró el formulario con id 'formTarea'");
    return;
  }

  formTarea.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Validar campos antes de enviar
  const titulo = document.getElementById("titulo")?.value.trim();
  const prioridad = document.querySelector('input[name="prioridad"]:checked')?.value;
  const fechaLimite = document.getElementById("fecha_limite")?.value.trim();
  const compartirCon = document.getElementById("compartir_con")?.value.trim();
  const descripcion = document.getElementById("descripcion")?.value.trim();

  if (!titulo || !prioridad || !fechaLimite) {
    mostrarToast("⚠️ Debes completar al menos Título, Prioridad y Fecha Límite", "error");
    return;
  }


    try {
      const response = await fetch("http://localhost:8080/api/tareas/crear", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          titulo,
          prioridad,
          fechaLimite,
          compartirCon,
          descripcion
        })
      });

      if (!response.ok) {
        throw new Error("❌ Error en la creación de la tarea");
      }

      const data = await response.json();

      if (data.success) {
        mostrarToast("✅ Tarea creada con éxito", "success");
        formTarea.reset(); // limpia el formulario
      } else {
        mostrarToast("⚠️ No se pudo crear la tarea", "error");
      }

    } catch (error) {
      console.error("❌ Error:", error);
      mostrarToast("❌ Error al conectar con el servidor", "error");
    }


    
  // Esperar 1.5 segundos y redirigir al tablero
  setTimeout(() => {
    window.location.href = "/HTML/Wime_interfaz_Tablero.html"; // 👈 ajusta la ruta si tu tablero tiene otro nombre
  }, 100);


    
  });

// Función para mostrar notificaciones (fallback si no existe aún)
function mostrarToast(mensaje, tipo = "info") {
  alert(mensaje); // temporal, luego lo puedes reemplazar con un toast bonito
}

});