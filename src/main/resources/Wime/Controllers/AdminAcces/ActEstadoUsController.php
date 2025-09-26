<?php
session_start();

if (!isset($_SESSION["id_usuario"]) || $_SESSION["tipo"] !== "Administrador") {
    header("Location: /Wime/Interfaces/Admin/HTML/Wime_interfaz_Inicio-Sesion.html");
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST" && isset($_POST["id_usuario"], $_POST["estado_actual"])) {
    $id = $_POST["id_usuario"];
    $estadoActual = $_POST["estado_actual"];
    $nuevoEstado = ($estadoActual === "Activo") ? "Inactivo" : "Activo";

    $conn = new mysqli("localhost", "root", "", "Wime");
    if ($conn->connect_error) {
        die("❌ Error de conexión: " . $conn->connect_error);
    }

    $stmt = $conn->prepare("UPDATE usuario SET Estado = ? WHERE IDusuario = ?");
    $stmt->bind_param("si", $nuevoEstado, $id);

    if ($stmt->execute()) {
        header("Location: /Wime/Interfaces/Admin/php/Wime_interfaz_AdministrarUsuarios.php");
        exit;
    } else {
        echo "❌ Error al actualizar estado.";
    }

    $stmt->close();
    $conn->close();
}
?>
