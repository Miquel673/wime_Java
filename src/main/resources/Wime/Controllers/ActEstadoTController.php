<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

session_start();
header("Content-Type: application/json");

if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "❌ Sesión no iniciada"]);
  exit;
}

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
  echo json_encode(["success" => false, "message" => "❌ Método no permitido"]);
  exit;
}

$id_usuario = $_SESSION["id_usuario"];
$idTarea = $_POST["id"] ?? null;
$nuevoEstado = $_POST["estado"] ?? '';

if (!$idTarea || !$nuevoEstado) {
  echo json_encode(["success" => false, "message" => "⚠️ Faltan datos"]);
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "❌ Error de conexión"]);
  exit;
}

//Actualizacion de estado

$sql = "UPDATE tareas SET Estado = ? WHERE IDTarea = ? AND id_usuario = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("sii", $nuevoEstado, $idTarea, $id_usuario);

if ($stmt->execute()) {
// Guardar notificación
$mensaje = "Se ha cambiado el estado de una Tarea.";
$sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'tarea', ?)";
$stmtNotif = $conn->prepare($sqlNotif);
$stmtNotif->bind_param("is", $id_usuario, $mensaje);
$stmtNotif->execute();

  echo json_encode(["success" => true, "message" => "✅ Estado de la tarea actualizado"]);
} else {
  echo json_encode(["success" => false, "message" => "❌ Error al actualizar tarea"]);
}

$stmt->close();
$conn->close();