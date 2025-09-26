//Creacion de Rutinas//

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("form-rutina");

  if (!form) {
    console.error("❌ Formulario de rutina no encontrado");
    return;
  }

  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const datos = new FormData(form);

    fetch("/Wime/Controllers/RController.php", {
      method: "POST",
      body: datos
    })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          mostrarToast("✅ Rutina creada exitosamente", true, true);
          alert("rutina creada exitosamente")
          form.reset();
        } else {
          mostrarToast(data.message || "❌ No se pudo crear la rutina", false);
        }
      })
      .catch(err => {
        console.error("❌ Error al crear la rutina:", err);
        mostrarToast("❌ Error de conexión", false);
      });
  });
});

// Puedes reutilizar esto si no lo tienes en global:
function mostrarToast(mensaje, esExito = true, redirigir = false) {
  const toast = document.getElementById("mensaje-flotante");
  const texto = document.getElementById("texto-toast");

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
