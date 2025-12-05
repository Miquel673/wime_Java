document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const tareaId = params.get("id");

  if (!tareaId) {
    alert("⚠️ No se proporcionó ID de tarea");
    window.location.href = "/HTML/Wime_interfaz_Tablero.html";
    return;
  }

  const form = document.getElementById("form-editar-tarea");

  // ✅ Cargar datos de la tarea en el formulario
  fetch(`/api/tareas/${tareaId}`)
    .then(res => {
      if (!res.ok) throw new Error("Error al obtener la tarea");
      return res.json();
    })
    .then(data => {
      if (!data.success || !data.tarea) throw new Error("Datos de tarea inválidos");
      const tarea = data.tarea;

      // Asignar valores correctamente según los nombres reales
      form.querySelector("[name='id']").value = tarea.idTarea;
      form.querySelector("[name='titulo']").value = tarea.titulo;
      form.querySelector("[name='descripcion']").value = tarea.descripcion || "";
      form.querySelector("[name='estado']").value = tarea.estado || "pendiente";

      if (tarea.fechaLimite) {
        const fecha = new Date(tarea.fechaLimite);
        const fechaISO = fecha.toISOString().split("T")[0];
        form.querySelector("[name='fecha_limite']").value = fechaISO;
      }

      // marcar prioridad
      form.querySelectorAll("[name='prioridad']").forEach(radio => {
        radio.checked = (radio.value === tarea.prioridad);
      });
    })
    .catch(err => {
      console.error("❌ Error:", err);
      alert("❌ Error al cargar la tarea");
      window.location.href = "/HTML/Wime_interfaz_Tablero.html";
    });

  // ✅ Guardar cambios
  form.addEventListener("submit", (e) => {
    e.preventDefault();

    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // Ajuste de campos
    const tareaActualizada = {
      titulo: data.titulo,
      descripcion: data.descripcion,
      estado: data.estado,
      prioridad: data.prioridad,
      fechaLimite: data.fecha_limite || null
    };

    fetch(`/api/tareas/editar/${tareaId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(tareaActualizada)
    })
      .then(res => res.json())
      .then(result => {
        if (result.success) {
          alert("✅ Tarea actualizada correctamente");
          window.location.href = "/HTML/Wime_interfaz_Tablero.html";
        } else {
          throw new Error(result.message || "Error desconocido al guardar");
        }
      })
      .catch(err => {
        console.error("❌ Error al guardar:", err);
        alert("❌ No se pudo guardar la tarea");
      });
  });
});
