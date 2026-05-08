let idUsuario = null;
let correoUsuarioActual = "";

const CANAL_NOTIFICACION = {
  ESCRITORIO: "Notificación de escritorio",
  PUSH: "Notificación Push"
};

async function verificarSesionCuenta() {
  try {
    const res = await fetch("/api/auth/check-session", {
      method: "GET",
      credentials: "include",
      cache: "no-store"
    });
    const data = await res.json();

    if (!data.active) {
      window.location.replace("/");
      return null;
    }

    if (data.idUsuario) {
      sessionStorage.setItem("idUsuario", data.idUsuario);
    }

    return data;
  } catch (err) {
    console.error(" Error verificando sesión:", err);
    window.location.replace("/");
    return null;
  }
}

window.addEventListener("pageshow", () => {
  verificarSesionCuenta();
});

document.addEventListener("DOMContentLoaded", async () => {

  const sesion = await verificarSesionCuenta();
  if (!sesion) return;
  idUsuario = sessionStorage.getItem("idUsuario");

  if (!idUsuario) {
    console.warn("⚠️ Usuario no autenticado");
    return;
  }

  cargarFotoPerfil();
  cargarCorreoUsuario(); // 🔹 ahora sí se ejecuta

  const modalEliminar = document.getElementById("modalEliminarCuenta");
  if(modalEliminar){
    modalEliminar.addEventListener("show.bs.modal", () => {
      const correoEliminar = document.getElementById("correoEliminarCuenta");
      if(correoEliminar){
        correoEliminar.textContent = correoUsuarioActual ? `(${correoUsuarioActual})` : "";
      }
      const inputPassword = document.getElementById("passwordEliminarCuenta");
      if(inputPassword){
        inputPassword.value = "";
      }
    });
  }
  
      if (localStorage.getItem("theme") === "dark") {
        document.body.classList.add("dark");
        document.getElementById("themeIcon").className = "bi bi-moon-stars-fill";
    }

});

function validarContrasena(pass) {

  const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
  return regex.test(pass);

}

function obtenerCanalNotificacion() {
  return CANAL_NOTIFICACION.PUSH;
}

async function registrarNotificacionCambio(mensaje, canal = obtenerCanalNotificacion()) {
  if (!idUsuario || !mensaje) return;

  try {
    const params = new URLSearchParams({
      idUsuario,
      tipo: canal,
      mensaje
    });

    const res = await fetch(`/api/notificaciones/crear?${params.toString()}`, {
      method: "POST",
      credentials: "include"
    });

    if (!res.ok) {
      throw new Error("No se pudo registrar la notificación");
    }

    if (typeof mostrarNotificacion === "function") {
      await mostrarNotificacion(canal, mensaje, true);
    }
  } catch (error) {
    console.error(" Error registrando notificación de cambio", error);
  }
}

// =============================
// CARGAR FOTO
// =============================
async function cargarFotoPerfil(){

  if (!idUsuario) return;

  try {

    const res = await fetch(`/api/usuarios/${idUsuario}/foto`,{
      credentials:"include"
    });

    const data = await res.json();

    if(data.fotoPerfil){
      document.getElementById("preview").src =
        data.fotoPerfil + "?t=" + Date.now();
    }

  }catch(e){
    console.error(" Error cargando foto", e);
  }

}
// =============================
// CARGAR CORREO
// =============================
async function cargarCorreoUsuario(){

  if (!idUsuario) return;

  try{

    const res = await fetch(`/api/usuarios/${idUsuario}`,{
      credentials:"include"
    });

    const data = await res.json();

    console.log("Respuesta backend:", data);

const correo = document.getElementById("correoUsuario");

    if(data.success && correo){
        correo.textContent = data.email;
        correoUsuarioActual = data.email || "";

        const correoEliminar = document.getElementById("correoEliminarCuenta");
        if(correoEliminar){
            correoEliminar.textContent = correoUsuarioActual ? `(${correoUsuarioActual})` : "";
        }
    }

  }catch(e){
    console.error(" Error cargando correo", e);
  }

}

// =============================
// GUARDAR CAMBIOS PERFIL
// =============================
async function guardarCambiosPerfil(){

    const nombre = document.getElementById("nombreUsuario").value.trim();
    const imagen = document.getElementById("imageInput").files[0];
    const cambiosAplicados = [];

    try {
        if(nombre){
            await actualizarNombre(nombre);
            cambiosAplicados.push("nombre de usuario");
        }

        if(imagen){
            await subirFoto(imagen);
            cambiosAplicados.push("foto de perfil");
        }

        if(cambiosAplicados.length === 0){
            mostrarToast("No hay cambios para guardar");
            return;
        }

        const mensaje = cambiosAplicados.length === 2
          ? "Se actualizaron tu nombre de usuario y tu foto de perfil."
          : `Se actualizó tu ${cambiosAplicados[0]}.`;

        await registrarNotificacionCambio(mensaje);
        mostrarToast("Perfil actualizado correctamente");
    } catch (error) {
        console.error(" Error guardando cambios de perfil", error);
        mostrarToast(error.message || "No se pudo actualizar el perfil");
    }

}

// =============================
// ACTUALIZAR NOMBRE
// =============================
async function actualizarNombre(nombre){

    const res = await fetch(`/api/usuarios/${idUsuario}/actualizar-nombre`,{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        credentials:"include",
        body:JSON.stringify({
            nombre:nombre
        })
    });

    const data = await res.json();

    if(!res.ok || !data.success){
        throw new Error(data.message || "No se pudo actualizar el nombre");
    }

}

// =============================
// SUBIR FOTO
// =============================
async function subirFoto(file){

    const formData = new FormData();
    formData.append("file", file);
    formData.append("idUsuario", idUsuario);

    const res = await fetch("/api/usuarios/subir-foto",{
        method:"POST",
        body:formData
    });

    const data = await res.json();

    if(data.success){
        document.getElementById("preview").src = data.urlFoto;
    }else{
        throw new Error(data.message || "No se pudo actualizar la foto de perfil");
    }

}

// =============================
// CAMBIAR CONTRASEÑA
// =============================
async function confirmarCambiosToast(){

    const actual = document.getElementById("passwordActual").value.trim();
    const pass = document.getElementById("nuevaPassword").value.trim();
    const confirm = document.getElementById("confirmarPassword").value.trim();

    if(!actual || !pass || !confirm){
        mostrarToast("Debes completar todos los campos de contraseña");
        return;
    }

    if(!validarContrasena(pass)){
        mostrarToast("La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un símbolo.");
        return;
    }

    if(pass !== confirm){
        mostrarToast("Las contraseñas no coinciden");
        return;
    }

    try{

        const res = await fetch(`/api/usuarios/${idUsuario}/cambiar-password`,{
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            credentials:"include",
            body:JSON.stringify({
                currentPassword:actual,
                password:pass
            })
        });

        const data = await res.json();

        if(!res.ok || !data.success){
            mostrarToast(data.message || "No se pudo actualizar la contraseña");
            return;
        }

        await registrarNotificacionCambio("Se realizó un cambio de contraseña en tu cuenta.");
        mostrarToast(data.message || "Contraseña actualizada");

        bootstrap.Modal.getInstance(
            document.getElementById('modalAjustesCuenta')
        ).hide();

        document.getElementById("passwordActual").value = "";
        document.getElementById("nuevaPassword").value = "";
        document.getElementById("confirmarPassword").value = "";

    }catch(e){

        console.error(" Error cambiando contraseña", e);
        mostrarToast("Error actualizando contraseña");

    }

}

async function confirmarEliminacionCuenta(){

    const password = document.getElementById("passwordEliminarCuenta").value.trim();

    if(!password){
        mostrarToast("Debes confirmar tu contraseña para eliminar la cuenta");

                return;
    }
    try{

        const res = await fetch(`/api/usuarios/${idUsuario}/eliminar-cuenta`,{
            method:"DELETE",
            headers:{
                "Content-Type":"application/json"
            },
            credentials:"include",
            body:JSON.stringify({
                password:password
            })
        });

        const data = await res.json();

        if(!res.ok || !data.success){
            mostrarToast(data.message || "No se pudo eliminar la cuenta");
            return;
        }

        mostrarToast(data.message || "Cuenta eliminada correctamente");

        sessionStorage.removeItem("idUsuario");

        setTimeout(() => {
            window.location.href = "/";
        }, 1000);

    }catch(e){
        console.error(" Error eliminando cuenta", e);
        mostrarToast("Error eliminando la cuenta");
    }

}


// =============================
// MOSTRAR / OCULTAR PASSWORD
// =============================
function togglePassword(idInput, icon){

    const input = document.getElementById(idInput);

    if(input.type === "password"){
        input.type = "text";
        icon.classList.remove("bi-eye-slash");
        icon.classList.add("bi-eye");
    }else{
        input.type = "password";
        icon.classList.remove("bi-eye");
        icon.classList.add("bi-eye-slash");
    }

}


// =============================
// CAMBIO DE TEMA
// =============================
document.getElementById("themeToggle").addEventListener("click", () => {

    const isDark = document.body.classList.toggle("dark");

    localStorage.setItem("theme", isDark ? "dark" : "light");

    document.getElementById("themeIcon").className =
        isDark ? "bi bi-moon-stars-fill" : "bi bi-sun";


    // Fuerza refresco visual de tarjetas para asegurar contraste en ambos temas
    if (typeof renderItems === "function") {
        renderItems();
    }
});


async function cerrarSesion() {
    if (!confirm("¿Cerrar sesión?")) return;

    try {
        await fetch("/api/auth/logout", {
            method: "POST",
            credentials: "include",
            cache: "no-store"
        });
    } catch (e) {
        console.warn("No se pudo invalidar sesión en backend:", e);
    }

    sessionStorage.clear();
    localStorage.removeItem("usuario");
    window.location.replace("/");
}