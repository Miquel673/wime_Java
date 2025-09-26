<?php
session_start();
if (!isset($_SESSION["id_usuario"])) {
    header("Location: /Wime/public/HTML/Wime_interfaz_Inicio-Sesion.html");
    exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
    die("Error de conexión: " . $conn->connect_error);
}

$id_usuario = $_SESSION["id_usuario"];

if (isset($_POST["accion"])) {
    if ($_POST["accion"] === "marcar_leidas") {
        $sql = "UPDATE notificaciones SET leida = 1 WHERE id_usuario = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("i", $id_usuario);
        $stmt->execute();
    }

    if ($_POST["accion"] === "eliminar_todas") {
        $sql = "DELETE FROM notificaciones WHERE id_usuario = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("i", $id_usuario);
        $stmt->execute();
    }

    header("Location: /Wime/private/PhP/Wime_interfaz_BandejaEntrada.php");
    exit;
}
?>
