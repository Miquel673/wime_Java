<?php
session_start();
header("Content-Type: application/json");

if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "Sesión no iniciada"]);
  exit;
}

$id_usuario = $_SESSION["id_usuario"];
$id_tarea = $_POST["id_tarea"] ?? null;

if (!$id_tarea) {
  echo json_encode(["success" => false, "message" => "ID de tarea no recibido"]);
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "Error de conexión"]);
  exit;
}

$sql = "DELETE FROM tareas WHERE IDtarea = ? AND id_usuario = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ii", $id_tarea, $id_usuario);

if ($stmt->execute()) {
  echo json_encode(["success" => true]);
} else {
  echo json_encode(["success" => false, "message" => "Error al eliminar"]);
}

$mensaje = "Se ha eliminado una tarea.";
$sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'tarea', ?)";
$stmtNotif = $conn->prepare($sqlNotif);
$stmtNotif->bind_param("is", $id_usuario, $mensaje);
$stmtNotif->execute();


$stmt->close();
$conn->close();
