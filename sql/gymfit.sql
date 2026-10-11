-- =====================================================================
--  GymFit · Script de base de datos para MySQL 8
--  Curso integrador I (UTP) · Semana 9 · Sesión 18 · Taller de arquitectura
--
--  CÓMO EJECUTARLO EN MYSQL WORKBENCH
--  1. Abre Workbench y entra a "Local instance MySQL80" con el usuario root.
--  2. File > Open SQL Script...  y elige este archivo (gymfit.sql).
--  3. Pulsa el rayo (Execute, Ctrl+Shift+Enter) para ejecutar TODO el script.
--  4. En el panel Navigator pulsa el botón de refrescar: aparece la base "gymfit".
--
--  ¡OJO! El script BORRA la base gymfit si ya existe y la crea de nuevo.
-- =====================================================================

DROP DATABASE IF EXISTS gymfit;
CREATE DATABASE gymfit CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE gymfit;

-- ---------------------------------------------------------------------
-- 1. TABLAS (modelo de la sesión 12: plan, socio, membresia, cuota,
--    entrenador y rutina) + usuario para el inicio de sesión
-- ---------------------------------------------------------------------

CREATE TABLE plan (
    id_plan        INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(40)   NOT NULL UNIQUE,
    periodicidad   ENUM('MENSUAL','TRIMESTRAL','ANUAL') NOT NULL,
    precio         DECIMAL(8,2)  NOT NULL CHECK (precio > 0),
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE socio (
    id_socio       INT AUTO_INCREMENT PRIMARY KEY,
    dni            CHAR(8)       NOT NULL UNIQUE,
    nombres        VARCHAR(60)   NOT NULL,
    apellidos      VARCHAR(60)   NOT NULL,
    telefono       VARCHAR(15),
    email          VARCHAR(80)   UNIQUE,
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla intermedia: conserva la historia cuando el socio cambia de plan
CREATE TABLE membresia (
    id_membresia   INT AUTO_INCREMENT PRIMARY KEY,
    id_socio       INT           NOT NULL,
    id_plan        INT           NOT NULL,
    fecha_inicio   DATE          NOT NULL,
    fecha_fin      DATE          NOT NULL,
    estado         ENUM('ACTIVA','VENCIDA','CANCELADA') NOT NULL DEFAULT 'ACTIVA',
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_membresia_socio FOREIGN KEY (id_socio) REFERENCES socio (id_socio),
    CONSTRAINT fk_membresia_plan  FOREIGN KEY (id_plan)  REFERENCES plan (id_plan)
);

-- La tabla que usa "Registrar pago" (clase Cuota en Java)
CREATE TABLE cuota (
    id_cuota          INT AUTO_INCREMENT PRIMARY KEY,
    id_membresia      INT           NOT NULL,
    monto             DECIMAL(8,2)  NOT NULL CHECK (monto > 0),
    fecha_vencimiento DATE          NOT NULL,
    estado            ENUM('PENDIENTE','PAGADA','ANULADA') NOT NULL DEFAULT 'PENDIENTE',
    medio_pago        ENUM('EFECTIVO','TARJETA','TRANSFERENCIA') NULL,
    fecha_pago        DATETIME      NULL,
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cuota_membresia FOREIGN KEY (id_membresia) REFERENCES membresia (id_membresia)
);

CREATE TABLE entrenador (
    id_entrenador  INT AUTO_INCREMENT PRIMARY KEY,
    nombres        VARCHAR(60)   NOT NULL,
    apellidos      VARCHAR(60)   NOT NULL,
    especialidad   VARCHAR(60),
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rutina (
    id_rutina      INT AUTO_INCREMENT PRIMARY KEY,
    id_socio       INT           NOT NULL,
    id_entrenador  INT           NOT NULL,
    objetivo       VARCHAR(100)  NOT NULL,
    fecha_inicio   DATE          NOT NULL,
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rutina_socio      FOREIGN KEY (id_socio)      REFERENCES socio (id_socio),
    CONSTRAINT fk_rutina_entrenador FOREIGN KEY (id_entrenador) REFERENCES entrenador (id_entrenador)
);

-- Usuarios del sistema. La contraseña se guarda como hash BCrypt (60 caracteres),
-- nunca en texto plano. Lo usa la clase UsuarioDAOMySQL.
CREATE TABLE usuario (
    id_usuario     INT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(30)   NOT NULL UNIQUE,
    password_hash  CHAR(60)      NOT NULL,
    nombre         VARCHAR(80)   NOT NULL,
    rol            ENUM('ADMIN','ASESOR','ENTRENADOR') NOT NULL,
    activo         TINYINT(1)    NOT NULL DEFAULT 1,
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- 2. DATOS DE PRUEBA
-- ---------------------------------------------------------------------

INSERT INTO plan (nombre, periodicidad, precio) VALUES
    ('Básico', 'MENSUAL',  89.00),
    ('Plus',   'MENSUAL', 129.00),
    ('Premium','MENSUAL', 179.00);

INSERT INTO socio (dni, nombres, apellidos, telefono, email) VALUES
    ('45871236', 'Luis',   'Vega Díaz',     '949112233', 'luis.vega@correo.pe'),
    ('70123456', 'María',  'Ríos Paredes',  '956778899', 'maria.rios@correo.pe'),
    ('46550912', 'Jorge',  'Castillo León', '944556677', 'jorge.castillo@correo.pe');

INSERT INTO membresia (id_socio, id_plan, fecha_inicio, fecha_fin) VALUES
    (1, 2, '2026-09-21', '2027-09-20'),   -- Luis  · Plus
    (2, 1, '2026-08-01', '2027-07-31'),   -- María · Básico
    (3, 3, '2026-09-01', '2027-08-31');   -- Jorge · Premium

-- Cuota 1 es la del caso del taller: Luis, S/ 129.00, pendiente
INSERT INTO cuota (id_membresia, monto, fecha_vencimiento, estado, medio_pago, fecha_pago) VALUES
    (1, 129.00, '2026-10-21', 'PENDIENTE', NULL, NULL),
    (1, 129.00, '2026-11-21', 'PENDIENTE', NULL, NULL),
    (2,  89.00, '2026-09-01', 'PAGADA',    'EFECTIVO', '2026-09-01 10:15:00'),
    (2,  89.00, '2026-10-01', 'PENDIENTE', NULL, NULL),
    (3, 179.00, '2026-10-01', 'PENDIENTE', NULL, NULL);

INSERT INTO entrenador (nombres, apellidos, especialidad) VALUES
    ('Pedro', 'Salas Ruiz', 'Musculación');

INSERT INTO rutina (id_socio, id_entrenador, objetivo, fecha_inicio) VALUES
    (1, 1, 'Ganar masa muscular', '2026-09-22');

-- Contraseñas de prueba (solo para el laboratorio):
--   carla / Carla2026*  -> ASESOR      (puede registrar pagos)
--   admin / Admin2026*  -> ADMIN       (puede registrar pagos)
--   pedro / Pedro2026*  -> ENTRENADOR  (NO puede registrar pagos: regla R3)
INSERT INTO usuario (username, password_hash, nombre, rol) VALUES
    ('carla', '$2a$10$OQiP8xqsyZNmrOwrUNX7ieU3jMau0AJCJiXG3oLwt6kAZjBGsO.Pi', 'Carla Mendoza',  'ASESOR'),
    ('admin', '$2a$10$Vk2TfR1T.Ni5d06vC.CsV.YPwmPHTM/xyB83bs/lH..apBwaZKxOS', 'Administrador', 'ADMIN'),
    ('pedro', '$2a$10$8E7NkJxdX8gdMBUSFz49a.CCLE1zrSNRalRleW7QwhBokn1Odu4Tm', 'Pedro Salas',    'ENTRENADOR');

-- ---------------------------------------------------------------------
-- 3. SEGURIDAD: usuario de la aplicación con MÍNIMO PRIVILEGIO
--    Java NO se conecta como root: usa app_gymfit, que solo puede
--    leer, insertar y actualizar (no puede borrar tablas ni datos).
--    La clave debe coincidir con db.password en db.properties.
-- ---------------------------------------------------------------------

DROP USER IF EXISTS 'app_gymfit'@'localhost';
CREATE USER 'app_gymfit'@'localhost' IDENTIFIED BY 'GymFit#2026';
GRANT SELECT, INSERT, UPDATE ON gymfit.* TO 'app_gymfit'@'localhost';
FLUSH PRIVILEGES;

-- ---------------------------------------------------------------------
-- 4. VERIFICACIÓN (ejecuta estas consultas y revisa el resultado)
-- ---------------------------------------------------------------------

-- Debe mostrar 7 tablas
SHOW TABLES;

-- Cuotas pendientes con el nombre del socio y su plan
SELECT c.id_cuota, CONCAT(s.nombres, ' ', s.apellidos) AS socio, p.nombre AS plan,
       c.monto, c.fecha_vencimiento, c.estado
FROM cuota c
JOIN membresia m ON m.id_membresia = c.id_membresia
JOIN socio s     ON s.id_socio     = m.id_socio
JOIN plan p      ON p.id_plan      = m.id_plan
WHERE c.estado = 'PENDIENTE'
ORDER BY c.fecha_vencimiento;

-- Permisos del usuario de la aplicación
SHOW GRANTS FOR 'app_gymfit'@'localhost';
