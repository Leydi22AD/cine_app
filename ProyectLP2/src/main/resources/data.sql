-- Usuarios iniciales del sistema
-- Contraseña: admin123 (en producción debería estar hasheada)
INSERT INTO usuario (id_usuario, nombre, email, password, rol) 
VALUES 
(1, 'Administrador', 'admin@cine.com', 'admin123', 'ADMIN'),
(2, 'Usuario Cliente', 'user@cine.com', 'user123', 'CLIENTE')
ON DUPLICATE KEY UPDATE nombre=nombre;
