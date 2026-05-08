// ======================================================
// LOGIN DE USUARIO - WIME
// ======================================================

// 🔹 Validación de contraseña
function validarContrasena(pass) {
  const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
  return regex.test(pass);
}


function esAdministrador(rol) {
  if (!rol) return false;
  const valor = String(rol).toLowerCase();
  return valor.includes("admin");
}

function mostrarModalTipoAccesoAdmin() {
  const modalEl = document.getElementById("adminAccessChoiceModal");
  if (!modalEl || typeof bootstrap === "undefined") {
    window.location.href = "/admin/dashboard";
    return;
  }

  const modal = new bootstrap.Modal(modalEl);

  const btnComun = document.getElementById("btnAdminComoUsuarioComun");
  const btnAdmin = document.getElementById("btnAdminComoAdministrador");

  if (btnComun) {
    btnComun.onclick = () => {
      modal.hide();
      window.location.href = "/tablero";
    };
  }

  if (btnAdmin) {
    btnAdmin.onclick = () => {
      modal.hide();
      window.location.href = "/admin/dashboard";
    };
  }

  modal.show();
}


document.addEventListener("DOMContentLoaded", function () {

  console.log("Script de login cargado correctamente");

const googleAuthStatus = new URLSearchParams(window.location.search).get("googleAuth");
  if (googleAuthStatus) {
    const googleMessages = {
      success: { text: "Inicio de sesión con Google completado correctamente.", type: "success" },
      google_access_denied: { text: "Cancelaste el acceso con Google.", type: "error" },
      google_missing_code: { text: "Google no devolvió el código de autorización.", type: "error" },
      google_invalid_state: { text: "La sesión OAuth de Google expiró o no es válida. Intenta de nuevo.", type: "error" },
      google_config_error: { text: "Google Login no está configurado correctamente en el servidor.", type: "error" },
      google_token_error: { text: "No fue posible obtener el token de Google.", type: "error" },
      google_profile_error: { text: "No fue posible obtener el perfil de Google.", type: "error" },
      google_email_missing: { text: "La cuenta de Google no devolvió un correo electrónico válido.", type: "error" },
      google_callback_error: { text: "Ocurrió un error inesperado al procesar el login con Google.", type: "error" }
    };

    const feedback = googleMessages[googleAuthStatus];
    if (feedback && typeof mostrarToast === "function") {
      mostrarToast(feedback.text, feedback.type);
    }

    const cleanUrl = new URL(window.location.href);
    cleanUrl.searchParams.delete("googleAuth");
    window.history.replaceState({}, document.title, cleanUrl.pathname + cleanUrl.search + cleanUrl.hash);
  }


  const form = document.getElementById("form-login");

  const recoverForm = document.getElementById("recoverForm");
  const resetForm = document.getElementById("resetPasswordForm");

  if (!form) {
    console.error(" No se encontró el formulario con ID form-login.");
    return;
  }

  form.addEventListener("submit", function (e) {

    e.preventDefault();

    const email = document.getElementById("email").value.trim();
    const contrasena = document.getElementById("contrasena").value;

    if (!email || !contrasena) {
      mostrarToast("Debes llenar todos los campos.", "error");
      return;
    }

    fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `email=${encodeURIComponent(email)}&contrasena=${encodeURIComponent(contrasena)}`,
      credentials: "include"
    })
    .then(response => {
      if (!response.ok) {
        throw new Error("Error en la respuesta del servidor: " + response.status);
      }
      return response.json();
    })
    .then(data => {

      console.log("📦 Respuesta del servidor:", data);

      if (data.success) {

        const idUsuario =
          data.id_usuario ||
          data.idUsuario ||
          data.id ||
          data.usuarioId;

        const nombreUsuario = data.nombre || data.nombreUsuario || "Usuario";
        const rolUsuario = data.rol || data.rolUsuario || data.tipo || "Usuario";

        if (idUsuario) {
          sessionStorage.setItem("idUsuario", idUsuario);
        }

        sessionStorage.setItem("nombreUsuario", nombreUsuario);
        sessionStorage.setItem("rolUsuario", rolUsuario);

        if (esAdministrador(rolUsuario)) {
          mostrarModalTipoAccesoAdmin();
        } else {
          mostrarToast("Inicio de sesión exitoso", "success", "irTablero");
        }
        
      } else {
        mostrarToast(data.message || "Usuario o contraseña incorrectos", "error");
      }

    })
    .catch(error => {
      console.error(" Error en la solicitud:", error);
      mostrarToast("Error de conexión con el servidor", "error");
    });

  });

if (recoverForm) {
    recoverForm.addEventListener("submit", async function (e) {
      e.preventDefault();

      const recoverEmail = document.getElementById("recoverEmail").value.trim();

      if (!recoverEmail) {
        mostrarToast("Debes ingresar un correo.", "error");
        return;
      }

      try {
        const res = await fetch("/api/usuarios/recuperar-password", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email: recoverEmail })
        });

        const data = await res.json();

        if (!res.ok || !data.success) {
          mostrarToast(data.message || "No se pudo procesar la solicitud.", "error");
          return;
        }

        mostrarToast(`Hemos enviado un correo a ${recoverEmail}`, "success");

      } catch (error) {
        console.error(" Error recuperando contraseña:", error);
        mostrarToast("Error de conexión con el servidor.", "error");
      }
    });
  }

  if (resetForm) {
    resetForm.addEventListener("submit", async function (e) {
      e.preventDefault();

      const token = document.getElementById("resetToken").value;
      const nueva = document.getElementById("newPassword").value;
      const confirmar = document.getElementById("confirmNewPassword").value;

      if (!token) {
        mostrarToast("El enlace de recuperación no es válido.", "error");
        return;
      }

      if (!nueva || !confirmar) {
        mostrarToast("Debes completar todos los campos.", "error");
        return;
      }

      if (nueva !== confirmar) {
        mostrarToast("Las contraseñas no coinciden.", "error");
        return;
      }

      if (!validarContrasena(nueva)) {
        mostrarToast(
          "La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un símbolo.",
          "error"
        );
        return;
      }

      try {
        const res = await fetch("/api/usuarios/reset-password", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            token,
            password: nueva
          })
        });

        const data = await res.json();

        if (!res.ok || !data.success) {
          mostrarToast(data.message || "No se pudo cambiar la contraseña.", "error");
          return;
        }

        mostrarToast("Contraseña actualizada correctamente.", "success", "abrirLogin");

      } catch (error) {
        console.error(" Error cambiando contraseña:", error);
        mostrarToast("Error de conexión con el servidor.", "error");
      }
    });
  }

  const params = new URLSearchParams(window.location.search);
  const resetToken = params.get("resetToken");

  if (resetToken) {
    fetch(`/api/usuarios/recover-token/${encodeURIComponent(resetToken)}`)
      .then(res => res.json())
      .then(data => {
        if (!data.success) {
          mostrarToast("El enlace de recuperación expiró o no es válido.", "error");
          return;
        }

        document.getElementById("resetToken").value = resetToken;
        document.getElementById("resetEmailRef").value = data.email;

        const modal = new bootstrap.Modal(document.getElementById("resetPasswordModal"));
        modal.show();
      })
      .catch(err => {
        console.error(" Error validando token:", err);
        mostrarToast("No se pudo validar el enlace de recuperación.", "error");
      });
  }

});

