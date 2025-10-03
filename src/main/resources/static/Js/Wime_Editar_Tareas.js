document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const tareaId = params.get("id");

  if (!tareaId) {
    alert("⚠️ No se proporcionó ID de tarea");
    window.location.href = "/HTML/Wime_interfaz_Tablero.html"; // 👈 redirige al tablero
    return;
  }

  const form = document.getElementById("form-editar-tarea");

  // ✅ Cargar datos de la tarea en el formulario
  fetch(`/api/tareas/${tareaId}`)
    .then(res => {
      if (!res.ok) throw new Error("Error al obtener la tarea");
      return res.json();
    })
    .then(tarea => {
      form.querySelector("[name='id']").value = tarea.id;
      form.querySelector("[name='titulo']").value = tarea.titulo;
      if (tarea.fechaLimite) {
  const fecha = new Date(tarea.fechaLimite);
  // Ajustar al formato YYYY-MM-DD
  const fechaISO = fecha.toISOString().split("T")[0];
  form.querySelector("[name='fecha_limite']").value = fechaISO;
}

      form.querySelector("[name='descripcion']").value = tarea.descripcion;
      form.querySelector("[name='estado']").value = tarea.estado;

      // marcar prioridad
      form.querySelectorAll("[name='prioridad']").forEach(radio => {
        if (radio.value === tarea.prioridad) {
          radio.checked = true;
        }
      });
    })
    .catch(err => {
      console.error("❌ Error:", err);
      alert("❌ Error al cargar la tarea");
      window.location.href = "/HTML/Wime_interfaz_Tablero.html"; // seguridad extra
    });

  // ✅ Guardar cambios
  form.addEventListener("submit", (e) => {
    e.preventDefault();

const formData = new FormData(form);
const data = Object.fromEntries(formData.entries());

// Convertir fecha vacía en null
if (!data.fecha_limite) {
  data.fechaLimite = null;
} else {
  data.fechaLimite = data.fecha_limite;
}

delete data.fecha_limite; // 👈 quitamos el duplicado

    fetch(`/api/tareas/editar/${tareaId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    })
      .then(res => res.json())
      .then(resp => {
        if (resp.success) {
          alert("✅ Tarea actualizada con éxito");
          window.location.href = "/HTML/Wime_interfaz_Tablero.html";
        } else {
          alert("❌ " + resp.message);
        }
      })
      .catch(err => console.error("❌ Error:", err));
  });
});


