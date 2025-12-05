<?php
session_start();
if (!isset($_SESSION["tipo"]) || $_SESSION["tipo"] !== "Administrador") {
    echo "⛔ Acceso denegado";
    exit;
}

if ($_SERVER["REQUEST_METHOD"] === "POST" && isset($_POST["id_usuario"])) {
    $id = intval($_POST["id_usuario"]);

    $conn = new mysqli("localhost", "root", "", "Wime");
    if ($conn->connect_error) {
        die("❌ Error de conexión: " . $conn->connect_error);
    }

    $stmt = $conn->prepare("DELETE FROM usuario WHERE IDusuario = ?");
    $stmt->bind_param("i", $id);

    if ($stmt->execute()) {
        header("Location: /Wime/Interfaces/Admin/php/Wime_interfaz_AdministrarUsuarios.php");
        exit;
    } else {
        echo "❌ Error al eliminar: " . $stmt->error;
    }

    $stmt->close();
    $conn->close();
} else {
    echo "❗ Petición no válida.";
}

//Cambio de estado

// --- Cambiar estado del usuario ---
$sql = "UPDATE usuario SET Estado = 'Inactivo' WHERE IDusuario = ?";
$stmt = $conn->prepare($sql);

if ($stmt) {
    $stmt->bind_param("i", $idUsuario);
    $stmt->execute();

    if ($stmt->affected_rows > 0) {
        echo json_encode(['success' => true, 'message' => '✅ Usuario inactivado exitosamente.']);
    } else {
        echo json_encode(['success' => false, 'message' => '⚠️ No se encontró el usuario o ya estaba inactivo.']);
    }

    $stmt->close();
} else {
    echo json_encode(['success' => false, 'message' => '❌ Error preparando la consulta.']);
}

$conn->close();