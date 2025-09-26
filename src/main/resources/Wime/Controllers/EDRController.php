<?php
session_start();
header("Content-Type: application/json");

if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "Sesión no iniciada"]);
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "Error de conexión"]);
  exit;
}

$id = $_POST["IDRutina"] ?? null;
$nombre = $_POST["NombreRutina"] ?? '';
$prioridad = $_POST["Prioridad"] ?? '';
$frecuencia = $_POST["Frecuencia"] ?? '';
$fecha_asignacion = $_POST["FechaAsignacion"] ?? '';
$fecha_fin = $_POST["FechaFin"] ?? '';
$descripcion = $_POST["Descripcion"] ?? '';
$estado = $_POST["Estado"] ?? '';

if (!$id || !$nombre || !$prioridad || !$fecha_asignacion || !$fecha_fin) {
  echo json_encode(["success" => false, "message" => "Faltan campos obligatorios"]);
  exit;
}

$sql = "UPDATE rutinas SET NombreRutina = ?, Prioridad = ?, Frecuencia = ?, FechaAsignacion = ?, FechaFin = ?, Descripcion = ?, Estado = ? WHERE IDRutina = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("sssssssi", $nombre, $prioridad, $frecuencia, $fecha_asignacion, $fecha_fin, $descripcion, $estado, $id);

echo $stmt->execute()
  ? json_encode(["success" => true])
  : json_encode(["success" => false, "message" => "Error al actualizar"]);

  $mensaje = "Se ha editado la Rutina: " . $NombreRutina;
$sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'rutina', ?)";
$stmtNotif = $conn->prepare($sqlNotif);
$stmtNotif->bind_param("is", $id_usuario, $mensaje);
$stmtNotif->execute();


$stmt->close();
$conn->close();
