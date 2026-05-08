let loginChartInstance = null;
let adminSessionId = null;

const esAdmin = (tipo = "") => String(tipo).toLowerCase().includes("admin");

document.addEventListener("DOMContentLoaded", async () => {
    aplicarTemaGuardado();
    await cargarSesionAdmin();
    await cargarUsuarios();
    await cargarNotificacionesAdmin();
});

async function cargarSesionAdmin() {
    try {
        const res = await fetch("/api/auth/check-session", {
            method: "GET",
            credentials: "include"
        });

        const data = await res.json();

        if (!data.active) {
            window.location.href = "/";
            return;
        }

        const bienvenida = document.getElementById("bienvenidaAdmin");
        const rol = document.getElementById("rolAdmin");

        if (bienvenida) bienvenida.textContent = `Bienvenido, ${data.usuario || "Administrador"}`;
        if (rol) rol.textContent = `Rol: ${data.rol || "Administrador"}`;

        adminSessionId = sessionStorage.getItem("idUsuario");
        if (adminSessionId) {
            await cargarFotoPerfilAdmin(adminSessionId);
        }
    } catch (error) {
        console.error("Error verificando sesión admin:", error);
        window.location.href = "/";
    }
}

async function cargarFotoPerfilAdmin(idUsuario) {
    try {
        const res = await fetch(`/api/usuarios/${idUsuario}/foto`, { credentials: "include" });
        const data = await res.json();
        const foto = document.getElementById("fotoPerfilAdmin");

        if (foto && data.fotoPerfil) {
            foto.src = `${data.fotoPerfil}?t=${Date.now()}`;
        }
    } catch (error) {
        console.warn("No se pudo cargar foto de perfil del admin:", error);
    }
}

async function cargarUsuarios() {
    try {
        const response = await fetch("/admin/usuarios", { credentials: "include" });
        if (!response.ok) throw new Error(`Error cargando usuarios: ${response.status}`);

        const data = await response.json();
        renderTablaUsuarios(data);
        renderResumen(data);
        renderGraficoUltimoLogin(data);
        renderListaAdministradores(data);
    } catch (error) {
        console.error("Error:", error);
        const tabla = document.getElementById("tablaUsuarios");
        if (tabla) tabla.innerHTML = `<tr><td colspan="7">No se pudo cargar la información de usuarios.</td></tr>`;
    }
}

async function cargarNotificacionesAdmin() {
    const lista = document.getElementById("listaNotificacionesAdmin");
if (!lista) return;

    try {
        const res = await fetch(`/admin/notificaciones`, { credentials: "include" });        if (!res.ok) throw new Error("No se pudieron cargar notificaciones");

        const notificaciones = await res.json();

        if (!Array.isArray(notificaciones) || notificaciones.length === 0) {
            lista.innerHTML = "<li>No hay notificaciones.</li>";
            return;
        }

        lista.innerHTML = notificaciones.map(n => `
            <li>
                <div>
                    <div>${n.mensaje || "Sin mensaje"}</div>
                    <div class="notif-meta">Usuario: ${n.nombreUsuario || "Sin usuario"} • ${(n.fecha || "").replace("T", " ")} • ${n.tipo || "Admin"}</div>
                </div>
                <button class="notif-delete" onclick="eliminarNotificacionAdmin(${n.id})" title="Eliminar notificación">
                    <i class="bi bi-trash"></i>
                </button>
            </li>
        `).join("");
    } catch (error) {
        console.error(error);
        lista.innerHTML = "<li>Error al cargar notificaciones.</li>";
    }
}

async function eliminarNotificacionAdmin(idNotificacion) {
        await fetch(`/admin/notificaciones/${idNotificacion}`, {
        method: "DELETE",
        credentials: "include"
    });
    await cargarNotificacionesAdmin();
}

async function limpiarNotificacionesAdmin() {
    const confirmar = confirm("¿Eliminar todas las notificaciones administrativas?");
    if (!confirmar) return;

    await fetch(`/admin/notificaciones`, {
        method: "DELETE",
        credentials: "include"
    });
    await cargarNotificacionesAdmin();
}

function renderListaAdministradores(usuarios) {
    const lista = document.getElementById("listaAdministradores");
    if (!lista) return;

    const administradores = usuarios.filter(u => esAdmin(u.tipo));
    if (administradores.length === 0) {
        lista.innerHTML = "<li>No hay administradores registrados</li>";
        return;
    }

    lista.innerHTML = administradores
        .map(admin => `<li><i class="bi bi-person-check"></i> ${admin.nombreUsuario} (${admin.emailUsuario})</li>`)
        .join("");
}

function renderTablaUsuarios(usuarios) {
    const tabla = document.getElementById("tablaUsuarios");
    tabla.innerHTML = "";

    usuarios.forEach(usuario => {
        const estado = (usuario.estado || "Inactivo").toLowerCase();
        const badgeClass = estado === "activo" ? "status-active" : "status-inactive";
        const usuarioEsAdmin = esAdmin(usuario.tipo);

        tabla.innerHTML += `
            <tr>
                <td style="color: #94a3b8;">#${usuario.idUsuario}</td>
                <td style="font-weight: 600;">${usuario.nombreUsuario}</td>
                <td>${usuario.emailUsuario}</td>
                <td>${usuario.tipo || "Usuario"}</td>
                <td><span class="status-badge ${badgeClass}">${usuario.estado || "Inactivo"}</span></td>
                <td>${usuario.ultimoLogin ?? "Nunca"}</td>
                <td>
                    <div class="btn-group">
                        <button class="btn-crud btn-activate" onclick="activar(${usuario.idUsuario})" title="Activar">
                            <i class="bi bi-check-circle-fill"></i>
                        </button>
                        <button class="btn-crud btn-block" onclick="inactivar(${usuario.idUsuario})" title="Inactivar">
                            <i class="bi bi-slash-circle-fill"></i>
                        </button>
                        <button class="btn-crud btn-delegate" onclick="delegarAdministrador(${usuario.idUsuario})" title="Delegar como administrador">
                            <i class="bi bi-person-up"></i>
                        </button>
                        <button class="btn-crud btn-delete" onclick="eliminarUsuario(${usuario.idUsuario}, ${usuarioEsAdmin})" title="Eliminar" ${usuarioEsAdmin ? "disabled" : ""}>
                            <i class="bi bi-trash3-fill"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    });
}

function renderResumen(usuarios) {
    const activos = usuarios.filter(u => (u.estado || "").toLowerCase() === "activo").length;
    const inactivos = usuarios.length - activos;

    document.getElementById("totalUsuarios").textContent = usuarios.length;
    document.getElementById("usuariosActivos").textContent = activos;
    document.getElementById("usuariosInactivos").textContent = inactivos;
}

function renderGraficoUltimoLogin(usuarios) {
    const loginMap = new Map();

    usuarios.forEach(u => {
        const fecha = u.ultimoLogin ? String(u.ultimoLogin).split("T")[0] : "Nunca";
        loginMap.set(fecha, (loginMap.get(fecha) || 0) + 1);
    });

    const labels = Array.from(loginMap.keys());
    const values = Array.from(loginMap.values());

    const ctx = document.getElementById("loginChart");
    if (!ctx) return;

    if (loginChartInstance) loginChartInstance.destroy();

    loginChartInstance = new Chart(ctx, {
        type: "line",
        data: {
            labels,
            datasets: [{
                label: "Usuarios por Último_Login",
                data: values,
                backgroundColor: "rgba(67, 97, 238, 0.55)",
                borderColor: "rgba(67, 97, 238, 1)",
                borderWidth: 2,
                tension: 0.3,
                fill: false,
                pointRadius: 4
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: true } },
            scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
        }
    });
}

async function cambiarEstado(id, estado) {
    await fetch(`/admin/estado/${id}?estado=${encodeURIComponent(estado)}`, {
        method: "PUT",
        credentials: "include"
    });
    await cargarUsuarios();
    await cargarNotificacionesAdmin();
}

function activar(id) {
    cambiarEstado(id, "Activo");
}

function inactivar(id) {
    cambiarEstado(id, "Inactivo");
}

async function delegarAdministrador(id) {
    const confirmarDelegacion = confirm("¿Deseas delegar permisos de administrador a este usuario?");
    if (!confirmarDelegacion) return;

    await fetch(`/admin/tipo/${id}?tipo=Admin`, {
        method: "PUT",
        credentials: "include"
    });

    await cargarUsuarios();
    await cargarNotificacionesAdmin();
}

async function eliminarUsuario(id, usuarioEsAdmin = false) {
    if (usuarioEsAdmin) {
        alert("No se puede eliminar un usuario con rol administrador.");
        return;
    }

    const confirmado = confirm("¿Seguro que quieres eliminar este usuario? No podrás deshacerlo.");
    if (!confirmado) return;

    const response = await fetch(`/admin/eliminar/${id}`, {
        method: "DELETE",
        credentials: "include"
    });

    if (!response.ok) {
        const mensaje = await response.text();
        alert(mensaje || "No se pudo eliminar el usuario.");
    }

    await cargarUsuarios();
    await cargarNotificacionesAdmin();
}

function exportarReporteAdmin() {
    window.open("/admin/reporte/pdf", "_blank");
}

function toggleTheme() {
    const body = document.body;
    const icon = document.getElementById("themeIcon");

    body.classList.toggle("dark");
    const isDark = body.classList.contains("dark");

    icon.className = isDark ? "bi bi-moon-stars-fill" : "bi bi-sun-fill";
    localStorage.setItem("theme", isDark ? "dark" : "light");
}

function aplicarTemaGuardado() {
    if (localStorage.getItem("theme") === "dark") {
        document.body.classList.add("dark");
        document.getElementById("themeIcon").className = "bi bi-moon-stars-fill";
    }
}

async function cerrarSesion() {
    const confirmarCierre = confirm("¿Cerrar sesión del panel?");
    if (!confirmarCierre) return;

    try {
        await fetch("/api/auth/logout", { method: "POST", credentials: "include" });
    } catch (e) {
        console.warn("No se pudo invalidar sesión en backend:", e);
    }

    window.location.href = "/";
}