<?php
session_start();
session_unset();     // Elimina todas las variables de sesión
session_destroy();   // Destruye la sesión activa

// Redirige al inicio de sesión (o a tu página principal si prefieres)
header("Location: /Wime/public/HTML/Wime_interfaz_Inicio-Sesion.html");
exit;
?>
