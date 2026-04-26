-- base de datos: plataforma_streaming
CREATE DATABASE IF NOT EXISTS plataforma_streaming;
USE plataforma_streaming;

-- 1.1 TABLA ROLES
CREATE TABLE ROLES IF NOT EXISTS (
    id_rol INT PRIMARY KEY AUTO_INCREMENT,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- 1.2 TABLA PERMISOS
CREATE TABLE PERMISOS IF NOT EXISTS (
    id_permiso INT PRIMARY KEY AUTO_INCREMENT,
    nombre_permiso VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- 1.3 TABLA ROL_PERMISO (relación M:N)
CREATE TABLE ROL_PERMISO IF NOT EXISTS (
    id_rol_permiso INT PRIMARY KEY AUTO_INCREMENT,
    id_rol INT NOT NULL,
    id_permiso INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES ROLES(id_rol) ON DELETE CASCADE,
    FOREIGN KEY (id_permiso) REFERENCES PERMISOS(id_permiso) ON DELETE CASCADE,
    UNIQUE KEY unique_rol_permiso (id_rol, id_permiso)
);

-- 1. TABLA USUARIOS
CREATE TABLE USUARIOS if NOT EXISTS (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    contraseña_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100),
    id_rol INT NOT NULL DEFAULT 2,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    FOREIGN KEY (id_rol) REFERENCES ROLES(id_rol) ON DELETE RESTRICT
);

-- 2. TABLA GENEROS
CREATE TABLE GENEROS if NOT EXISTS (
    id_genero INT PRIMARY KEY AUTO_INCREMENT,
    nombre_genero VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- 3. TABLA CONTENIDO
CREATE TABLE CONTENIDO if NOT EXISTS (
    id_contenido INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    año_lanzamiento INT,
    tipo ENUM('película', 'serie', 'documental', 'otro') DEFAULT 'película',
    duracion_minutos INT,
    calificacion DECIMAL(2,1) CHECK (calificacion >= 0 AND calificacion <= 10),
    url_imagen VARCHAR(500),
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. TABLA CONTENIDO_GENERO (relación M:N)
CREATE TABLE CONTENIDO_GENERO if NOT EXISTS (
    id_contenido_genero INT PRIMARY KEY AUTO_INCREMENT,
    id_contenido INT NOT NULL,
    id_genero INT NOT NULL,
    FOREIGN KEY (id_contenido) REFERENCES CONTENIDO(id_contenido) ON DELETE CASCADE,
    FOREIGN KEY (id_genero) REFERENCES GENEROS(id_genero) ON DELETE CASCADE,
    UNIQUE KEY unique_contenido_genero (id_contenido, id_genero)
);

-- 5. TABLA HISTORIAL_VISUALIZACION
CREATE TABLE HISTORIAL_VISUALIZACION if NOT EXISTS (
    id_visualizacion INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_contenido INT NOT NULL,
    fecha_visualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    minutos_visto INT DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_contenido) REFERENCES CONTENIDO(id_contenido) ON DELETE CASCADE,
    INDEX idx_usuario (id_usuario),
    INDEX idx_contenido (id_contenido),
    INDEX idx_fecha (fecha_visualizacion)
);

-- 6. TABLA CONTINUAR_VIENDO
CREATE TABLE CONTINUAR_VIENDO if NOT EXISTS (
    id_continuar INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_contenido INT NOT NULL UNIQUE,
    minutos_pausado INT DEFAULT 0,
    fecha_ultima_pausa TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_contenido) REFERENCES CONTENIDO(id_contenido) ON DELETE CASCADE,
    UNIQUE KEY unique_usuario_contenido (id_usuario, id_contenido),
    INDEX idx_usuario (id_usuario)
);

-- ============================================
-- INSERTS DE EJEMPLO
-- ============================================

-- Insertar roles
INSERT INTO ROLES (nombre_rol, descripcion) VALUES
('ADMIN', 'Administrador con acceso total'),
('USER', 'Usuario normal con acceso limitado');

-- Insertar permisos
INSERT INTO PERMISOS (nombre_permiso, descripcion) VALUES
('crear_contenido', 'Permiso para crear nuevo contenido'),
('editar_contenido', 'Permiso para editar contenido'),
('eliminar_contenido', 'Permiso para eliminar contenido'),
('ver_contenido', 'Permiso para ver contenido'),
('gestionar_usuarios', 'Permiso para gestionar usuarios'),
('ver_historial', 'Permiso para ver historial de otros'),
('editar_perfil', 'Permiso para editar su propio perfil'),
('eliminar_cuenta', 'Permiso para eliminar su cuenta'),
('ver_estadisticas', 'Permiso para ver estadísticas del sistema'),
('cambiar_rol', 'Permiso para cambiar rol de usuarios');

-- Asignar permisos a ADMIN (todos)
INSERT INTO ROL_PERMISO (id_rol, id_permiso) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10);

-- Asignar permisos a USER (básicos)
INSERT INTO ROL_PERMISO (id_rol, id_permiso) VALUES
(2, 4), (2, 7), (2, 8);

-- Insertar géneros de ejemplo
INSERT INTO GENEROS (nombre_genero, descripcion) VALUES
('Acción', 'Películas de alta adrenalina y aventura'),
('Drama', 'Películas narrativas profundas y emocionales'),
('Comedia', 'Películas humorísticas de entretenimiento'),
('Fantasía', 'Películas con elementos mágicos y sobrenaturales'),
('Ciencia Ficción', 'Películas futuristas con tecnología'),
('Terror', 'Películas de suspenso y miedo'),
('Documental', 'Contenido educativo y de investigación');

-- Insertar usuarios de ejemplo
INSERT INTO USUARIOS (nombre_usuario, email, contraseña_hash, nombre_completo, id_rol) VALUES
('juan123', 'juan@example.com', 'hash_seguro_1', 'Juan Pérez', 1),
('maria_lopez', 'maria@example.com', 'hash_seguro_2', 'María López', 2),
('carlos_dev', 'carlos@example.com', 'hash_seguro_3', 'Carlos Developer', 2);

-- Insertar contenido de ejemplo
INSERT INTO CONTENIDO (titulo, descripcion, año_lanzamiento, tipo, duracion_minutos, calificacion, url_imagen) VALUES
('Matrix', 'Un hacker descubre la verdad sobre su realidad', 1999, 'película', 136, 8.7, '/img/matrix.jpg'),
('Inception', 'Viajes dentro de sueños para robar ideas', 2010, 'película', 148, 8.8, '/img/inception.jpg'),
('Stranger Things', 'Misterios sobrenaturales en un pueblo', 2016, 'serie', 42, 8.7, '/img/stranger_things.jpg'),
('Planet Earth', 'Documental sobre la naturaleza del planeta', 2006, 'documental', 600, 9.4, '/img/planet_earth.jpg'),
('Deadpool', 'Superhéroe cómico y violento', 2016, 'película', 108, 8.0, '/img/deadpool.jpg');

-- Asociar contenido con géneros
INSERT INTO CONTENIDO_GENERO (id_contenido, id_genero) VALUES
(1, 4), (1, 5),  -- Matrix: Fantasía, Ciencia Ficción
(2, 4), (2, 5),  -- Inception: Fantasía, Ciencia Ficción
(3, 4), (3, 6),  -- Stranger Things: Fantasía, Terror
(4, 7),          -- Planet Earth: Documental
(5, 2), (5, 3);  -- Deadpool: Drama (acción/oscuro), Comedia

-- Insertar historial de visualización de ejemplo
INSERT INTO HISTORIAL_VISUALIZACION (id_usuario, id_contenido, minutos_visto) VALUES
(1, 1, 136),  -- Juan vio toda Matrix
(1, 2, 75),   -- Juan vio parte de Inception
(2, 3, 42),   -- María vio un episodio de Stranger Things
(3, 4, 300);  -- Carlos vio parte del documental

-- Insertar continuar viendo de ejemplo
INSERT INTO CONTINUAR_VIENDO (id_usuario, id_contenido, minutos_pausado) VALUES
(1, 2, 75),   -- Juan paró en minuto 75 de Inception
(2, 3, 38),   -- María paró en minuto 38 de Stranger Things
(3, 5, 45);   -- Carlos paró en minuto 45 de Deadpool
