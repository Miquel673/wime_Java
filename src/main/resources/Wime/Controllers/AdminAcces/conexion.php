<?php
$host = "localhost";
$usuario = "root";
$clave = "";
$bd = "wime";

// Crear la conexión
$conn = new mysqli($host, $usuario, $clave, $bd);
if ($conn->connect_error) {
    die("Error de conexión: " . $conn->connect_error);
}
$conn->set_charset("utf8");
?>
