// ======================================================
// REGISTRO DE USUARIO - WIME
// ======================================================

// 🔹 Validación de contraseña
function validarContrasena(pass) {
  const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
  return regex.test(pass);
}

document.addEventListener("DOMContentLoaded", function () {

  const form = document.getElementById("registroForm");

  if (!form) {
    console.error("❌ Formulario registroForm no encontrado.");
    return;
  }

  form.addEventListener("submit", async function (e) {

    e.preventDefault();

    const email = document.getElementById("EmailUsuario").value.trim();
    const nombre = document.getElementById("NombreUsuario").value.trim();
    const pass = document.getElementById("ContrasenaUsuario").value;
    const pass2 = document.getElementById("confirm_password").value;

    if (!email || !nombre || !pass || !pass2) {
      mostrarToast("Todos los campos son obligatorios.", false);
      return;
    }

    if (pass !== pass2) {
      mostrarToast("Las contraseñas no coinciden.", false);
      return;
    }

    if (!validarContrasena(pass)) {
      mostrarToast(
        "La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un símbolo.",
        false
      );
      return;
    }

    const datos = {
      emailUsuario: email,
      nombreUsuario: nombre,
      contrasenaUsuario: pass
    };

    try {

      const res = await fetch("/api/usuarios/registro", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(datos)
      });

      const data = await res.json();

      if (!res.ok) {
        mostrarToast(data.message || "Error en el registro.", false);
        return;
      }

      if (data.success) {

        mostrarToast(
          "Registro exitoso. Ahora puedes iniciar sesión.",
          true,
          "abrirLogin"
        );

      } else {
        mostrarToast(data.message, false);
      }

    } catch (error) {
      console.error(error);
      mostrarToast("Error de conexión con el servidor.", false);
    }

  });

});