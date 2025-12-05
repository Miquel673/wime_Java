<?php
session_start();
if (!isset($_SESSION["id_usuario"])) {
  header("Location: /Wime/public/HTML/Wime_interfaz_Inicio-Sesion.html");
  exit;
}

$conn = new mysqli("localhost", "root", "", "Wime");
if ($conn->connect_error) {
  die("Error de conexión");
}

$id_usuario = $_SESSION["id_usuario"];
$id = $_GET["id"] ?? null;

if (!$id) {
  echo "ID de tarea no especificado";
  exit;
}

$sql = "SELECT * FROM tareas WHERE IDtarea = ? AND id_usuario = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ii", $id, $id_usuario);
$stmt->execute();
$resultado = $stmt->get_result();

$tarea = $resultado->fetch_assoc();
if (!$tarea) {
  echo "Tarea no encontrada";
  exit;
}
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
    <h2 class="mb-4">Editar Tarea</h2>

    <form id="form-editar-tarea" class="bg-light p-4 rounded shadow-sm">
      <input type="hidden" name="id" value="<?= $tarea['IDtarea'] ?>">
      
      <div class="mb-3">
        <label class="form-label">Título</label>
        <input type="text" name="titulo" class="form-control" value="<?= htmlspecialchars($tarea['titulo']) ?>" required>
      </div>

      <div class="mb-3">
        <label class="form-label">Prioridad</label>
        <?php
        $prioridades = ["alta", "media", "baja"];
        foreach ($prioridades as $p) {
          $checked = $tarea['prioridad'] === $p ? 'checked' : '';
          echo "<div class='form-check form-check-inline'>
                  <input class='form-check-input' type='radio' name='prioridad' value='$p' $checked>
                  <label class='form-check-label'>" . ucfirst($p) . "</label>
                </div>";
        }
        ?>
      </div>

      <div class="mb-3">
        <label class="form-label">Fecha Límite</label>
        <input type="date" name="fecha_limite" class="form-control" value="<?= $tarea['fecha_limite'] ?>" required>
      </div>

      <div class="mb-3">
        <label class="form-label">Descripción</label>
        <textarea name="descripcion" class="form-control" rows="4"><?= htmlspecialchars($tarea['descripcion']) ?></textarea>
      </div>

      <div class="mb-3">
        <label class="form-label">Estado</label>
        <select name="estado" class="form-select">
          <?php
          $estados = ["pendiente", "en progreso", "completada"];
          foreach ($estados as $e) {
            $selected = $tarea['estado'] === $e ? 'selected' : '';
            echo "<option value='$e' $selected>" . ucfirst($e) . "</option>";
          }
          ?>
        </select>
      </div>

      <button type="submit" class="btn btn-primary">Guardar Cambios</button>
      <a href="/Wime/private/PhP/Wime_interfaz_Tablero.php" class="btn btn-secondary">Cancelar</a>
    </form>
  </div>

  <script src="/Wime/public/bootstrap-5.3.7-dist/js/bootstrap.bundle.min.js"></script>
  <script src="/Wime/public/Js/Wime_Editar.js"></script>
</body>
</html>
