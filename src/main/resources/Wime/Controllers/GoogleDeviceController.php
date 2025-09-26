<?php


// Conexión a la base de datos
$host = "localhost";
$usuario = "root";
$clave = "";
$bd = "Wime";
$conn = new mysqli($host, $usuario, $clave, $bd);

if ($conn->connect_error) {
  echo json_encode(['success' => false, 'message' => 'Error de conexión']);
  exit;
}

// Leer el token de Google
$data = json_decode(file_get_contents("php://input"));
$token = $data->token ?? '';

if (!$token) {
  echo json_encode(['success' => false, 'message' => 'Token no proporcionado']);
  exit;
}

// Verificar el token con Google
$api_url = "https://oauth2.googleapis.com/tokeninfo?id_token=" . $token;
$response = file_get_contents($api_url);
$google_data = json_decode($response);

if (!$google_data || isset($google_data->error_description)) {
  echo json_encode(['success' => false, 'message' => 'Token inválido']);
  exit;
}

$email = $google_data->email;
$nombre = $google_data->name;

// Verificar si el usuario ya existe
$sql = "SELECT NombreUsuario FROM usuario WHERE EmailUsuario = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
  // Usuario ya existe
  $_SESSION["usuario"] = $result->fetch_assoc()["NombreUsuario"];
} else {
  // Crear nuevo usuario
  $sql_insert = "INSERT INTO usuario (EmailUsuario, NombreUsuario, ContrasenaUsuario) VALUES (?, ?, '')";
  $stmt_insert = $conn->prepare($sql_insert);
  $stmt_insert->bind_param("ss", $email, $nombre);
  $stmt_insert->execute();
  $_SESSION["usuario"] = $nombre;
  $stmt_insert->close();
}

$stmt->close();
$conn->close();

echo json_encode(['success' => true]);

header("Content-Type: application/json");

// Recibir el token desde el frontend
$data = json_decode(file_get_contents("php://input"), true);
$token = $data['token'] ?? '';

if (!$token) {
    echo json_encode(['success' => false, 'message' => '❌ Token no recibido']);
    exit;
}

// Validar el token con Google (sin SDK)
$verificacion = file_get_contents("https://oauth2.googleapis.com/tokeninfo?id_token=" . $token);

if ($verificacion === false) {
    echo json_encode(['success' => false, 'message' => '❌ No se pudo verificar el token']);
    exit;
}

$info = json_decode($verificacion, true);

// Verificar que sea válido y coincida el client_id
$CLIENT_ID = "722346440847-9e8nr5sn87tv3bmr7j557d696du8ub9r.apps.googleusercontent.com"; // Reemplaza esto por el mismo client_id que usaste en el HTML

if ($info && isset($info['aud']) && $info['aud'] === $CLIENT_ID) {
    // Token válido
    $nombre = $info['name'];
    $email = $info['email'];

    // Si quieres: guardar o verificar en tu base de datos

    echo json_encode([
        'success' => true,
        'nombre' => $nombre,
        'email' => $email
    ]);
} else {
    echo json_encode(['success' => false, 'message' => '❌ Token inválido o client_id no coincide']);
}

session_start();
header("Content-Type: application/json");

