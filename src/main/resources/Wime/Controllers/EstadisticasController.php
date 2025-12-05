<?php
session_start();
header("Content-Type: application/json");

if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "⚠️ Sesión no iniciada"]);
  exit;
}

$usuarioID = $_SESSION["id_usuario"];

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "❌ Error de conexión"]);
  exit;
}

// Contar tareas completadas
$sql_tareas_completadas = "SELECT COUNT(*) AS total FROM tareas WHERE id_usuario = ? AND estado = 'completada'";
$stmt1 = $conn->prepare($sql_tareas_completadas);
$stmt1->bind_param("i", $usuarioID);
$stmt1->execute();
$res1 = $stmt1->get_result()->fetch_assoc();

// Contar rutinas finalizadas
$sql_rutinas_finalizadas = "SELECT COUNT(*) AS total FROM rutinas WHERE IDusuarios = ? AND Estado = 'completada'";
$stmt2 = $conn->prepare($sql_rutinas_finalizadas);
$stmt2->bind_param("i", $usuarioID);
$stmt2->execute();
$res2 = $stmt2->get_result()->fetch_assoc();

// Contar tareas y rutinas en proceso
$sql_en_proceso = "
  SELECT 
    (SELECT COUNT(*) FROM tareas WHERE id_usuario = ? AND estado = 'en progreso') +
    (SELECT COUNT(*) FROM rutinas WHERE IDusuarios = ? AND Estado = 'en progreso') AS total";
$stmt3 = $conn->prepare($sql_en_proceso);
$stmt3->bind_param("ii", $usuarioID, $usuarioID);
$stmt3->execute();
$res3 = $stmt3->get_result()->fetch_assoc();

// Respuesta
echo json_encode([
  "success" => true,
  "tareas_completadas" => $res1["total"],
  "rutinas_finalizadas" => $res2["total"],
  "en_proceso" => $res3["total"]
]);

$conn->close();
?>
