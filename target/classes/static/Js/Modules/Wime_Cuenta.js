let idUsuario = null;

document.addEventListener("DOMContentLoaded", () => {

  idUsuario = sessionStorage.getItem("idUsuario");

  if (!idUsuario) {
    console.warn("⚠️ Usuario no autenticado");
    return;
  }

  cargarFotoPerfil();
  cargarCorreoUsuario(); // 🔹 ahora sí se ejecuta

});

function validarContrasena(pass) {

  const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
  return regex.test(pass);

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
    console.error("❌ Error cargando foto", e);
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
    }

  }catch(e){
    console.error("❌ Error cargando correo", e);
  }

}

// =============================
// GUARDAR CAMBIOS PERFIL
// =============================
async function guardarCambiosPerfil(){

    const nombre = document.getElementById("nombreUsuario").value.trim();
    const imagen = document.getElementById("imageInput").files[0];

    if(nombre){
        await actualizarNombre(nombre);
    }

    if(imagen){
        await subirFoto(imagen);
    }

    mostrarToast("Perfil actualizado correctamente");

}

// =============================
// ACTUALIZAR NOMBRE
// =============================
async function actualizarNombre(nombre){

    await fetch(`/api/usuarios/${idUsuario}/actualizar-nombre`,{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        credentials:"include",
        body:JSON.stringify({
            nombre:nombre
        })
    });

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
        mostrarToast(data.message);
    }

}

// =============================
// CAMBIAR CONTRASEÑA
// =============================
async function confirmarCambiosToast(){

    const pass = document.getElementById("nuevaPassword").value.trim();
    const confirm = document.getElementById("confirmarPassword").value.trim();

    if(!pass || !confirm){
        mostrarToast("Debes completar ambos campos de contraseña");
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
                password:pass
            })
        });

        const data = await res.json();

        mostrarToast(data.message || "Contraseña actualizada");

        bootstrap.Modal.getInstance(
            document.getElementById('modalAjustesCuenta')
        ).hide();

        document.getElementById("nuevaPassword").value = "";
        document.getElementById("confirmarPassword").value = "";

    }catch(e){

        console.error("❌ Error cambiando contraseña", e);
        mostrarToast("Error actualizando contraseña");

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
});
