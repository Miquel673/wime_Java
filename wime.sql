-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 31-01-2026 a las 17:41:45
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
-- Base de datos: `wime1`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aspnetroleclaims`
--

CREATE TABLE `aspnetroleclaims` (
  `Id` int(11) NOT NULL,
  `RoleId` varchar(255) NOT NULL,
  `ClaimType` longtext DEFAULT NULL,
  `ClaimValue` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aspnetroles`
--

CREATE TABLE `aspnetroles` (
  `Id` varchar(255) NOT NULL,
  `Name` varchar(256) DEFAULT NULL,
  `NormalizedName` varchar(256) DEFAULT NULL,
  `ConcurrencyStamp` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aspnetuserclaims`
--

CREATE TABLE `aspnetuserclaims` (
  `Id` int(11) NOT NULL,
  `UserId` varchar(255) NOT NULL,
  `ClaimType` longtext DEFAULT NULL,
  `ClaimValue` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aspnetuserlogins`
--

CREATE TABLE `aspnetuserlogins` (
  `LoginProvider` varchar(128) NOT NULL,
  `ProviderKey` varchar(128) NOT NULL,
  `ProviderDisplayName` longtext DEFAULT NULL,
  `UserId` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aspnetuserroles`
--

CREATE TABLE `aspnetuserroles` (
  `UserId` varchar(255) NOT NULL,
  `RoleId` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aspnetusers`
--

CREATE TABLE `aspnetusers` (
  `Id` varchar(255) NOT NULL,
  `UserName` varchar(256) DEFAULT NULL,
  `NormalizedUserName` varchar(256) DEFAULT NULL,
  `Email` varchar(256) DEFAULT NULL,
  `NormalizedEmail` varchar(256) DEFAULT NULL,
  `EmailConfirmed` tinyint(1) NOT NULL,
  `PasswordHash` longtext DEFAULT NULL,
  `SecurityStamp` longtext DEFAULT NULL,
  `ConcurrencyStamp` longtext DEFAULT NULL,
  `PhoneNumber` longtext DEFAULT NULL,
  `PhoneNumberConfirmed` tinyint(1) NOT NULL,
  `TwoFactorEnabled` tinyint(1) NOT NULL,
  `LockoutEnd` datetime(6) DEFAULT NULL,
  `LockoutEnabled` tinyint(1) NOT NULL,
  `AccessFailedCount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `aspnetusers`
--

INSERT INTO `aspnetusers` (`Id`, `UserName`, `NormalizedUserName`, `Email`, `NormalizedEmail`, `EmailConfirmed`, `PasswordHash`, `SecurityStamp`, `ConcurrencyStamp`, `PhoneNumber`, `PhoneNumberConfirmed`, `TwoFactorEnabled`, `LockoutEnd`, `LockoutEnabled`, `AccessFailedCount`) VALUES
('40bb0850-13bf-48a9-9643-8c89e85b83a3', 'migi00823@gmail.com', 'MIGI00823@GMAIL.COM', 'migi00823@gmail.com', 'MIGI00823@GMAIL.COM', 0, 'AQAAAAIAAYagAAAAEPZuMVkOXXEEKzReG4+Eq2knjpSQMLoooeB4PmG9FEa3BOC9rl8E+Tin47hLmzXNbQ==', '5LUMNK6CGWVGU3L5CEABXCITFBUWDDBN', '7c8b5534-bae6-4196-881f-2e2bb438014a', NULL, 0, 0, NULL, 1, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aspnetusertokens`
--

CREATE TABLE `aspnetusertokens` (
  `UserId` varchar(255) NOT NULL,
  `LoginProvider` varchar(128) NOT NULL,
  `Name` varchar(128) NOT NULL,
  `Value` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(99, 39, 'Nueva tarea creada', 'Se ha creado la tarea: Terminar vista y crud de servicios', '2025-12-06 15:44:10', 0),
(100, 39, 'Nueva tarea creada', 'Se ha creado la tarea: dddddd', '2025-12-06 15:45:12', 0),
(101, 40, 'Nueva tarea creada', 'Se ha creado la tarea: Retiro de documentos', '2025-12-06 16:06:04', 0),
(102, 40, 'Nueva tarea creada', 'Se ha creado la tarea: Retiro de documentos', '2025-12-06 16:06:07', 0),
(103, 40, 'Tarea editada', 'Se ha actualizado la tarea: Retiro de documentos', '2025-12-06 16:06:45', 0),
(104, 40, 'Tarea eliminada', 'Se ha eliminado la tarea: Retiro de documentos', '2025-12-06 16:07:12', 0),
(105, 40, 'Nueva tarea creada', 'Se ha creado la tarea: holsa', '2025-12-06 16:11:04', 0),
(106, 40, 'Nueva tarea creada', 'Se ha creado la tarea: holsa', '2025-12-06 16:11:08', 0),
(107, 40, 'Nueva tarea creada', 'Se ha creado la tarea: holsa', '2025-12-06 16:11:10', 0),
(108, 40, 'Nueva tarea creada', 'Se ha creado la tarea: p', '2025-12-06 16:12:24', 0),
(122, 41, 'Rutina eliminada', 'Se ha eliminado la rutina: pasear al perro ', '2025-12-11 03:43:07', 0),
(123, 41, 'Tarea actualizada', 'La tarea \'Crear Modulo de vista de rutinas\' cambió su estado a: pendiente', '2025-12-11 03:43:59', 0),
(124, 41, 'Tarea actualizada', 'La tarea \'Crear Modulo de vista de rutinas\' cambió su estado a: completada', '2025-12-11 03:44:02', 0),
(125, 43, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2025-12-12 05:06:52', 0),
(126, 43, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2025-12-12 05:15:25', 0),
(127, 43, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2025-12-12 05:16:35', 0),
(128, 43, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2025-12-12 05:19:04', 0),
(129, 43, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2025-12-12 05:25:54', 0),
(130, 43, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2025-12-12 05:37:48', 0),
(154, 43, 'Tarea actualizada', 'La tarea \'hello world\' cambió su estado a: pendiente', '2025-12-13 03:18:16', 0),
(155, 43, 'Tarea actualizada', 'La tarea \'hello world\' cambió su estado a: en progreso', '2025-12-13 03:18:20', 0),
(156, 43, 'Tarea eliminada', 'Se ha eliminado la tarea: hello world', '2025-12-13 03:18:34', 0),
(157, 43, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2025-12-13 03:23:12', 0),
(158, 43, 'Tarea eliminada', 'Se ha eliminado la tarea: hello world', '2025-12-13 03:23:48', 0),
(159, 43, 'Tarea eliminada', 'Se ha eliminado la tarea: hello world', '2025-12-13 03:23:54', 0),
(160, 43, 'Tarea eliminada', 'Se ha eliminado la tarea: hello world', '2025-12-13 03:24:00', 0),
(161, 43, 'Tarea eliminada', 'Se ha eliminado la tarea: hello world', '2025-12-13 03:24:05', 0),
(162, 43, 'Tarea eliminada', 'Se ha eliminado la tarea: hello world', '2025-12-13 03:24:11', 0),
(163, 43, 'Nueva tarea creada', 'Se ha creado la tarea: oqjwukh', '2025-12-13 03:24:50', 0),
(164, 43, 'Tarea eliminada', 'Se ha eliminado la tarea: hello world', '2025-12-13 03:25:01', 0),
(165, 43, 'Tarea eliminada', 'Se ha eliminado la tarea: oqjwukh', '2025-12-13 03:25:06', 0),
(166, 43, 'Tarea actualizada', 'La tarea \'Crear Modulo de vista de rutinas\' cambió su estado a: en progreso', '2025-12-13 03:26:17', 0),
(167, 43, 'Tarea actualizada', 'La tarea \'Crear Modulo de vista de rutinas\' cambió su estado a: pendiente', '2025-12-13 03:26:20', 0),
(171, 43, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2025-12-13 05:33:45', 0),
(175, 43, 'Nueva tarea creada', 'Se ha creado la tarea: Prueba de tareas compartidas', '2025-12-13 05:57:24', 0),
(176, 43, 'Tarea actualizada', 'La tarea \'Prueba de tareas compartidas\' cambió su estado a: completada', '2025-12-13 05:58:30', 0),
(189, 43, 'Tarea actualizada', 'La tarea \'Crear Modulo de vista de rutinas\' cambió su estado a: en progreso', '2025-12-13 07:31:02', 0),
(193, 24, 'Nueva tarea creada', 'Se ha creado la tarea: Jugar', '2025-12-13 15:25:17', 0),
(194, 24, 'Tarea actualizada', 'La tarea \'Jugar\' cambió su estado a: completada', '2025-12-13 15:35:11', 0),
(195, 24, 'Tarea actualizada', 'La tarea \'Jugar\' cambió su estado a: en progreso', '2025-12-13 15:35:15', 0),
(210, 20, 'Tarea actualizada', 'La tarea \'Interfaz de Administrador Wime\' cambió su estado a: pendiente', '2026-01-24 02:06:26', 0),
(211, 44, 'Nueva tarea creada', 'Se ha creado la tarea: hello world', '2026-01-28 02:21:02', 1),
(212, 44, 'Tarea actualizada', 'La tarea \'hello world\' cambió su estado a: en progreso', '2026-01-28 02:31:18', 1),
(213, 44, 'Tarea actualizada', 'La tarea \'hello world\' cambió su estado a: completada', '2026-01-28 02:31:30', 1),
(214, 44, 'Tarea actualizada', 'La tarea \'hello world\' cambió su estado a: en progreso', '2026-01-28 02:31:52', 1),
(215, 44, 'Tarea actualizada', 'La tarea \'hello world\' cambió su estado a: completada', '2026-01-28 02:33:16', 1);

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
(2, 20, 'Programar', '2025-07-08', '2025-07-12', NULL, 'alta', 'Realizar avances del proyecto en java spring y mejorar interfaces y demas detalles\n', '', 'pendiente', 'semanal'),
(9, 20, 'programar FrontEnd', '2025-10-27', '2025-12-27', NULL, 'alta', 'Se realizaran cambios en el FrontEnd', NULL, 'en progreso', 'semanal'),
(11, 24, 'Leer', '2025-12-14', '2026-01-01', NULL, 'alta', 'Kevin', NULL, 'pendiente', 'semanal');

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
  `descripcion` text DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'pendiente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tareas`
--

INSERT INTO `tareas` (`IDtarea`, `id_usuario`, `titulo`, `prioridad`, `fecha_limite`, `descripcion`, `estado`) VALUES
(16, 20, 'Crear Modulo de vista de rutinas', 'alta', '2025-07-08', 'se harán las vistas del modulo de las rutinas basandonos en la base de datos y lo que se supone que deba hacer el modulo', 'COMPLETADA'),
(17, 20, 'Interfaz de Administrador Wime', 'alta', '2025-07-10', 'Se realizara la vista de la interfaz que tendra un usuario administrador en el sistema junto con una forma unica de ingreso', 'PENDIENTE'),
(18, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', 'Realizacion de la vista de la bandeja de entrada y funcionabilidad', 'completada'),
(19, 20, 'Actualizacion de pantallas .JSON a Mensajes emergentes', 'alta', '2025-07-11', 'Se ajustaran interfaces para evitar mostrar archivos .JSON', 'completada'),
(62, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', 'Revisar el archivo .js', 'completada'),
(67, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', 'Se realizara revision de errores y se implementaran nuevas funciones interactivas', 'EN_PROGRESO'),
(85, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(88, 20, 'Realizacion de reportes', 'alta', '2025-07-12', NULL, 'completada'),
(91, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(92, 20, 'Realizar la validacion de contraseñas', 'alta', '2025-11-01', NULL, 'pendiente'),
(93, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(97, 20, 'Terminar vista y crud de servicios', 'alta', '2025-12-10', NULL, 'pendiente'),
(104, 20, 'Crear Modulo de vista de rutinas', 'alta', '2025-07-08', NULL, 'completada'),
(105, 20, 'Interfaz de Administrador Wime', 'alta', '2025-07-10', NULL, 'completada'),
(106, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(107, 20, 'Actualizacion de pantallas .JSON a Mensajes emergentes', 'alta', '2025-07-11', NULL, 'completada'),
(108, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(109, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(110, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(111, 20, 'Realizacion de reportes', 'alta', '2025-07-12', NULL, 'completada'),
(112, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(113, 20, 'Realizar la validacion de contraseñas', 'alta', '2025-11-01', NULL, 'pendiente'),
(114, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', 'hola', 'pendiente'),
(115, 20, 'Terminar vista y crud de servicios', 'alta', '2025-12-10', NULL, 'pendiente'),
(116, 41, 'Crear Modulo de vista de rutinas', 'alta', '2025-07-08', NULL, 'completada'),
(117, 41, 'Interfaz de Administrador Wime', 'alta', '2025-07-10', NULL, 'completada'),
(118, 41, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(119, 41, 'Actualizacion de pantallas .JSON a Mensajes emergentes', 'alta', '2025-07-11', NULL, 'completada'),
(120, 41, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(121, 41, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(122, 41, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(123, 41, 'Realizacion de reportes', 'alta', '2025-07-12', NULL, 'completada'),
(124, 41, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(125, 41, 'Realizar la validacion de contraseñas', 'alta', '2025-11-01', NULL, 'pendiente'),
(126, 41, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(127, 41, 'Terminar vista y crud de servicios', 'alta', '2025-12-10', NULL, 'pendiente'),
(134, 43, 'Crear Modulo de vista de rutinas', 'alta', '2025-07-08', NULL, 'EN_PROGRESO'),
(135, 43, 'Interfaz de Administrador Wime', 'alta', '2025-07-10', NULL, 'completada'),
(136, 43, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(137, 43, 'Actualizacion de pantallas .JSON a Mensajes emergentes', 'alta', '2025-07-11', NULL, 'completada'),
(138, 43, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(139, 43, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(140, 43, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(141, 43, 'Realizacion de reportes', 'alta', '2025-07-12', NULL, 'completada'),
(142, 43, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(143, 43, 'Realizar la validacion de contraseñas', 'alta', '2025-11-01', NULL, 'pendiente'),
(144, 43, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(145, 43, 'Terminar vista y crud de servicios', 'alta', '2025-12-10', NULL, 'pendiente'),
(149, 43, 'hello world', 'alta', '2025-12-13', 'hola', 'PENDIENTE'),
(152, 43, 'Prueba de tareas compartidas', 'alta', '2025-12-13', 'Hola mundo', 'COMPLETADA'),
(166, 20, 'Interfaz de Administrador Wime', 'alta', '2025-07-10', NULL, 'EN_PROGRESO'),
(167, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(168, 20, 'Actualizacion de pantallas .JSON a Mensajes emergentes', 'alta', '2025-07-11', NULL, 'completada'),
(169, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(170, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'EN_PROGRESO'),
(171, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(172, 20, 'Realizacion de reportes', 'alta', '2025-07-12', NULL, 'completada'),
(173, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(174, 20, 'Realizar la validacion de contraseñas', 'alta', '2025-11-01', NULL, 'pendiente'),
(175, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(176, 20, 'Terminar vista y crud de servicios', 'alta', '2025-12-10', NULL, 'pendiente'),
(177, 20, 'Crear Modulo de vista de rutinas', 'alta', '2025-07-08', NULL, 'completada'),
(178, 20, 'Interfaz de Administrador Wime', 'alta', '2025-07-10', NULL, 'completada'),
(179, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(180, 20, 'Actualizacion de pantallas .JSON a Mensajes emergentes', 'alta', '2025-07-11', NULL, 'completada'),
(181, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(182, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(183, 20, 'Creacion de vista de bandeja de entrada (Notificaciones)', 'alta', '2025-07-12', NULL, 'completada'),
(184, 20, 'Realizacion de reportes', 'alta', '2025-07-12', NULL, 'completada'),
(185, 20, 'Revisar la actualizacion y eliminacion de las TyR', 'alta', '2025-08-06', NULL, 'completada'),
(186, 20, 'Realizar la validacion de contraseñas', 'alta', '2025-11-01', NULL, 'pendiente'),
(187, 20, 'Realizar detalles de FrontEnd', 'baja', '2025-12-27', NULL, 'pendiente'),
(188, 20, 'Terminar vista y crud de servicios', 'alta', '2025-12-10', NULL, 'pendiente'),
(189, 20, '555', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(190, 20, 'Prueba #8', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(191, 20, 'Prueba #9', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(192, 20, 'Prueba #10', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(193, 20, 'prueba # 12', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(194, 20, 'hola bola', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(195, 20, 'hello world', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(196, 20, 'hello world', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(197, 20, 'hello world', 'alta', '2025-12-13', NULL, 'PENDIENTE'),
(198, 20, 'hello world', 'media', '2025-12-13', NULL, 'PENDIENTE'),
(199, 20, 'hello world', 'media', '2025-12-13', NULL, 'PENDIENTE'),
(200, 20, 'hello world', 'media', '2025-12-18', NULL, 'PENDIENTE'),
(201, 24, 'Jugar', 'alta', '2025-12-14', 'Jugar algo bonito', 'EN_PROGRESO'),
(202, 44, 'hello world', 'alta', '2025-03-03', 'hola mundo', 'COMPLETADA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tareas_usuarios`
--

CREATE TABLE `tareas_usuarios` (
  `id` int(11) NOT NULL,
  `id_tarea` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `rol` enum('CREADOR','COMPARTIDA') NOT NULL,
  `fecha_asignacion` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tareas_usuarios`
--

INSERT INTO `tareas_usuarios` (`id`, `id_tarea`, `id_usuario`, `rol`, `fecha_asignacion`) VALUES
(3, 149, 43, 'CREADOR', '2025-12-13 10:33:45'),
(4, 149, 20, 'COMPARTIDA', '2025-12-13 10:33:45'),
(9, 152, 43, 'CREADOR', '2025-12-13 10:57:24'),
(10, 152, 20, 'COMPARTIDA', '2025-12-13 10:57:24'),
(35, 201, 24, 'CREADOR', '2025-12-13 20:25:17'),
(36, 201, 39, 'COMPARTIDA', '2025-12-13 20:25:17'),
(37, 202, 44, 'CREADOR', '2026-01-28 07:21:02'),
(38, 202, 20, 'COMPARTIDA', '2026-01-28 07:21:02');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `IDusuario` int(11) NOT NULL,
  `NombreUsuario` varchar(20) NOT NULL,
  `FechaRegistro` datetime NOT NULL DEFAULT current_timestamp(),
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
(20, 'Miquel_673', '2025-07-01 01:40:11', 'mixagg6@gmail.com', '$2y$10$ZmCJAykvmGduB/T40TETmOUqoLDU4O8U3uvyH896NwYm9q0xkEem.', 19, 'Corriente', 'Activo', '2026-01-24 02:10:55.000000', '/uploads/fotos_perfil/usuario_20_1769220639549_493.webp'),
(24, 'Kevinlop25', '2025-07-06 16:18:21', 'kevinlop2524@gmail.com', '$2y$10$go3HnFnHKbKChP3p7HVOw.kFZban/eUpPfMFLdvb5NK37HuWJOaa2', 18, 'Corriente', 'Inactivo', '2025-12-13 15:24:08.000000', NULL),
(25, 'jordan', '2025-07-06 23:13:34', 'hello@gmail.com', '$2y$10$F1JcwXO2xUwwmqAmlaMPjOeF2yjomgLdbW7r2me9Id/yPuUeRxrqe', 19, 'Corriente', 'Inactivo', NULL, NULL),
(29, 'Miguel Ibarvo', '2025-07-10 06:46:00', 'mixagg7@gmail.com', 'nigga', 19, 'Administrador', 'Activo', NULL, NULL),
(33, 'andrea', '2025-07-13 14:05:48', 'paula.barbosa.0524@gmail.com', '$2y$10$UIDeblbQr79z1eVSOi4yDOjVgRQiGm72jw0Ebqih.AlMEbAslv2/m', 19, 'Corriente', 'Inactivo', NULL, NULL),
(34, 'tomas', '2025-08-05 02:34:17', 'tomas@gmail.com', '$2y$10$c7pLA7eb8NCV6f.j.tKIP.hG29VkuME4R0tvPymSrVPgOp1JWuUnm', 20, 'Corriente', 'Activo', '2025-08-04 19:34:43.000000', NULL),
(37, 'Eva arroyo', '2025-12-06 10:30:44', 'evaarroyo862@gmail.com', '$2a$10$mJV3Vo8TQzAiHNd8L7TA7OyWjQAmThUDaG7bP38Uh.sI4RN4eRFjO', 19, 'Corriente', 'Activo', NULL, NULL),
(38, 'natsu', '2025-12-06 10:37:18', 'natsudragneel202013@gmail.com', '$2a$10$vXQJQ4RU/dKfcwSwM5ECYOG3NuWj7XRBP3FvyTZiAqdGkcu6O7XJ.', 25, 'Usuario', 'Activo', '2025-12-06 15:38:23.000000', NULL),
(39, 'Yeinner19', '2025-12-06 10:40:39', 'yeinnerramirez417@gmail.com', '$2a$10$MPUJuCAARqg5yRwS7BoyZu8slNnTPeiW6aoebt1nEm1QfuTrIRy9m', 18, 'Usuario', 'Activo', '2025-12-06 15:42:27.000000', NULL),
(40, 'MCuervo', '2025-12-06 11:01:42', 'patriciacc@gmail.com', '$2a$10$attHrGQi7XvuiB4C10Eb7.mk1aE56JCTGKDzVQZxUwka9Tnmd1Yuy', 0, 'Usuario', 'Activo', '2025-12-06 16:02:24.000000', NULL),
(43, 'mig ib', '2025-12-12 00:06:33', 'migi00823@gmail.com', 'GOOGLE_USER', 0, 'Corriente', 'Activo', '2025-12-13 11:53:22.000000', NULL),
(44, 'FurrryJordan', '2026-01-27 21:19:16', 'FurryloveMiguel@gmail.com', '$2a$10$787Pt7KD4t77kWhk9HkQNu71do2KNUNc3fRpNgPOxXQfpOaYjWkCe', 5, 'Usuario', 'Activo', '2026-01-28 02:43:08.000000', '/uploads/fotos_perfil/usuario_44_1769568163000_peterDiva.png.png');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `__efmigrationshistory`
--

CREATE TABLE `__efmigrationshistory` (
  `MigrationId` varchar(150) NOT NULL,
  `ProductVersion` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `__efmigrationshistory`
--

INSERT INTO `__efmigrationshistory` (`MigrationId`, `ProductVersion`) VALUES
('20251206032833_CreateIdentitySchema', '9.0.0');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `aspnetroleclaims`
--
ALTER TABLE `aspnetroleclaims`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `IX_AspNetRoleClaims_RoleId` (`RoleId`);

--
-- Indices de la tabla `aspnetroles`
--
ALTER TABLE `aspnetroles`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `RoleNameIndex` (`NormalizedName`);

--
-- Indices de la tabla `aspnetuserclaims`
--
ALTER TABLE `aspnetuserclaims`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `IX_AspNetUserClaims_UserId` (`UserId`);

--
-- Indices de la tabla `aspnetuserlogins`
--
ALTER TABLE `aspnetuserlogins`
  ADD PRIMARY KEY (`LoginProvider`,`ProviderKey`),
  ADD KEY `IX_AspNetUserLogins_UserId` (`UserId`);

--
-- Indices de la tabla `aspnetuserroles`
--
ALTER TABLE `aspnetuserroles`
  ADD PRIMARY KEY (`UserId`,`RoleId`),
  ADD KEY `IX_AspNetUserRoles_RoleId` (`RoleId`);

--
-- Indices de la tabla `aspnetusers`
--
ALTER TABLE `aspnetusers`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `UserNameIndex` (`NormalizedUserName`),
  ADD KEY `EmailIndex` (`NormalizedEmail`);

--
-- Indices de la tabla `aspnetusertokens`
--
ALTER TABLE `aspnetusertokens`
  ADD PRIMARY KEY (`UserId`,`LoginProvider`,`Name`);

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
-- Indices de la tabla `tareas_usuarios`
--
ALTER TABLE `tareas_usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_tarea_usuario` (`id_tarea`,`id_usuario`),
  ADD KEY `fk_usuario` (`id_usuario`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`IDusuario`);

--
-- Indices de la tabla `__efmigrationshistory`
--
ALTER TABLE `__efmigrationshistory`
  ADD PRIMARY KEY (`MigrationId`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `aspnetroleclaims`
--
ALTER TABLE `aspnetroleclaims`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `aspnetuserclaims`
--
ALTER TABLE `aspnetuserclaims`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `notificaciones`
--
ALTER TABLE `notificaciones`
  MODIFY `IDnotificacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=216;

--
-- AUTO_INCREMENT de la tabla `progresotarea`
--
ALTER TABLE `progresotarea`
  MODIFY `IDprogreso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `rutinas`
--
ALTER TABLE `rutinas`
  MODIFY `IDRutina` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `tareas`
--
ALTER TABLE `tareas`
  MODIFY `IDtarea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=203;

--
-- AUTO_INCREMENT de la tabla `tareas_usuarios`
--
ALTER TABLE `tareas_usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `IDusuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `aspnetroleclaims`
--
ALTER TABLE `aspnetroleclaims`
  ADD CONSTRAINT `FK_AspNetRoleClaims_AspNetRoles_RoleId` FOREIGN KEY (`RoleId`) REFERENCES `aspnetroles` (`Id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `aspnetuserclaims`
--
ALTER TABLE `aspnetuserclaims`
  ADD CONSTRAINT `FK_AspNetUserClaims_AspNetUsers_UserId` FOREIGN KEY (`UserId`) REFERENCES `aspnetusers` (`Id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `aspnetuserlogins`
--
ALTER TABLE `aspnetuserlogins`
  ADD CONSTRAINT `FK_AspNetUserLogins_AspNetUsers_UserId` FOREIGN KEY (`UserId`) REFERENCES `aspnetusers` (`Id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `aspnetuserroles`
--
ALTER TABLE `aspnetuserroles`
  ADD CONSTRAINT `FK_AspNetUserRoles_AspNetRoles_RoleId` FOREIGN KEY (`RoleId`) REFERENCES `aspnetroles` (`Id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_AspNetUserRoles_AspNetUsers_UserId` FOREIGN KEY (`UserId`) REFERENCES `aspnetusers` (`Id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `aspnetusertokens`
--
ALTER TABLE `aspnetusertokens`
  ADD CONSTRAINT `FK_AspNetUserTokens_AspNetUsers_UserId` FOREIGN KEY (`UserId`) REFERENCES `aspnetusers` (`Id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `tareas_usuarios`
--
ALTER TABLE `tareas_usuarios`
  ADD CONSTRAINT `fk_tarea` FOREIGN KEY (`id_tarea`) REFERENCES `tareas` (`IDtarea`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`IDusuario`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
