//Creacion de Tareas//

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("form-tarea");

  if (!form) {
    console.error("❌ Formulario no encontrado");
    return;
  }

  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const datos = new FormData(form);

    fetch("/Wime/Controllers/TController.php", {
      method: "POST",
      body: datos
    })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          mostrarToast("✅ Tarea creada correctamente", true, true);
          alert("Tarea creada exitosamente")
          form.reset();
        } else {
          mostrarToast(data.message || "❌ No se pudo crear la tarea", false);
        }
      })
      .catch(err => {
        console.error("❌ Error:", err);
        mostrarToast("❌ Error de conexión con el servidor", false);
      });
  });
});

function mostrarToast(mensaje, esExito = true, redirigir = false) {
  const toast = document.getElementById("mensaje-flotante");
  const texto = document.getElementById("texto-toast");

  if (!toast || !texto) {
    console.warn("⚠️ Toast no está definido correctamente en el HTML");
    return;
  }

  texto.textContent = mensaje;
  toast.classList.remove("d-none", "bg-danger", "bg-success");
  toast.classList.add(esExito ? "bg-success" : "bg-danger");

  setTimeout(() => {
    toast.classList.add("d-none");
    if (redirigir && esExito) {
      window.location.href = "/Wime/private/PhP/Wime_interfaz_Tablero.php";
    }
  }, 1000);
}


