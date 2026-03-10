// ======================================================
// TOAST GLOBAL - WIME
// ======================================================

function mostrarToast(mensaje, esExito = true, accion = null) {

  const toastEl = document.getElementById("toast-general");
  const texto = document.getElementById("toast-texto");

  if (!toastEl || !texto) {
    console.error("Toast no encontrado.");
    return;
  }

  texto.textContent = mensaje;

  toastEl.classList.remove("text-bg-success", "text-bg-danger");
  toastEl.classList.add(esExito ? "text-bg-success" : "text-bg-danger");

  const toast = new bootstrap.Toast(toastEl, { delay: 2500 });
  toast.show();

  toastEl.addEventListener("hidden.bs.toast", function () {

    // 🔹 Abrir login después del registro
    if (accion === "abrirLogin") {

      const registerModal = bootstrap.Modal.getInstance(
        document.getElementById("registerModal")
      );

      if (registerModal) registerModal.hide();

      const loginModal = new bootstrap.Modal(
        document.getElementById("loginModal")
      );

      loginModal.show();
    }

    // 🔹 Ir al tablero después del login
    if (accion === "irTablero") {
      window.location.href = "/HTML/Interfaces/Wime_interfaz_Tablero.html";
    }

  }, { once: true });

}