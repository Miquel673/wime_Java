<?php
session_start();

if (!isset($_SESSION["id_usuario"])) {
  header("Location: /Wime/public/HTML/Wime_interfaz_Inicio-Sesion.html");
  exit;
}

$id_rutina = $_GET["id"] ?? null;
if (!$id_rutina) {
  echo "⚠️ Rutina no especificada";
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
$rutina = [];

if ($stmt = $conn->prepare("SELECT * FROM rutinas WHERE IDRutina = ?")) {
  $stmt->bind_param("i", $id_rutina);
  $stmt->execute();
  $result = $stmt->get_result();
  $rutina = $result->fetch_assoc();
  $stmt->close();
}
$conn->close();
?>


<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Editar Tarea</title>
  <link rel="stylesheet" href="/Wime/public/bootstrap-5.3.7-dist/css/bootstrap.min.css">
</head>
<body>
  <div class="container py-5">
    <h2 class="mb-4">Editar Rutina</h2>



    <form id="form-editar-rutina">
  <input type="hidden" name="IDRutina" value="<?= $rutina['IDRutina'] ?>">

  <div class="mb-3">
    <label class="form-label">Nombre</label>
    <input type="text" name="NombreRutina" class="form-control" value="<?= $rutina['NombreRutina'] ?>" required>
  </div>

  <div class="mb-3">
    <label class="form-label">Prioridad</label>
    <select name="Prioridad" class="form-select">
      <option value="alta" <?= $rutina['Prioridad'] === 'alta' ? 'selected' : '' ?>>Alta</option>
      <option value="media" <?= $rutina['Prioridad'] === 'media' ? 'selected' : '' ?>>Media</option>
      <option value="baja" <?= $rutina['Prioridad'] === 'baja' ? 'selected' : '' ?>>Baja</option>
    </select>
  </div>

  <!-- Continúa con Frecuencia, Fechas, Estado, Descripción, etc. -->
  <button type="submit" class="btn btn-primary">Guardar cambios</button>
</form>


  <script src="/Wime/public/bootstrap-5.3.7-dist/js/bootstrap.bundle.min.js"></script>
  <script src="/Wime/public/Js/Wime_Editar.js"></script>
</body>
</html>
