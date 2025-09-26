-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 06-07-2025 a las 03:17:33
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
  `IDnotificaciones` int(11) NOT NULL,
  `IDusuario` int(11) NOT NULL,
  `IDTarea` int(11) NOT NULL,
  `IDRutina` int(11) NOT NULL,
  `NombreTarea` varchar(50) NOT NULL,
  `NombreRutina` varchar(50) NOT NULL,
  `Mensaje` varchar(100) NOT NULL,
  `FechaHora` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `notificaciones`
--

INSERT INTO `notificaciones` (`IDnotificaciones`, `IDusuario`, `IDTarea`, `IDRutina`, `NombreTarea`, `NombreRutina`, `Mensaje`, `FechaHora`) VALUES
(4, 4, 4, 4, 'anime', 'Ejercicio', 'Un paso a la vez es suficiente, siempre que no te detengas', '2025-04-24 13:30:00'),
(5, 5, 5, 5, 'cita', 'practicar', 'El trabajo duro es inútil si no crees en ti mismo', '2025-04-05 17:39:02'),
(6, 6, 6, 6, 'Cocina', 'ingles', 'No importa cuán lento vayas, mientras no te detengas', '2025-04-25 10:42:32'),
(7, 7, 7, 7, 'Ejercicio', 'limpieza', 'Incluso si no tienes talento, puedes superar a alguien que lo tiene si trabajas lo suficiente', '2025-04-05 17:39:02'),
(8, 8, 8, 8, 'videojuegos', 'codigo', 'No hay atajos para llegar a lo más alto. ¡Tienes que escalar paso a paso!', '2025-04-05 17:39:02'),
(9, 9, 9, 9, 'Trabajo', 'Conducir', 'Las pequeñas victorias también son victorias. Celebra cada una de ellas', '2025-04-05 17:45:19'),
(10, 10, 10, 10, 'compras', 'anime', 'No hay nada que no puedas hacer si te lo propones', '2025-04-05 17:45:19'),
(11, 11, 11, 11, 'peliculas', 'caminata', 'Los errores son la prueba de que lo estás intentando', '2025-04-05 17:45:19'),
(12, 12, 12, 12, 'Lavar', 'trabajo', '¡Mientras no me rinda, no ha terminado!', '2025-04-05 17:45:19'),
(13, 13, 13, 13, 'oficio', 'bicicleta', 'La fuerza no siempre se mide en poder… a veces se trata de no rendirse.', '2025-04-05 17:45:19');

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
-- Estructura de tabla para la tabla `rutina`
--

CREATE TABLE `rutina` (
  `IDRutina` int(11) NOT NULL,
  `IDusuarios` int(1) NOT NULL,
  `NombreRutina` varchar(50) NOT NULL,
  `FechaAsignacion` datetime NOT NULL,
  `FechaFin` datetime NOT NULL,
  `Fechacompletorutina` datetime DEFAULT NULL,
  `Prioridad` enum('Bajo','Medio','Alto') NOT NULL,
  `Descripcion` varchar(100) NOT NULL,
  `Estado` enum('Incompleto','Completo') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rutina`
--

INSERT INTO `rutina` (`IDRutina`, `IDusuarios`, `NombreRutina`, `FechaAsignacion`, `FechaFin`, `Fechacompletorutina`, `Prioridad`, `Descripcion`, `Estado`) VALUES
(4, 4, 'Ejercicio', '2025-04-01 08:55:23', '2025-04-08 08:55:23', '2025-04-07 10:47:27', 'Alto', 'Ejercitar mi cuerpo', 'Completo'),
(5, 5, 'practicar', '2025-04-01 08:55:23', '2025-04-05 08:55:23', '2025-04-15 10:53:40', 'Alto', 'tomar una hora al dia pra practicar codigo', 'Incompleto'),
(6, 6, 'ingles', '2025-04-01 08:55:23', '2025-04-19 08:55:23', '2025-04-09 10:53:40', 'Medio', 'tomar clase de ingles', 'Completo'),
(7, 7, 'limpieza', '2025-04-01 08:55:23', '2025-04-15 08:55:23', '2025-04-18 10:53:40', 'Medio', 'limpiar mi cuarto todo los dias', 'Incompleto'),
(8, 8, 'codigo', '2025-04-05 15:53:59', '2025-04-09 15:53:59', NULL, 'Medio', 'mejorar mi conicimiento de JS', 'Completo'),
(9, 9, 'Conducir', '2025-04-30 09:01:56', '2025-04-03 09:01:56', NULL, 'Medio', 'tomar clases de conducir', 'Completo'),
(10, 10, 'anime', '2025-05-01 09:01:56', '2025-04-14 09:01:56', NULL, 'Bajo', 'leer todo el manga de One piece', 'Completo'),
(11, 11, 'caminata', '2025-04-05 16:00:05', '2025-04-10 16:00:05', NULL, 'Medio', 'salir a caminar', 'Completo'),
(12, 12, 'trabajo', '2025-04-05 16:00:05', '2025-04-12 16:00:05', NULL, 'Alto', 'trabajar horas extras', 'Completo'),
(13, 13, 'bicicleta', '2025-04-05 16:00:05', '2025-04-10 16:00:05', NULL, 'Bajo', 'andar en bicicleta ', 'Completo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tarea`
--

CREATE TABLE `tarea` (
  `IDTarea` int(11) NOT NULL,
  `IDusuario` int(11) NOT NULL,
  `NombreTarea` varchar(50) NOT NULL,
  `FechaAsignacion` datetime NOT NULL,
  `FechaFin` datetime NOT NULL,
  `Descripcion` varchar(100) NOT NULL,
  `Prioridad` enum('Bajo','Medio','Alto') NOT NULL,
  `Estado` enum('Incompleto','Completo') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tarea`
--

INSERT INTO `tarea` (`IDTarea`, `IDusuario`, `NombreTarea`, `FechaAsignacion`, `FechaFin`, `Descripcion`, `Prioridad`, `Estado`) VALUES
(4, 4, 'anime', '2025-04-01 15:22:08', '2025-04-12 15:22:08', 'Maraton de Rezero', 'Alto', 'Completo'),
(5, 5, 'cita', '2025-04-05 15:50:41', '2025-04-17 15:50:41', 'Dia de salida con mi novia', 'Alto', 'Completo'),
(6, 6, 'Cocina', '2025-04-05 15:50:41', '2025-04-05 15:50:41', 'tomar clases de cocina', 'Medio', 'Completo'),
(7, 7, 'ejercicios', '2025-04-01 08:22:15', '2025-04-30 08:22:15', '20 rep de abdomen serie de 4, y 20 flexiones serie de 4', 'Alto', 'Completo'),
(8, 8, 'Videojuegos', '2025-04-05 15:22:08', '2025-04-10 15:22:08', 'Completar misioness diarias', 'Medio', 'Completo'),
(9, 9, 'trabajo', '2025-04-05 15:22:08', '2025-04-05 15:22:08', 'Entregar documento a tiempo', 'Alto', 'Completo'),
(10, 10, 'compras', '2025-04-05 15:50:41', '2025-04-05 15:50:41', 'Dia de compras', 'Bajo', 'Completo'),
(11, 11, 'peliculas', '2025-04-02 08:50:46', '2025-04-03 08:50:46', 'Maraton de Harry potter', 'Medio', 'Incompleto'),
(12, 12, 'Lavar', '2025-04-06 08:50:46', '2025-04-08 08:50:46', 'Lavar toda la ropa sucia', 'Medio', 'Incompleto'),
(13, 13, 'oficio', '2025-04-15 08:22:15', '2025-04-20 08:22:15', 'Limpieza profunda', 'Medio', '');

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
  `Edad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`IDusuario`, `NombreUsuario`, `FechaRegistro`, `EmailUsuario`, `ContrasenaUsuario`, `Edad`) VALUES
(19, 'HelloWorld', '2025-06-30 22:58:02', 'helloworld@gmail.com', '$2y$10$H6W2owhPnzL5QDxuYxCPFuKMPIpL026Z22WpRP83p7bXjCJcF4ymm', 23),
(20, 'Miguel Ibarvo', '2025-07-01 01:40:11', 'mixagg6@gmail.com', '$2y$10$ZmCJAykvmGduB/T40TETmOUqoLDU4O8U3uvyH896NwYm9q0xkEem.', 19),
(21, 'cu', '2025-07-02 03:39:03', 'hu@gmail.com', '$2y$10$B0yJ5c0kLKo0YQMY3ky8Iep.CCVYDnugN4GI1tW6EW6WRqDcAX0L2', 13),
(22, 'Miguel chevere', '2025-07-03 05:45:51', 'miguel06@gmail.com', '$2y$10$3lJ3LGLTc3Zi.yRRfTjDHeRBbYdV7.ym6c/GHTctiPDGrsSIdiql.', 19);

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
  ADD PRIMARY KEY (`IDnotificaciones`),
  ADD UNIQUE KEY `IDusuario` (`IDusuario`,`IDTarea`,`IDRutina`);

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
-- Indices de la tabla `rutina`
--
ALTER TABLE `rutina`
  ADD PRIMARY KEY (`IDRutina`),
  ADD UNIQUE KEY `UDusuario` (`IDusuarios`),
  ADD UNIQUE KEY `IDusuario` (`IDusuarios`);

--
-- Indices de la tabla `tarea`
--
ALTER TABLE `tarea`
  ADD PRIMARY KEY (`IDTarea`);

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
  MODIFY `IDnotificaciones` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `progresotarea`
--
ALTER TABLE `progresotarea`
  MODIFY `IDprogreso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `rutina`
--
ALTER TABLE `rutina`
  MODIFY `IDRutina` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `tarea`
--
ALTER TABLE `tarea`
  MODIFY `IDTarea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `IDusuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD CONSTRAINT `categoria_ibfk_1` FOREIGN KEY (`IDCategoria`) REFERENCES `tarea` (`IDTarea`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `categoria_ibfk_2` FOREIGN KEY (`IDCategoria`) REFERENCES `rutina` (`IDRutina`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `notificaciones`
--
ALTER TABLE `notificaciones`
  ADD CONSTRAINT `notificaciones_ibfk_1` FOREIGN KEY (`IDnotificaciones`) REFERENCES `usuario` (`IDusuario`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `notificaciones_ibfk_2` FOREIGN KEY (`IDnotificaciones`) REFERENCES `tarea` (`IDTarea`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `notificaciones_ibfk_3` FOREIGN KEY (`IDnotificaciones`) REFERENCES `rutina` (`IDRutina`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `progresotarea`
--
ALTER TABLE `progresotarea`
  ADD CONSTRAINT `progresotarea_ibfk_1` FOREIGN KEY (`IDprogreso`) REFERENCES `tarea` (`IDTarea`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `progresotarea_ibfk_2` FOREIGN KEY (`IDprogreso`) REFERENCES `rutina` (`IDRutina`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `rolespermisos`
--
ALTER TABLE `rolespermisos`
  ADD CONSTRAINT `rolespermisos_ibfk_1` FOREIGN KEY (`IDRoles`) REFERENCES `usuario` (`IDusuario`) ON UPDATE CASCADE,
  ADD CONSTRAINT `rolespermisos_ibfk_2` FOREIGN KEY (`IDUsuario`) REFERENCES `usuario` (`IDusuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `rutina`
--
ALTER TABLE `rutina`
  ADD CONSTRAINT `rutina_ibfk_1` FOREIGN KEY (`IDRutina`) REFERENCES `usuario` (`IDusuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `tarea`
--
ALTER TABLE `tarea`
  ADD CONSTRAINT `tarea_ibfk_1` FOREIGN KEY (`IDTarea`) REFERENCES `usuario` (`IDusuario`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
