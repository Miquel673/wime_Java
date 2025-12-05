<!DOCTYPE html>
<html lang="es">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WIME</title>
    <link rel="stylesheet" href="/Wime/public/Css/Wime_interfaz_configuracion.css">
    <link rel="stylesheet" href="/Wime/public/bootstrap-5.3.7-dist/css/bootstrap.min.css">

    <link rel="icon" type="image/png" href="/public/IMG/Logo_Wime.png">

    </head>

    <body>
        
        <header>
            <div id="sidebar-container"></div>

            <script defer>
                // Cargar la barra lateral y conectar el evento después
                fetch('Wime_SideBar.html')
                .then(response => response.text())

                .then(html => {
                    document.getElementById('sidebar-container').innerHTML = html;

                    setTimeout(() => {
                    const bandejaBtn = document.getElementById('bandejaEntrada');
                    if (bandejaBtn) {
                    bandejaBtn.addEventListener('click', function (e) {
                    e.preventDefault();
                    mostrarNotificacion(); // ✅ llamada a la función del Script.js
                            });
                        }
                    }, 100);
                });

            </script>
        </header>

        <main class="settings">
        <div class="tabs">
            <button>General</button>
            <button>Cuenta</button>
            <button>Centro de Ayuda</button>
        </div>

        <div class="content">
            <section class="themes">
            <h3>Temas</h3>
            <div class="theme-options">
                <div class="theme light">Claro</div>
                <div class="theme dark">Oscuro</div>
            </div>
            </section>

            <section class="notifications">
            <h3>Notificaciones:</h3>
            <label><input type="radio" name="notif" checked> Activar</label>
            <label><input type="radio" name="notif"> Desactivar</label>
            </section>

            <section class="messages">
            <h3>Mensajes:</h3>
            <label><input type="radio" name="msg" checked> Activar</label>
            <label><input type="radio" name="msg"> Desactivar</label>
            </section>
        </div>

        <footer><a href="#">Políticas de Privacidad</a></footer>
        </main>
        <script src="/Wime/public/Js/Script.js"></script>
        <script src="/Wime/public/bootstrap-5.3.7-dist/js/bootstrap.bundle.min.js"></script>


    </body>
</html>