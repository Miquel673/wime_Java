<?php
ob_start(); // Activa buffer de salida

include __DIR__ . "/conexion.php"; // Asegúrate de que esta ruta sea correcta

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $email = trim($_POST["EmailUsuario"] ?? '');
    $nombre = trim($_POST["NombreUsuario"] ?? '');
    $contrasena = $_POST["ContrasenaUsuario"] ?? '';
    $confirm = $_POST["confirm_password"] ?? '';
    $nacimiento = $_POST["Birth_Day"] ?? '';

    // Validaciones básicas
    if (empty($email) || empty($nombre) || empty($contrasena) || empty($confirm) || empty($nacimiento)) {
        echo "<script>alert('⚠️ Todos los campos son obligatorios.'); window.history.back();</script>";
        exit;
    }

    if ($contrasena !== $confirm) {
        echo "<script>alert('❌ Las contraseñas no coinciden.'); window.history.back();</script>";
        exit;
    }

    // Calcular edad
    try {
        $nacimiento_date = new DateTime($nacimiento);
        $hoy = new DateTime();
        $edad = $hoy->diff($nacimiento_date)->y;
    } catch (Exception $e) {
        echo "<script>alert('❌ Fecha inválida.'); window.history.back();</script>";
        exit;
    }

    // Verificar si el correo ya está registrado
    $verificar = $conn->prepare("SELECT IDusuario FROM usuario WHERE EmailUsuario = ?");
    $verificar->bind_param("s", $email);
    $verificar->execute();
    $verificar->store_result();

    if ($verificar->num_rows > 0) {
        echo "<script>
                alert('❌ Este correo ya está registrado. ¿Deseas iniciar sesión?');
                window.location.href='/Wime/public/HTML/Wime_interfaz_inicio_Sesion.html';
              </script>";
        exit;
    }
    $verificar->close();

    // Encriptar contraseña y registrar
    $hash = password_hash($contrasena, PASSWORD_DEFAULT);
    $fecha = date("Y-m-d H:i:s");
    $tipo = "Administrador"; // Valor fijo para la interfaz admin

    $sql = "INSERT INTO usuario (NombreUsuario, EmailUsuario, ContrasenaUsuario, Edad, FechaRegistro, Tipo)
            VALUES (?, ?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sssiss", $nombre, $email, $hash, $edad, $fecha, $tipo);

    if ($stmt->execute()) {
        echo "<script>alert('✅ Registro exitoso.'); window.location.href='/Wime/Interfaces/Admin/HTML/Wime_interfaz_inicio-Sesion.html';</script>";
    } else {
        echo "<script>alert('❌ Error al registrar: " . $stmt->error . "'); window.history.back();</script>";
    }

    $stmt->close();
    $conn->close();
}

ob_end_flush(); // Limpia el buffer
?>
