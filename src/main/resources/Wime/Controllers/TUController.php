<?php
session_start();
header("Content-Type: application/json");

if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "Sesión no iniciada"]);
  exit;
}

$id_usuario = $_SESSION["id_usuario"];
$id = $_GET["id"] ?? null;

if (!$id) {
  echo json_encode(["success" => false, "message" => "ID no proporcionado"]);
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "Error de conexión"]);
  exit;
}

$sql = "SELECT * FROM tareas WHERE IDtarea = ? AND id_usuario = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ii", $id, $id_usuario);
$stmt->execute();
$result = $stmt->get_result();

if ($tarea = $result->fetch_assoc()) {
  echo json_encode(["success" => true, "tarea" => $tarea]);
} else {
  echo json_encode(["success" => false, "message" => "Tarea no encontrada"]);
}

$stmt->close();
$conn->close();
