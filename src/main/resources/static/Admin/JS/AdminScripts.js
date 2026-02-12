document.addEventListener("DOMContentLoaded", cargarUsuarios);

function cargarUsuarios() {
    fetch("/admin/usuarios")
        .then(response => response.json())
        .then(data => {

            let tabla = document.getElementById("tablaUsuarios");
            tabla.innerHTML = "";

            data.forEach(usuario => {
                tabla.innerHTML += `
                    <tr>
                        <td>${usuario.idUsuario}</td>
                        <td>${usuario.nombreUsuario}</td>
                        <td>${usuario.emailUsuario}</td>
                        <td>${usuario.edad}</td>
                        <td>${usuario.tipo}</td>
                        <td>${usuario.estado}</td>
                        <td>${usuario.ultimoLogin ?? "Nunca"}</td>
                        <td>
                            <button onclick="activar(${usuario.idUsuario})">Activar</button>
                            <button onclick="bloquear(${usuario.idUsuario})">Inhabilitar</button>
                            <button onclick="eliminarUsuario(${usuario.idUsuario})">Eliminar</button>
                        </td>
                    </tr>
                `;
            });

        })
        .catch(error => console.error("Error:", error));
}

function activar(id) {
    fetch(`/admin/estado/${id}?estado=Activo`, {
        method: "PUT"
    }).then(() => cargarUsuarios());
}

function bloquear(id) {
    fetch(`/admin/estado/${id}?estado=Inactivo`, {
        method: "PUT"
    }).then(() => cargarUsuarios());
}

function eliminarUsuario(id) {
    fetch(`/admin/eliminar/${id}`, {
        method: "DELETE"
    }).then(() => cargarUsuarios());
}
