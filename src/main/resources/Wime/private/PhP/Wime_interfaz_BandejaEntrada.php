<?php
session_start();
if (!isset($_SESSION["id_usuario"])) {
    header("Location: /Wime/public/HTML/Wime_interfaz_Inicio-Sesion.html");
    exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
    die("❌ Error de conexión: " . $conn->connect_error);
}

$id_usuario = $_SESSION["id_usuario"];
$sql = "SELECT * FROM notificaciones WHERE id_usuario = ? ORDER BY fecha DESC";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $id_usuario);
$stmt->execute();
$resultado = $stmt->get_result();



?>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>🔔 Notificaciones - Wime</title>
  <link rel="stylesheet" href="/Wime/public/bootstrap-5.3.7-dist/css/bootstrap.min.css">
  <style>
    .notificacion-nueva {
      background-color: #e9f5ff;
    }
    .notificacion-tipo {
      font-size: 0.85rem;
      font-weight: bold;
      color: #4a90e2;
    }
  </style>
</head>
<body class="container py-5">
  <h2 class="mb-4">🔔 Tus Notificaciones</h2>

  <form action="/Wime/Controllers/NotiController.php" method="post" class="mb-3 d-flex gap-2">
  <button name="accion" value="marcar_leidas" class="btn btn-success">✅ Marcar todas como leídas</button>
  <button name="accion" value="eliminar_todas" class="btn btn-danger" onclick="return confirm('¿Estás seguro de eliminar todas las notificaciones?')">🗑️ Eliminar todas</button>
  </form>


  <?php if ($resultado->num_rows > 0): ?>
    <ul class="list-group">
      <?php while ($n = $resultado->fetch_assoc()): ?>
        <li class="list-group-item d-flex justify-content-between align-items-start <?= $n["leida"] ? '' : 'notificacion-nueva' ?>">
          <div class="ms-2 me-auto">
            <div class="notificacion-tipo"><?= ucfirst($n["tipo"]) ?> | <?= date("d M, H:i", strtotime($n["fecha"])) ?></div>
            <?= htmlspecialchars($n["mensaje"]) ?>
          </div>
          <?php if (!$n["leida"]): ?>
            <span class="badge bg-primary rounded-pill">Nuevo</span>
          <?php endif; ?>
        </li>
      <?php endwhile; ?>
    </ul>
  <?php else: ?>
    <div class="alert alert-info">No tienes notificaciones por el momento.</div>
  <?php endif; ?>

  <a href="/Wime/private/PhP/Wime_interfaz_Tablero.php" class="btn btn-outline-secondary mt-4">← Volver al Tablero</a>

</body>
</html>
