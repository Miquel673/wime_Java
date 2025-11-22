-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-11-2025 a las 15:58:23
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `wime`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `Actualizar` ()   UPDATE rutina
SET Estado = 
  CASE 
    WHEN FechaCompletorutina > FechaFin THEN 'Incompleto'
    ELSE 'Completo'
  END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Actualizar2` ()   BEGIN
 UPDATE rutina
    SET Estado = 'incompleto'
    WHERE FechaFin < NOW()
      AND Estado != 'incompleto';
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Informe` ()   BEGIN
    SELECT 
        u.IDusuario,
        u.NombreUsuario AS NombreUsuario,
        t.IDTarea,
        t.NombreTarea AS NombreTarea,
        t.Estado,
        t.FechaAsignacion,
        t.FechaFIN,
       ROUND(TIMESTAMPDIFF(MINUTE, t.FechaAsignacion, t.FechaFIN) / 1440, 2) AS Dias_Ejecutado
    FROM tarea t
    INNER JOIN usuario u ON t.IDusuario = u.IDusuario
    WHERE t.Estado = 'Completo'
    ORDER BY u.IDusuario, t.FechaFIN DESC;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `IDCategoria` int(1) NOT NULL,
  `IDusuario` int(11) NOT NULL,
  `IDTarea` int(11) NOT NULL,
  `IDRurtina` int(11) NOT NULL,
  `NombreCategoria` varchar(50) NOT NULL,
  `DescripcionCategoria` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`IDCategoria`, `IDusuario`, `IDTarea`, `IDRurtina`, `NombreCategoria`, `DescripcionCategoria`) VALUES
(4, 4, 4, 4, 'Dia a dia', NULL),
(5, 5, 5, 5, 'Mejorar', NULL),
(6, 6, 6, 6, 'ser Buen hermano', NULL),
(7, 7, 7, 7, 'No explotar', NULL),
(8, 8, 8, 8, 'Relajarme', NULL),
(9, 9, 9, 9, 'Ser el mejor', NULL),
(10, 10, 10, 10, 'Ayudar', NULL),
(11, 11, 11, 11, 'joker', NULL),
(12, 12, 12, 12, 'ser mas fuerte', NULL),
(13, 13, 13, 13, 'cosas que nunca hize', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notificaciones`
--

CREATE TABLE `notificaciones` (
  `IDnotificacion` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  `mensaje` text DEFAULT NULL,
  `fecha` datetime DEFAULT current_timestamp(),
  `leida` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `notificaciones`
--

INSERT INTO `notificaciones` (`IDnotificacion`, `id_usuario`, `tipo`, `mensaje`, `fecha`, `leida`) VALUES
(11, 34, 'tarea', 'Se ha creado una nueva tarea: escribirle al pana miguel kkkk', '2025-08-04 19:35:31', 1),
(92, 20, 'Tarea actualizada', 'La tarea \'Interfaz de Administrador Wime\' cambió su estado a: completada', '2025-10-30 04:09:31', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `progresotarea`
--

CREATE TABLE `progresotarea` (
  `IDprogreso` int(11) NOT NULL,
  `IDTarea` int(11) NOT NULL,
  `IDRutina` int(11) NOT NULL,
  `NombreUsuario` varchar(20) NOT NULL,
  `FechaReporte` date NOT NULL,
  `TiempoReporte` date NOT NULL,
  `Porcentaje` int(11) NOT NULL,
  `EstadoTarea` enum('Incompleto','Completo') NOT NULL,
  `EstadoRutina` enum('Incompleto','Completo') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `progresotarea`
--

INSERT INTO `progresotarea` (`IDprogreso`, `IDTarea`, `IDRutina`, `NombreUsuario`, `FechaReporte`, `TiempoReporte`, `Porcentaje`, `EstadoTarea`, `EstadoRutina`) VALUES
(4, 4, 4, 'Katerin', '2025-04-15', '2025-04-01', 50, 'Incompleto', 'Completo'),
(5, 5, 5, 'Rin', '2025-04-01', '2025-04-05', 50, 'Incompleto', 'Completo'),
(6, 6, 6, 'Yukio', '2025-04-05', '2025-04-01', 50, 'Completo', 'Incompleto'),
(7, 7, 7, 'John', '2025-04-22', '2025-04-30', 0, 'Incompleto', 'Incompleto'),
(8, 8, 8, 'Serafina', '2025-04-02', '2025-04-16', 50, 'Completo', 'Incompleto'),
(9, 9, 9, 'Arlo', '2025-04-28', '2025-03-05', 50, 'Completo', 'Incompleto'),
(10, 10, 10, 'Remi', '2025-04-01', '2025-03-04', 50, 'Completo', 'Incompleto'),
(11, 11, 11, 'Blyke', '2025-04-16', '2025-04-01', 50, 'Completo', 'Incompleto'),
(12, 12, 12, 'Subaru', '2025-04-25', '2025-04-02', 100, 'Completo', 'Completo'),
(13, 13, 13, 'Reinhard ', '2025-04-29', '2025-04-15', 0, 'Incompleto', 'Incompleto');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rolespermisos`
--

CREATE TABLE `rolespermisos` (
  `IDRoles` int(11) NOT NULL,
  `IDUsuario` int(11) NOT NULL,
  `NombreUsuario` varchar(20) NOT NULL,
  `permisos` varchar(50) NOT NULL,
  `Roles` enum('Administrador','Usuario') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rolespermisos`
--

INSERT INTO `rolespermisos` (`IDRoles`, `IDUsuario`, `NombreUsuario`, `permisos`, `Roles`) VALUES
(4, 4, 'Katerin', 'Select,pdate,Intert', 'Usuario'),
(5, 5, 'Rin', 'Select,pdate,Insert', 'Usuario'),
(6, 6, 'Yukio', 'Select,pdate,Insert', ''),
(7, 7, 'John', 'Select,pdate,Insert', 'Usuario'),
(8, 8, 'Serafina', 'Select,pdate,Insert', 'Usuario'),
(9, 9, 'Arlo', 'Select,pdate,Insert', 'Usuario'),
(10, 10, 'Remi', 'Select,pdate,Insert', 'Usuario'),
(11, 11, 'Blyke', 'Select,pdate,Insert', 'Usuario'),
(12, 12, 'Subaru ', 'Select,pdate,Insert', 'Usuario'),
(13, 13, 'Reinhard', 'Select,pdate,Insert', 'Usuario'),
(14, 14, 'Administrador', 'ALL PRIVILEGES', 'Administrador');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rutinas`
--

CREATE TABLE `rutinas` (
  `IDRutina` int(11) NOT NULL,
  `IDusuarios` int(11) NOT NULL,
  `NombreRutina` varchar(100) NOT NULL,
  `FechaAsignacion` date NOT NULL,
  `FechaFin` date NOT NULL,
  `Fechacompletorutina` date DEFAULT NULL,
  `Prioridad` enum('alta','media','baja') NOT NULL,
  `Descripcion` text DEFAULT NULL,
  `compartir_con` varchar(100) DEFAULT NULL,
  `Estado` enum('pendiente','en progreso','completada') DEFAULT 'pendiente',
  `Frecuencia` enum('diario','semanal','mensual') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rutinas`
--

INSERT INTO `rutinas` (`IDRutina`, `IDusuarios`, `NombreRutina`, `FechaAsignacion`, `FechaFin`, `Fechacompletorutina`, `Prioridad`, `Descripcion`, `compartir_con`, `Estado`, `Frecuencia`) VALUES
(2, 20, 'Programar', '2025-07-08', '2025-07-12', NULL, 'alta', 'Realizar avances del proyecto en java spring y mejorar interfaces y demas detalles\n', '', 'completada', 'semanal'),
(9, 20, 'programar FrontEnd', '2025-10-27', '2025-12-27', NULL, 'alta', 'Se realizaran cambios en el FrontEnd', NULL, 'completada', 'semanal');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tareas`
--

CREATE TABLE `tareas` (
  `IDtarea` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `prioridad` enum('alta','media','baja') NOT NULL,
  `fecha_limite` date DEFAULT NULL,
  `compartir_con` varchar(100) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'pendiente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tareas`
--

INSERT INTO `tareas` (`IDtarea`, `id_usuario`, `titulo`, `prioridad`, `fecha_limite`, `compartir_con`, `descripcion`, `estado`) VALUES
(8, 24, 'Dashboart', 'alta', '2025-02-12', NULL, 'Creación primera tarea', 'pendiente'),
(9, 24, 'gei', 'alta', '2026-03-04', NULL, 'gei el que lo lea', 'pendiente'),
(13, 25, 'hello world', 'alta', '2025-04-04', NULL, 'errfe', 'completada'),
(16, 20, 'Crear Modulo de vista de rutinas', 'alta', '2025-07-08', NULL, 'se harán las vistas del modulo de las rutinas basandonos en la base de datos y lo que se supone que deba hacer el modulo', 'completada'),
(17, 20, 'Interfaz de Administrador Wime', 'alta', '2025-07-10', NULL, 'Se realizara la vista de la interfaz que tendra un usuario administrador en el sistema junto con una forma unica de ingreso', 'completada'),
(18, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'Realizacion de la vista de la bandeja de entrada y funcionabilidad', 'completada'),
(19, 20, 'Actualizacion de pantallas .JSON a Mensajes emergentes', 'alta', '2025-07-11', NULL, 'Se ajustaran interfaces para evitar mostrar archivos .JSON', 'completada'),
(21, 20, 'Pulir Software', 'media', '2025-07-13', NULL, 'Se deberan pulir detalles, botones, colores y filtros, configuraciones', 'en progreso'),
(44, 20, 'Realizacion de reportes', 'alta', '2025-07-12', NULL, 'mañana hay exposicion', 'completada'),
(57, 32, 'proyecto wime', 'alta', '2025-07-13', NULL, 'terminar pagina web', 'completada'),
(60, 34, 'escribirle al pana miguel kkkk', 'alta', '2028-02-05', NULL, 'jsjsjsjjsjs hola', 'completada'),
(62, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'Revisar el archivo .js', 'completada'),
(66, 20, 'Realizar la validacion de contraseñas', 'alta', '2025-11-01', NULL, 'Se realizara la correcta validacion de la creacion de la contraseña del usuario', 'pendiente'),
(67, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'Se realizara revision de errores y se implementaran nuevas funciones interactivas', 'pendiente'),
(68, 20, 'hello world', 'alta', '2025-05-05', NULL, 'hola', 'completada');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `IDusuario` int(11) NOT NULL,
  `NombreUsuario` varchar(20) NOT NULL,
  `FechaRegistro` datetime NOT NULL,
  `EmailUsuario` varchar(50) NOT NULL,
  `ContrasenaUsuario` varchar(225) NOT NULL,
  `Edad` int(11) NOT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `ultimo_login` datetime(6) DEFAULT NULL,
  `foto_perfil` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`IDusuario`, `NombreUsuario`, `FechaRegistro`, `EmailUsuario`, `ContrasenaUsuario`, `Edad`, `tipo`, `estado`, `ultimo_login`, `foto_perfil`) VALUES
(20, 'Miguel Ibarvo', '2025-07-01 01:40:11', 'mixagg6@gmail.com', '$2y$10$ZmCJAykvmGduB/T40TETmOUqoLDU4O8U3uvyH896NwYm9q0xkEem.', 19, 'Corriente', 'Activo', '2025-11-21 03:18:50.000000', 'http://localhost:8080/uploads/fotos_perfil/usuario_20_1762909838528_Captura de pantalla 2025-05-25 203036.png'),
(24, 'Kevinlop25', '2025-07-06 16:18:21', 'kevinlop2524@gmail.com', '$2y$10$go3HnFnHKbKChP3p7HVOw.kFZban/eUpPfMFLdvb5NK37HuWJOaa2', 18, 'Corriente', 'Inactivo', NULL, NULL),
(25, 'jordan', '2025-07-06 23:13:34', 'hello@gmail.com', '$2y$10$F1JcwXO2xUwwmqAmlaMPjOeF2yjomgLdbW7r2me9Id/yPuUeRxrqe', 19, 'Corriente', 'Inactivo', NULL, NULL),
(29, 'Miguel Ibarvo', '2025-07-10 06:46:00', 'mixagg7@gmail.com', 'nigga', 19, 'Administrador', 'Activo', NULL, NULL),
(31, 'Prueba01', '2025-07-13 07:02:28', 'Miguel@gmailcom', 'ngga', 19, 'Corriente', 'Inactivo', NULL, NULL),
(32, 'padme', '2025-07-13 14:02:06', 'evaarroyo862@gmail.com', '$2y$10$RPoiIdjXcEDh2UebIf4HIuqgMjcs8WkjGGFy2v0cL9QTKs8GtKQ7W', 18, 'Corriente', 'Inactivo', NULL, NULL),
(33, 'andrea', '2025-07-13 14:05:48', 'paula.barbosa.0524@gmail.com', '$2y$10$UIDeblbQr79z1eVSOi4yDOjVgRQiGm72jw0Ebqih.AlMEbAslv2/m', 19, 'Corriente', 'Inactivo', NULL, NULL),
(34, 'tomas', '2025-08-05 02:34:17', 'tomas@gmail.com', '$2y$10$c7pLA7eb8NCV6f.j.tKIP.hG29VkuME4R0tvPymSrVPgOp1JWuUnm', 20, 'Corriente', 'Activo', '2025-08-04 19:34:43.000000', NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`IDCategoria`),
  ADD UNIQUE KEY `IDTarea` (`IDTarea`,`IDRurtina`);

--
-- Indices de la tabla `notificaciones`
--
ALTER TABLE `notificaciones`
  ADD PRIMARY KEY (`IDnotificacion`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `progresotarea`
--
ALTER TABLE `progresotarea`
  ADD PRIMARY KEY (`IDprogreso`),
  ADD UNIQUE KEY `IDTarea` (`IDTarea`,`IDRutina`);

--
-- Indices de la tabla `rolespermisos`
--
ALTER TABLE `rolespermisos`
  ADD PRIMARY KEY (`IDRoles`),
  ADD KEY `IDUsuario` (`IDUsuario`);

--
-- Indices de la tabla `rutinas`
--
ALTER TABLE `rutinas`
  ADD PRIMARY KEY (`IDRutina`),
  ADD KEY `Id_usuario` (`IDusuarios`) USING BTREE;

--
-- Indices de la tabla `tareas`
--
ALTER TABLE `tareas`
  ADD PRIMARY KEY (`IDtarea`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`IDusuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `notificaciones`
--
ALTER TABLE `notificaciones`
  MODIFY `IDnotificacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=93;

--
-- AUTO_INCREMENT de la tabla `progresotarea`
--
ALTER TABLE `progresotarea`
  MODIFY `IDprogreso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `rutinas`
--
ALTER TABLE `rutinas`
  MODIFY `IDRutina` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `tareas`
--
ALTER TABLE `tareas`
  MODIFY `IDtarea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `IDusuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
