// ======================================================
// TOAST GLOBAL - WIME (UNIFICADO)
// ======================================================

// Crear contenedor automáticamente si no existe
document.addEventListener("DOMContentLoaded", () => {

  if (!document.getElementById("toast-container")) {

    const container = document.createElement("div");
    container.id = "toast-container";

    container.style.position = "fixed";
    container.style.top = "20px";
    container.style.right = "20px";
    container.style.zIndex = "9999";

    document.body.appendChild(container);
  }

});

// ======================================================
// FUNCION PRINCIPAL
// ======================================================

function mostrarToast(mensaje, tipo = "info", accion = null) {

  const colores = {
    success: "text-bg-success",
    error: "text-bg-danger",
    warning: "text-bg-warning",
    info: "text-bg-primary"
  };

  const claseColor = colores[tipo] || colores.info;

  // ==================================================
  // CASO 1: Toast estático (Login / Registro)
  // ==================================================

  const toastGeneral = document.getElementById("toast-general");
  const texto = document.getElementById("toast-texto");

  if (toastGeneral && texto) {

    texto.textContent = mensaje;

    toastGeneral.classList.remove(
      "text-bg-success",
      "text-bg-danger",
      "text-bg-warning",
      "text-bg-primary"
    );

    toastGeneral.classList.add(claseColor);

    const toast = new bootstrap.Toast(toastGeneral, { delay: 2500 });
    toast.show();

    toastGeneral.addEventListener("hidden.bs.toast", () => {

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

      if (accion === "irTablero") {
        window.location.href = "/HTML/Interfaces/Wime_interfaz_Tablero.html";
      }

    }, { once: true });

    return;
  }

  // ==================================================
  // CASO 2: Toast dinámico (Cuenta / Tablero / otros)
  // ==================================================

  const container = document.getElementById("toast-container");

  if (!container) {
    console.warn("No existe contenedor de toast.");
    return;
  }

  const toastHTML = `
    <div class="toast align-items-center ${claseColor} border-0 mb-2 shadow"
         role="alert"
         aria-live="assertive"
         aria-atomic="true">

      <div class="d-flex">
        <div class="toast-body">
          ${mensaje}
        </div>

        <button type="button"
                class="btn-close btn-close-white me-2 m-auto"
                data-bs-dismiss="toast">
        </button>
      </div>

    </div>
  `;

  container.insertAdjacentHTML("beforeend", toastHTML);

  const toastElement = container.lastElementChild;

  const toast = new bootstrap.Toast(toastElement, {
    delay: 4000
  });

  toast.show();

  toastElement.addEventListener("hidden.bs.toast", () => {
    toastElement.remove();
  });

}
