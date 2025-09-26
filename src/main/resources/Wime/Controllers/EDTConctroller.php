<?php
session_start();
header("Content-Type: application/json");

if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "⚠️ Sesión no iniciada"]);
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "❌ Error de conexión"]);
  exit;
}

$id_usuario = $_SESSION["id_usuario"];
$id_tarea = $_POST["id"] ?? null;
$titulo = trim($_POST["titulo"] ?? '');
$prioridad = $_POST["prioridad"] ?? '';
$fecha_limite = $_POST["fecha_limite"] ?? null;
$descripcion = trim($_POST["descripcion"] ?? '');
$estado = $_POST["estado"] ?? "pendiente";

if (!$id_tarea || empty($titulo) || empty($prioridad) || empty($fecha_limite)) {
  echo json_encode(["success" => false, "message" => "⚠️ Faltan campos obligatorios"]);
  exit;
}

$sql = "UPDATE tareas SET titulo = ?, prioridad = ?, fecha_limite = ?, descripcion = ?, estado = ?
        WHERE IDtarea = ? AND id_usuario = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ssssssi", $titulo, $prioridad, $fecha_limite, $descripcion, $estado, $id_tarea, $id_usuario);

if ($stmt->execute()) {
  echo json_encode(["success" => true]);
} else {
  echo json_encode(["success" => false, "message" => "❌ Error al actualizar la tarea"]);
}

$mensaje = "Se ha editado la tarea: " . $titulo;
$sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'tarea', ?)";
$stmtNotif = $conn->prepare($sqlNotif);
$stmtNotif->bind_param("is", $id_usuario, $mensaje);
$stmtNotif->execute();


$stmt->close();
$conn->close();
