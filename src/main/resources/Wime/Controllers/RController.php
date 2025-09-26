<?php
session_start();
header("Content-Type: application/json");

// Verifica si el usuario ha iniciado sesión
if (!isset($_SESSION["id_usuario"])) {
  echo json_encode(["success" => false, "message" => "❌ Sesión no iniciada"]);
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  echo json_encode(["success" => false, "message" => "❌ Error de conexión"]);
  exit;
}

// ID del usuario que crea la rutina
$id_usuario = $_SESSION["id_usuario"];

// Captura de datos del formulario
$titulo = trim($_POST["titulo"] ?? '');
$prioridad = $_POST["prioridad"] ?? '';
$fecha_asignacion = $_POST["fecha_Asignacion"] ?? '';
$fecha_limite = $_POST["fecha_limite"] ?? '';
$frecuencia = $_POST["Frecuencia"] ?? '';
$descripcion = trim($_POST["descripcion"] ?? '');
$estado = "pendiente"; // Estado por defecto
$compartir_con = trim($_POST["compartir_con"] ?? '');



// Validación básica
if (empty($titulo) || empty($prioridad) || empty($fecha_asignacion) || empty($fecha_limite) || empty($frecuencia)) {
  echo json_encode(["success" => false, "message" => "⚠️ Faltan campos obligatorios"]);
  exit;
}

// Preparar consulta
$sql = "INSERT INTO rutinas (IDusuarios, NombreRutina, Prioridad, FechaAsignacion, FechaFin, Frecuencia, Descripcion, Estado, Compartir_Con)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

$stmt = $conn->prepare($sql);
$stmt->bind_param("issssssss", $id_usuario, $titulo, $prioridad, $fecha_asignacion, $fecha_limite, $frecuencia, $descripcion, $estado, $compartir_con);

if ($stmt->execute()) {
  echo json_encode(["success" => true, "message" => "✅ Rutina creada con éxito"]);
} else {
  echo json_encode(["success" => false, "message" => "❌ Error al guardar rutina"]);
}

$mensaje = "Se ha creado una nueva rutina: " . $NombreRutina;
$sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'rutina', ?)";
$stmtNotif = $conn->prepare($sqlNotif);
$stmtNotif->bind_param("is", $id_usuario, $mensaje);
$stmtNotif->execute();


$stmt->close();
$conn->close();
?>
