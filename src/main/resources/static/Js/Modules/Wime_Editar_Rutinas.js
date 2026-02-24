document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const rutinaId = params.get("id");

  if (!rutinaId) {
    alert("⚠️ No se proporcionó ID de rutina");
    window.location.href = "../../HTML/Interfaces/Wime_interfaz_Tablero.html";
    return;
  }

  const form = document.getElementById("form-editar-rutina");

  // ✅ Cargar datos de la rutina
  fetch(`/api/rutinas/${rutinaId}`)
    .then(res => {
      if (!res.ok) throw new Error("Error al obtener la rutina");
      return res.json();
    })
    .then(rutina => {
      form.querySelector("[name='idRutina']").value = rutina.idRutina;
      form.querySelector("[name='nombreRutina']").value = rutina.nombreRutina;
      form.querySelector("[name='prioridad']").value = rutina.prioridad;
      form.querySelector("[name='frecuencia']").value = rutina.frecuencia;
      form.querySelector("[name='fechaFin']").value = rutina.fechaFin;
      form.querySelector("[name='descripcion']").value = rutina.descripcion;
      form.querySelector("[name='estado']").value = rutina.estado;
    })
    .catch(err => {
      console.error("❌ Error:", err);
      alert("❌ Error al cargar la rutina");
      window.location.href = "../../HTML/Interfaces/Wime_interfaz_Tablero.html";
    });

  // ✅ Guardar cambios
  form.addEventListener("submit", (e) => {
    e.preventDefault();

    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    fetch(`/api/rutinas/editar/${rutinaId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    })
      .then(res => res.json())
      .then(resp => {
        if (resp.success) {
          alert("✅ Rutina actualizada con éxito");
          window.location.href = "../../HTML/Interfaces/Wime_interfaz_Tablero.html";
        } else {
          alert("❌ " + resp.message);
        }
      })
      .catch(err => console.error("❌ Error:", err));
  });
});
