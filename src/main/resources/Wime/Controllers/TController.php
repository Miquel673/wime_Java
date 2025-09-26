<?php
session_start();
header("Content-Type: application/json");

if (!isset($_SESSION["id_usuario"])) {
    echo json_encode(["success" => false, "message" => "❌ Sesión no iniciada"]);
    exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "❌ Error de conexión"]);
    exit;
}

$id_usuario = $_SESSION["id_usuario"];
$titulo = trim($_POST["titulo"] ?? '');
$prioridad = $_POST["prioridad"] ?? '';
$fecha_limite = $_POST["fecha_limite"] ?? null;
$descripcion = trim($_POST["descripcion"] ?? '');
$estado = "pendiente";


// Validación
if (empty($titulo) || empty($prioridad) || empty($fecha_limite)) {
    echo json_encode(["success" => false, "message" => "⚠️ Faltan campos obligatorios"]);
    exit;
}

$sql = "INSERT INTO tareas (id_usuario, titulo, prioridad, fecha_limite, descripcion, estado)
        VALUES (?, ?, ?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("isssss", $id_usuario, $titulo, $prioridad, $fecha_limite, $descripcion, $estado);

if ($stmt->execute()) {
    echo json_encode(["success" => true, "message" => "✅ Tarea creada con éxito"]);
} else {
    echo json_encode(["success" => false, "message" => "❌ Error al guardar"]);
}

// Después de insertar la tarea exitosamente
$mensaje = "Se ha creado una nueva tarea: " . $titulo;
$sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'tarea', ?)";
$stmtNotif = $conn->prepare($sqlNotif);
$stmtNotif->bind_param("is", $id_usuario, $mensaje);
$stmtNotif->execute();



$stmt->close();
$conn->close();