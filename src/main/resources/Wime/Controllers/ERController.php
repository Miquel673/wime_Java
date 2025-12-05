<?php
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
$idRutina = $_POST["id"] ?? null;

if (!$idRutina) {
  echo json_encode(["success" => false, "message" => "⚠️ Falta ID de la rutina"]);
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "❌ Error de conexión"]);
  exit;
}

$sql = "DELETE FROM rutinas WHERE IDRutina = ? AND IDusuarios = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ii", $idRutina, $id_usuario);

if ($stmt->execute()) {
  echo json_encode(["success" => true, "message" => "✅ Rutina eliminada"]);
} else {
  echo json_encode(["success" => false, "message" => "❌ No se pudo eliminar"]);
}

$mensaje = "Se ha eliminado una Rutina.";
$sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'rutina', ?)";
$stmtNotif = $conn->prepare($sqlNotif);
$stmtNotif->bind_param("is", $id_usuario, $mensaje);
$stmtNotif->execute();


$stmt->close();
$conn->close();
