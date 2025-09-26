<?php
session_start();
if (!isset($_SESSION["usuario"])) {
  header("Location: /Wime/public/HTML/Wime_interfaz_Inicio-Sesion.html");
  exit;
}
?>



<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>WIME | Mi Perfil</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Bootstrap -->
<link rel="stylesheet" href="/Wime/public/bootstrap-5.3.7-dist/css/bootstrap.min.css">

  <!-- Ícono -->
  <link rel="icon" type="image/png" href="Wime/public/IMG/Logo_Wime.png">

  <!-- Estilos propios -->
  <link rel="stylesheet" href="/Wime/public/Css/Wime_SideBar.css">
  <link rel="stylesheet" href="/Wime/public/Css/wime_interfaz_cuenta.css">

  <!-- Íconos Bootstrap -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>

<body>
    <!-- Botón hamburguesa (solo móviles) -->
  <button class="toggle-sidebar d-md-none" onclick="toggleSidebar()">
    <i class="bi bi-list"></i>
  </button>

  <div id="sidebar-container"></div>


  <script>
  fetch('/Wime/Interfaces/Admin/html/Wime_SideBar.html')
  .then(res => res.text())
  .then(html => {
    document.getElementById("sidebar-container").innerHTML = html;
    if (typeof window.inicializarCalendario === "function") {
      window.inicializarCalendario();
    }
  });

  </script>

  <!-- Contenido principal -->
    <main class="main-content">

      <div class="d-flex justify-content-between mb-3">
  <span></span>
  <div id="fecha-actual">📅 Cargando Fecha...</div>
</div>


    <div class="d-flex align-items-center gap-3 mb-4">
      <img src="/Wime/public/IMG/vector-de-perfil-avatar-predeterminado-foto-usuario-medios-sociales-icono-183042379.jpeg" alt="Avatar" class="rounded-circle bg-secondary" style="width: 60px; height: 60px;">
      <h1 class="fs-4 text-primary-emphasis">
        <?php echo "Bienvenido, " .htmlspecialchars($_SESSION["usuario"]); ?>
      </h1>

    </div>

    <div class="d-flex flex-wrap gap-3">
      <div class="bg-white border p-3 rounded shadow-sm" style="width: 300px;">
        <button class="btn btn-outline-primary w-100 mb-2"><a href="/Wime/Interfaces/Admin/php/Wime_interfaz_Tablero.php">Mi tablero</a></button>
        <button class="btn btn-primary w-100 my-2" data-bs-toggle="modal" data-bs-target="#modalNuevo">
          ➕
        </button>
  <div id="calendario-Cuenta">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <button id="prev" class="btn btn-sm btn-outline-primary">◀</button>
        <h5 id="mes-anio" class="m-0"></h5>
        <button id="next" class="btn btn-sm btn-outline-primary">▶</button>
      </div>
      <div id="dias-semana">
        <div>D</div><div>L</div><div>M</div><div>M</div><div>J</div><div>V</div><div>S</div>
      </div>
      <div id="dias"></div>
  </div>



      </div>

      <div class="flex-grow-1 bg-white border p-3 rounded shadow-sm">
        <h5 class="mb-3">Productividad:</h5>
        <ul>
          <li> Tareas Realizadas: <strong id="tareas-completadas">0</strong></li>
          <li> Rutinas Finalizadas: <strong id="rutinas-finalizadas">0</strong></li>
          <li> En proceso: <strong id="en-proceso">0</strong></li>
        </ul>

        <div class="card shadow-sm my-4">
  <div class="card-header bg-primary text-white">
    <h5 class="mb-0">📊 Resumen de Actividades</h5>
  </div>
  <div class="card-body">
    <canvas id="grafico-estadisticas" height="70"></canvas>
  </div>
</div>




        <div>
          <button class="btn btn-success me-2">📈 Progreso</button>
          <button class="btn btn-secondary">📋</button>
        </div>
        
      </div>
    </div>
  </main>

  <!-- Modal para crear nueva tarea o rutina -->
<div class="modal fade" id="modalNuevo" tabindex="-1" aria-labelledby="modalNuevoLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalNuevoLabel">¿Qué deseas crear?</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body d-flex flex-column gap-2">

        <!-- dentro del modal -->
        <a href="/Wime/public/HTML/Wime_interfaz_Modulo_CTareas.html" class="btn btn-primary">Crear Tarea</a>
        <a href="/Wime/public/HTML/Wime_interfaz_Modulo_CRutinas.html" class="btn btn-success">Crear Rutina</a>

      </div>
    </div>
  </div>
</div>


  <!-- Scripts -->
<!-- Calendario -->



<script>
  document.addEventListener("DOMContentLoaded", () => {
    const hoy = new Date();
    const dia = String(hoy.getDate()).padStart(2, '0');
    const mes = String(hoy.getMonth() + 1).padStart(2, '0'); // Meses comienzan en 0
    const anio = hoy.getFullYear();

    const fechaFormateada = `📅 ${dia}/${mes}/${anio}`;
    const contenedorFecha = document.getElementById("fecha-actual");
    if (contenedorFecha) {
      contenedorFecha.textContent = fechaFormateada;
    }
  });
</script>

<script>
    let fechaActual = new Date();
    const mesAnio = document.getElementById("mes-anio");
    const diasContainer = document.getElementById("dias");

    function renderizarCalendario() {
      diasContainer.innerHTML = "";

      const year = fechaActual.getFullYear();
      const month = fechaActual.getMonth();
      const primerDia = new Date(year, month, 1).getDay();
      const totalDias = new Date(year, month + 1, 0).getDate();

      mesAnio.textContent = fechaActual.toLocaleDateString("es-ES", {
        month: "long",
        year: "numeric"
      });

      for (let i = 0; i < primerDia; i++) {
        diasContainer.innerHTML += `<div></div>`;
      }

      for (let d = 1; d <= totalDias; d++) {
        const dia = document.createElement("div");
        dia.classList.add("dia");
        dia.textContent = d;

        const hoy = new Date();
        if (d === hoy.getDate() && month === hoy.getMonth() && year === hoy.getFullYear()) {
          dia.classList.add("hoy");
        }

        diasContainer.appendChild(dia);
      }
    }

    document.getElementById("prev").addEventListener("click", () => {
      fechaActual.setMonth(fechaActual.getMonth() - 1);
      renderizarCalendario();
    });

    document.getElementById("next").addEventListener("click", () => {
      fechaActual.setMonth(fechaActual.getMonth() + 1);
      renderizarCalendario();
    });

    renderizarCalendario();
  </script>

<script src="/Wime/public/Js/calendario.js"></script>
<script src="/Wime/public/Js/Estadisticas.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/Wime/public/bootstrap-5.3.7-dist/js/bootstrap.bundle.min.js"></script>



</body>



</html>
