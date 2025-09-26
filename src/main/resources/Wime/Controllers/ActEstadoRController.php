<?php
session_start();
header("Content-Type: application/json");

// Validar sesión
if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "❌ Sesión no iniciada"]);
  exit;
}

// Validar método
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
  echo json_encode(["success" => false, "message" => "❌ Método no permitido"]);
  exit;
}

// Obtener datos
$id_usuario = $_SESSION["id_usuario"];
$idTarea = isset($_POST["id"]) ? intval($_POST["id"]) : null;
$nuevoEstado = $_POST["estado"] ?? null;

// Validar datos
if (!$idTarea || !$nuevoEstado) {
  echo json_encode(["success" => false, "message" => "⚠️ Faltan datos"]);
  exit;
}

// Conexión a la base de datos
$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "❌ Error de conexión"]);
  exit;
}

// Actualizar estado de la tarea
$sql = "UPDATE tareas SET Estado = ? WHERE IDTarea = ? AND IDusuarios = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("sii", $nuevoEstado, $idTarea, $id_usuario);

if ($stmt->execute()) {
  // Notificación
  $mensaje = "Se ha cambiado el estado de una Tarea.";
  $sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'tarea', ?)";
  $stmtNotif = $conn->prepare($sqlNotif);
  $stmtNotif->bind_param("is", $id_usuario, $mensaje);
  $stmtNotif->execute();
  $stmtNotif->close();

  echo json_encode(["success" => true, "message" => "✅ Estado de la tarea actualizado"]);
} else {
  echo json_encode(["success" => false, "message" => "❌ Error al actualizar la tarea"]);
}

$stmt->close();
$conn->close();
?>
