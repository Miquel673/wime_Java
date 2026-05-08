-- Script de recreacion completa para la base de datos del proyecto Wime Java
-- Motor objetivo: MySQL 8 / MariaDB compatible con InnoDB y utf8mb4
-- Credenciales semilla:
--   admin@wime.com / admin123
--   user@wime.com  / user123

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `wime1`;
CREATE DATABASE `wime1`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `wime1`;

CREATE TABLE `usuario` (
  `IDusuario` BIGINT NOT NULL AUTO_INCREMENT,
  `NombreUsuario` VARCHAR(100) NOT NULL,
  `FechaRegistro` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `EmailUsuario` VARCHAR(150) NOT NULL,
  `ContrasenaUsuario` VARCHAR(255) NOT NULL,
  `Tipo` VARCHAR(50) NOT NULL DEFAULT 'Usuario',
  `Estado` VARCHAR(20) NOT NULL DEFAULT 'Activo',
  `ultimo_login` DATETIME(6) DEFAULT NULL,
  `foto_perfil` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`IDusuario`),
  UNIQUE KEY `uk_usuario_email` (`EmailUsuario`),
  KEY `idx_usuario_tipo` (`Tipo`),
  KEY `idx_usuario_estado` (`Estado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tareas` (
  `IDtarea` BIGINT NOT NULL AUTO_INCREMENT,
  `id_usuario` BIGINT NOT NULL,
  `titulo` VARCHAR(200) NOT NULL,
  `prioridad` VARCHAR(50) NOT NULL,
  `fecha_limite` DATE DEFAULT NULL,
  `descripcion` TEXT DEFAULT NULL,
  `estado` VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
  PRIMARY KEY (`IDtarea`),
  KEY `idx_tareas_usuario` (`id_usuario`),
  KEY `idx_tareas_estado` (`estado`),
  KEY `idx_tareas_fecha_limite` (`fecha_limite`),
  CONSTRAINT `fk_tareas_usuario`
    FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`IDusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `rutinas` (
  `IDRutina` BIGINT NOT NULL AUTO_INCREMENT,
  `IDusuarios` BIGINT NOT NULL,
  `NombreRutina` VARCHAR(200) NOT NULL,
  `FechaAsignacion` DATE NOT NULL,
  `FechaFin` DATE NOT NULL,
  `Fechacompletorutina` DATE DEFAULT NULL,
  `Prioridad` VARCHAR(50) NOT NULL,
  `Descripcion` TEXT DEFAULT NULL,
  `compartir_con` VARCHAR(255) DEFAULT NULL,
  `Estado` VARCHAR(50) NOT NULL DEFAULT 'pendiente',
  `Frecuencia` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`IDRutina`),
  KEY `idx_rutinas_usuario` (`IDusuarios`),
  KEY `idx_rutinas_estado` (`Estado`),
  KEY `idx_rutinas_fecha_fin` (`FechaFin`),
  CONSTRAINT `fk_rutinas_usuario`
    FOREIGN KEY (`IDusuarios`) REFERENCES `usuario` (`IDusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `notificaciones` (
  `IDnotificacion` BIGINT NOT NULL AUTO_INCREMENT,
  `id_usuario` BIGINT NOT NULL,
  `tipo` VARCHAR(50) NOT NULL,
  `mensaje` TEXT NOT NULL,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `leida` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`IDnotificacion`),
  KEY `idx_notificaciones_usuario` (`id_usuario`),
  KEY `idx_notificaciones_fecha` (`fecha`),
  CONSTRAINT `fk_notificaciones_usuario`
    FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`IDusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tareas_usuarios` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `id_tarea` BIGINT NOT NULL,
  `id_usuario` BIGINT NOT NULL,
  `rol` ENUM('CREADOR', 'COMPARTIDA') NOT NULL,
  `fecha_asignacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tarea_usuario` (`id_tarea`, `id_usuario`),
  KEY `idx_tareas_usuarios_usuario` (`id_usuario`),
  CONSTRAINT `fk_tareas_usuarios_tarea`
    FOREIGN KEY (`id_tarea`) REFERENCES `tareas` (`IDtarea`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_tareas_usuarios_usuario`
    FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`IDusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `rutinas_usuarios` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `id_rutina` BIGINT NOT NULL,
  `id_usuario` BIGINT NOT NULL,
  `rol` ENUM('CREADOR', 'COMPARTIDA') NOT NULL,
  `fecha_asignacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rutina_usuario` (`id_rutina`, `id_usuario`),
  KEY `idx_rutinas_usuarios_usuario` (`id_usuario`),
  CONSTRAINT `fk_rutinas_usuarios_rutina`
    FOREIGN KEY (`id_rutina`) REFERENCES `rutinas` (`IDRutina`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_rutinas_usuarios_usuario`
    FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`IDusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `usuario` (
  `NombreUsuario`,
  `EmailUsuario`,
  `ContrasenaUsuario`,
  `Tipo`,
  `Estado`
) VALUES
  (
    'Admin',
    'admin@wime.com',
    '$2a$10$0EMPfVf29qqomkUi1s1RJ.ktKtdHGOtRCqc9g9.vnJEIjoNviaEU.',
    'Admin',
    'Activo'
  ),
  (
    'Usuario Demo',
    'user@wime.com',
    '$2a$10$aC8GCfuA8kg5PGTnwDtdwuIk8opwVsSLaTG4sLYN9YQx2BaAttJQO',
    'Usuario',
    'Activo'
  );

-- Datos opcionales de ejemplo. Descomenta si quieres arrancar con contenido de prueba.
-- INSERT INTO `tareas` (`id_usuario`, `titulo`, `prioridad`, `fecha_limite`, `descripcion`, `estado`)
-- VALUES (1, 'Primera tarea', 'alta', CURRENT_DATE + INTERVAL 7 DAY, 'Tarea inicial de prueba', 'PENDIENTE');
--
-- INSERT INTO `tareas_usuarios` (`id_tarea`, `id_usuario`, `rol`)
-- VALUES (1, 1, 'CREADOR');
--
-- INSERT INTO `rutinas` (`IDusuarios`, `NombreRutina`, `FechaAsignacion`, `FechaFin`, `Prioridad`, `Descripcion`, `Estado`, `Frecuencia`)
-- VALUES (1, 'Rutina inicial', CURRENT_DATE, CURRENT_DATE + INTERVAL 14 DAY, 'media', 'Rutina de ejemplo', 'pendiente', 'semanal');
--
-- INSERT INTO `rutinas_usuarios` (`id_rutina`, `id_usuario`, `rol`)
-- VALUES (1, 1, 'CREADOR');
--
-- INSERT INTO `notificaciones` (`id_usuario`, `tipo`, `mensaje`)
-- VALUES (1, 'Sistema', 'Base de datos Wime creada correctamente.');
