-- Solo para Docker: Java se conecta desde fuera del contenedor,
-- así que el usuario debe aceptar conexiones desde cualquier host ('%').
-- Mantiene el mínimo privilegio: SELECT, INSERT, UPDATE.
CREATE USER IF NOT EXISTS 'app_gymfit'@'%' IDENTIFIED BY 'GymFit#2026';
GRANT SELECT, INSERT, UPDATE ON gymfit.* TO 'app_gymfit'@'%';
FLUSH PRIVILEGES;