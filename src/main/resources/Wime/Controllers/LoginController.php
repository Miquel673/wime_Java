<?php
session_start();
header("Content-Type: application/json");

// --- Conexión a base de datos ---
$host = "localhost";
$usuario = "root";
$clave = "";
$bd = "Wime";

$conn = new mysqli($host, $usuario, $clave, $bd);

// Mostrar errores durante desarrollo
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// --- Verificar conexión ---
if ($conn->connect_error) {
    echo json_encode(['success' => false, 'message' => '❌ Error de conexión a la base de datos.']);
    exit;
}

// --- Captura de datos POST ---
$email = trim($_POST["email"] ?? '');
$contrasena = $_POST["contrasena"] ?? '';

// --- Validación ---
if (empty($email) || empty($contrasena)) {
    echo json_encode(['success' => false, 'message' => '⚠️ Todos los campos son obligatorios.']);
    exit;
}

// --- Consulta del usuario ---
$sql = "SELECT IDusuario, NombreUsuario, ContrasenaUsuario, Tipo, ultimo_login, Estado FROM usuario WHERE EmailUsuario = ?";
$stmt = $conn->prepare($sql);

if (!$stmt) {
    echo json_encode(['success' => false, 'message' => '❌ Error preparando la consulta.']);
    exit;
}

$stmt->bind_param("s", $email);
$stmt->execute();
$resultado = $stmt->get_result();

if ($resultado->num_rows === 1) {
    $usuario = $resultado->fetch_assoc();

    // Verificar contraseña
    if (password_verify($contrasena, $usuario["ContrasenaUsuario"])) {

        // 🛑 Verificar estado actual
        if ($usuario["Estado"] !== "Activo") {
            echo json_encode([
                'success' => false,
                'reason' => 'Inactivo',
                'message' => 'Tu cuenta está inactiva. Contacta con soporte.'
            ]);
            exit;
        }

        // 🕒 Verificar si ha pasado más de 30 días desde el último login
        if (!empty($usuario["ultimo_login"])) {
            $ultimoLogin = strtotime($usuario["ultimo_login"]);
            $ahora = time();
            $diasInactivo = ($ahora - $ultimoLogin) / (60 * 60 * 24); // días

            if ($diasInactivo > 60) {
                // Cambiar estado a inactivo
                $sqlEstado = "UPDATE usuario SET Estado = 'Inactivo' WHERE IDusuario = ?";
                $stmtEstado = $conn->prepare($sqlEstado);
                $stmtEstado->bind_param("i", $usuario["IDusuario"]);
                $stmtEstado->execute();
                $stmtEstado->close();

                echo json_encode([
                    'success' => false,
                    'reason' => 'Inactivo',
                    'message' => '❌ Tu cuenta ha sido desactivada por inactividad.'
                ]);
                exit;
            }
        }

        // ✅ Iniciar sesión
        $_SESSION["usuario"] = $usuario["NombreUsuario"];
        $_SESSION["id_usuario"] = $usuario["IDusuario"];
        $_SESSION["tipo"] = $usuario["Tipo"];

        // 🔄 Actualizar fecha de último login
        $sqlUpdate = "UPDATE usuario SET ultimo_login = NOW() WHERE IDusuario = ?";
        $stmtUpdate = $conn->prepare($sqlUpdate);
        if ($stmtUpdate) {
            $stmtUpdate->bind_param("i", $usuario["IDusuario"]);
            $stmtUpdate->execute();
            $stmtUpdate->close();
        }

        echo json_encode(['success' => true]);
    } else {
        echo json_encode(['success' => false, 'message' => '❌ Contraseña incorrecta.']);
    }
} else {
    echo json_encode(['success' => false, 'message' => '❌ Usuario no encontrado.']);
}

$stmt->close();
$conn->close();
?>
