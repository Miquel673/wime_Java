<?php
session_start();
header("Content-Type: application/json");

if (!isset($_SESSION["id_usuario"])) {
  echo json_encode([]);
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode([]);
  exit;
}

$id_usuario = $_SESSION["id_usuario"];
$sql = "SELECT * FROM tareas WHERE id_usuario = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $id_usuario);
$stmt->execute();
$result = $stmt->get_result();

$tareas = [];
while ($fila = $result->fetch_assoc()) {
  $tareas[] = $fila;
}

echo json_encode($tareas);

$stmt->close();
$conn->close();
