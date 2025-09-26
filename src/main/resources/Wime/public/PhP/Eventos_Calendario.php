<?php
session_start();
$idUsuario = $_SESSION['id_usuario'];

require 'conexion.php';

$eventos = [];

// TAREAS
$tareas = $conn->prepare("SELECT TituloTarea AS titulo, Fecha_Limite AS fecha FROM tareas WHERE IDusuarios = ?");
$tareas->execute([$idUsuario]);

while ($fila = $tareas->fetch(PDO::FETCH_ASSOC)) {
    $eventos[] = [
        'title' => '📝 ' . $fila['titulo'],
        'start' => $fila['fecha'],
        'color' => '#2E86C1' // azul
    ];
}

// RUTINAS
$rutinas = $conn->prepare("SELECT NombreRutina AS titulo, FechaFin AS fecha FROM rutinas WHERE IDusuarios = ?");
$rutinas->execute([$idUsuario]);

while ($fila = $rutinas->fetch(PDO::FETCH_ASSOC)) {
    $eventos[] = [
        'title' => '🔁 ' . $fila['titulo'],
        'start' => $fila['fecha'],
        'color' => '#28B463' // verde
    ];
}

echo json_encode($eventos);
?>
