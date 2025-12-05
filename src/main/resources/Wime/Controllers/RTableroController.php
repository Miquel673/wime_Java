<?php
session_start();
header('Content-Type: application/json');

if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "❌ Sesión no iniciada"]);
  exit;
}

$id_usuario = $_SESSION["id_usuario"];

// Conexión a la base de datos
$conn = new mysqli("localhost", "root", "", "Wime");

if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "❌ Error de conexión"]);
  exit;
}

// Consulta las rutinas del usuario actual
$sql = "SELECT * FROM rutinas WHERE IDusuarios = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $id_usuario);
$stmt->execute();
$result = $stmt->get_result();

$rutinas = [];
while ($fila = $result->fetch_assoc()) {
  $rutinas[] = $fila;
}

echo json_encode($rutinas);

$stmt->close();
$conn->close();
?>
