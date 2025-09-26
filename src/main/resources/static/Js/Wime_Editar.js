document.addEventListener("DOMContentLoaded", () => {
  const formulario = document.getElementById("form-editar-tarea");

  formulario.addEventListener("submit", async function (e) {
    e.preventDefault();

    const datos = new FormData(formulario);

    try {
      const res = await fetch("/Wime/Controllers/EDTConctroller.php", {
        method: "POST",
        body: datos,
      });

      const respuesta = await res.json();
      if (respuesta.success) {
        alert("✅ Cambios guardados");
        window.location.href = "/Wime/private/PhP/Wime_interfaz_Tablero.php";
      } else {
        alert(respuesta.message || "❌ No se pudo guardar");
      }
    } catch (error) {
      console.error("Error al guardar:", error);
      alert("❌ Error al conectar con el servidor");
    }
  });
});


//Rutinas//

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("form-editar-rutina");

  form.addEventListener("submit", e => {
    e.preventDefault();
    const datos = new FormData(form);

    fetch("/Wime/Controllers/EDRController.php", {
      method: "POST",
      body: datos
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        alert("✅ Rutina actualizada correctamente");
        window.location.href = "/Wime/private/PhP/Wime_interfaz_Tablero.php";
      } else {
        alert("❌ " + data.message);
      }
    })
    .catch(err => {
      console.error("❌ Error en la solicitud:", err);
    });
  });
});
