import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';



//----------------------Barra Lateral---------------------//

// Cargar HTML de la barra lateral
fetch('/Wime/public/HTML/sidebar.html')
  .then(res => res.text())
  .then(html => {
    document.getElementById('sidebar-container').innerHTML = html;

    // Agregar clase al body para mostrar la barra lateral si ya estaba abierta
    const sidebar = document.querySelector('.sidebar');
    if (sidebar && document.body.classList.contains('sidebar-visible')) {
      sidebar.classList.add('sidebar-visible');
    }
  });

// Función para mostrar u ocultar la barra lateral
function toggleSidebar() {
  const sidebar = document.querySelector('.sidebar');
  sidebar.classList.toggle('sidebar-visible');
  document.body.classList.toggle('sidebar-visible');
}

//--------------Calcular edad _ Registro de Usuario---------------------------//

function calcularEdad() {
    let birthDate = document.getElementById("Birth_Day").value;
    let edadInput = document.getElementById("Edad");

    if (birthDate) {
        let today = new Date();
        let birth = new Date(birthDate);
        let age = today.getFullYear() - birth.getFullYear();
        let monthDiff = today.getMonth() - birth.getMonth();

        // Ajustar la edad si aún no ha cumplido años este año
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
            age--;
        }

        edadInput.value = age;
    } else {
        edadInput.value = "";
    }
}



// Evitar múltiples asignaciones si lo cargas en varias páginas
if (!window.sidebarYaCargado) {
  window.sidebarYaCargado = true;

  function mostrarNotificacion() {
    if (!("Notification" in window)) {
      alert("Tu navegador no soporta notificaciones.");
    } else if (Notification.permission === "granted") {
      new Notification("WIME", {
        body: "Tienes una nueva notificación pendiente.",
        icon: "/IMG/Logo_Wime.png"
      });
    } else if (Notification.permission !== "denied") {
      Notification.requestPermission().then(function (permission) {
        if (permission === "granted") {
          new Notification("WIME", {
            body: "Tienes una nueva notificación pendiente.",
            icon: "/IMG/Logo_Wime.png"
          });
        }
      });
    }
  }

  function conectarBandeja() {
    const boton = document.getElementById("bandejaEntrada");
    if (boton) {
      boton.addEventListener("click", function (e) {
        e.preventDefault();
        mostrarNotificacion();
      });
    } else {
      // Volver a intentar en un rato si el botón no ha sido insertado aún
      setTimeout(conectarBandeja, 200);
    }
  }

  // Ejecutar después de que cargue el HTML principal
  document.addEventListener("DOMContentLoaded", conectarBandeja);
}


//---------Tema Claro/Oscuro------------//

document.addEventListener('DOMContentLoaded', () => {
  const lightThemeBtn = document.querySelector('.theme.light');
  const darkThemeBtn = document.querySelector('.theme.dark');
  const body = document.body;
  const settings = document.querySelector('.settings');
  const sidebar = document.querySelector('.sidebar');
  
  lightThemeBtn.addEventListener('click', () => {
  body.style.backgroundColor = '#f4f4f4';
  settings.style.backgroundColor = 'white';
  sidebar.style.backgroundColor = '#e0e0e0';
  body.style.color = 'black';
  });

  darkThemeBtn.addEventListener('click', () => {
  body.style.backgroundColor = '#1e1e1e';
  settings.style.backgroundColor = '#2e2e2e';
  sidebar.style.backgroundColor = '#333333';
  body.style.color = 'white';
    });
});

