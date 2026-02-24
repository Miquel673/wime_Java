// ======================================================
// REGISTRO DE USUARIO - WIME
// ======================================================

// 🔹 1️⃣ Validación de contraseña
function validarContrasena(pass) {
    // Mínimo 8 caracteres, 1 mayúscula, 1 número y 1 símbolo
    const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    return regex.test(pass);
}

// 🔹 2️⃣ Toast Bootstrap 5
function mostrarToastRegistro(mensaje, esExito = true, redirigir = false) {
    const toastEl = document.getElementById("registro-toast");
    const texto = document.getElementById("registro-toast-texto");

    if (!toastEl || !texto) {
        console.error("Toast no encontrado en el HTML.");
        return;
    }

    texto.textContent = mensaje;

    toastEl.classList.remove("text-bg-success", "text-bg-danger");
    toastEl.classList.add(esExito ? "text-bg-success" : "text-bg-danger");

    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
    toast.show();

    toastEl.addEventListener("hidden.bs.toast", function () {
        if (redirigir && esExito) {
            window.location.href = "/Login.html";
        }
    }, { once: true });
}

// 🔹 3️⃣ Evento principal
document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("registroForm");

    if (!form) {
        console.error("Formulario registroForm no encontrado.");
        return;
    }

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const email = document.getElementById("EmailUsuario").value.trim();
        const nombre = document.getElementById("NombreUsuario").value.trim();
        const pass = document.getElementById("ContrasenaUsuario").value;
        const pass2 = document.getElementById("confirm_password").value;

        // 🔹 Validaciones
        if (!email || !nombre || !pass || !pass2) {
            mostrarToastRegistro("Todos los campos son obligatorios.", false);
            return;
        }

        if (pass !== pass2) {
            mostrarToastRegistro("Las contraseñas no coinciden.", false);
            return;
        }

        if (!validarContrasena(pass)) {
            mostrarToastRegistro(
                "La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un símbolo.",
                false
            );
            return;
        }

        // 🔹 Datos para el backend (coinciden con la entidad Usuario)
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
                mostrarToastRegistro(data.message || "Error en el registro.", false);
                return;
            }

            if (data.success) {
                mostrarToastRegistro(
                    "Registro exitoso. Redirigiendo al login...",
                    true,
                    true
                );
            } else {
                mostrarToastRegistro(data.message, false);
            }

        } catch (error) {
            console.error(error);
            mostrarToastRegistro("Error de conexión con el servidor.", false);
        }
    });
});