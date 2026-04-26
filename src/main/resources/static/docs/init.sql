-- Base de datos para MediaStreaming
-- Script de inicialización

DROP DATABASE IF EXISTS mediastreaming;

CREATE DATABASE IF NOT EXISTS mediastreaming;
USE mediastreaming;

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    apodo VARCHAR(100) NOT NULL UNIQUE,
    tipo_usuario ENUM('admin', 'usuario')
);

-- Tabla de Contenido (Películas y Series)
CREATE TABLE IF NOT EXISTS contenido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT,
    tipo ENUM('pelicula', 'serie', 'documental') NOT NULL,
    url_archivo VARCHAR(255),
    duracion INT COMMENT 'Duración en minutos'
);

-- Tabla de Géneros
CREATE TABLE IF NOT EXISTS generos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- Tabla de relación Contenido-Géneros
CREATE TABLE IF NOT EXISTS contenido_generos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    contenido_id INT NOT NULL,
    genero_id INT NOT NULL,
    FOREIGN KEY (contenido_id) REFERENCES contenido(id),
    FOREIGN KEY (genero_id) REFERENCES generos(id),
    UNIQUE KEY unique_contenido_genero (contenido_id, genero_id)
);

-- Tabla de Visualización (Historial de vistas)
CREATE TABLE IF NOT EXISTS visualizacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    contenido_id INT NOT NULL,
    estado ENUM('viendo', 'completado') DEFAULT 'viendo',
    tiempo_visto INT COMMENT 'Segundos visualizados',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (contenido_id) REFERENCES contenido(id),
    UNIQUE KEY unique_usuario_contenido (usuario_id, contenido_id)
);

CREATE TABLE IF NOT EXISTS favoritos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    contenido_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (contenido_id) REFERENCES contenido(id),
    UNIQUE KEY unique_usuario_favorito (usuario_id, contenido_id)
);

-- Insertar datos de prueba 1 admin y 19 usuarios
INSERT INTO usuarios (correo, contrasena, apodo, tipo_usuario) VALUES
('admin@example.com', 'admin123', 'Admin', 'admin'),
('user@example.com', 'user123', 'User', 'usuario'),
('user2@example.com', 'user123', 'User2', 'usuario'),
('user3@example.com', 'user123', 'User3', 'usuario'),
('user4@example.com', 'user123', 'User4', 'usuario'),
('user5@example.com', 'user123', 'User5', 'usuario'),
('user6@example.com', 'user123', 'User6', 'usuario'),
('user7@example.com', 'user123', 'User7', 'usuario'),
('user8@example.com', 'user123', 'User8', 'usuario'),
('user9@example.com', 'user123', 'User9', 'usuario'),
('user10@example.com', 'user123', 'User10', 'usuario'),
('user11@example.com', 'user123', 'User11', 'usuario'),
('user12@example.com', 'user123', 'User12', 'usuario'),
('user13@example.com', 'user123', 'User13', 'usuario'),
('user14@example.com', 'user123', 'User14', 'usuario'),
('user15@example.com', 'user123', 'User15', 'usuario'),
('user16@example.com', 'user123', 'User16', 'usuario'),
('user17@example.com', 'user123', 'User17', 'usuario'),
('user18@example.com', 'user123', 'User18', 'usuario'),
('user19@example.com', 'user123', 'User19', 'usuario');

-- Insertar géneros de prueba
INSERT INTO generos (nombre, descripcion) VALUES
('Acción', 'Películas y series de acción con escenas emocionantes y dinámicas.'),
('Comedia', 'Contenido diseñado para hacer reír, con situaciones cómicas y humorísticas.'),
('Drama', 'Historias profundas y emocionales que exploran las complejidades humanas.'),
('Terror', 'Películas y series que buscan asustar o inquietar al espectador.'),
('Ciencia Ficción', 'Contenido que explora conceptos futuristas, tecnología avanzada y mundos imaginarios.'),
('Romance', 'Historias centradas en relaciones amorosas y emocionales entre personajes.'),
('Documental', 'Contenido basado en hechos reales, que busca informar o educar sobre un tema específico.');

-- Insertar contenido de prueba ejemplos de películas, series y documentales
INSERT INTO contenido (titulo, descripcion, tipo, duracion) VALUES
('La Gran Aventura', 'Una emocionante película de acción que sigue a un grupo de héroes en una misión peligrosa.', 'pelicula', 120),
('Risas Incontrolables', 'Una comedia hilarante que te hará reír de principio a fin.', 'pelicula', 90),
('Corazones Rotos', 'Un drama intenso que explora las complejidades de las relaciones humanas.', 'pelicula', 110),
('Noche de Terror', 'Una película de terror que te mantendrá al borde de tu asiento.', 'pelicula', 100),
('Futuro Imaginado', 'Una fascinante película de ciencia ficción que explora un mundo futurista lleno de tecnología avanzada.', 'pelicula', 130),
('Amor Eterno', 'Una conmovedora historia de romance que sigue a dos almas destinadas a estar juntas.', 'pelicula', 95),
('El Mundo Real', 'Un documental impactante que revela la verdad detrás de un tema controvertido.', 'documental', 80),
('La Serie de la Ciudad', 'Una serie de televisión que sigue las vidas entrelazadas de los habitantes de una ciudad vibrante.', 'serie', 45),
('Risas en la Oficina', 'Una comedia de situación que se desarrolla en un entorno de oficina, llena de personajes excéntricos y situaciones cómicas.', 'serie', 30),
('El Drama Familiar', 'Un drama que explora las complejidades de las relaciones familiares a través de varias generaciones.', 'serie', 50),
('Terror en la Casa Embrujada', 'Una serie de terror que sigue a un grupo de personas atrapadas en una casa embrujada llena de secretos oscuros.', 'serie', 40),
('Viaje al Espacio', 'Una serie de ciencia ficción que sigue a un equipo de astronautas en su misión para explorar el espacio profundo.', 'serie', 60),
('Amor y Desamor', 'Una serie romántica que sigue las vidas amorosas entrelazadas de varios personajes a lo largo del tiempo.', 'serie', 35),
('Documental sobre la Naturaleza', 'Un documental impresionante que muestra la belleza y diversidad del mundo natural.', 'documental', 70),
('La Historia del Arte', 'Un documental educativo que explora la evolución del arte a lo largo de la historia humana.', 'documental', 90),
('Misterios sin Resolver', 'Un documental intrigante que investiga algunos de los misterios más desconcertantes del mundo.', 'documental', 85);

-- Insertar relaciones contenido-géneros de prueba
INSERT INTO contenido_generos (contenido_id, genero_id) VALUES
(1, 1), -- La Gran Aventura - Acción
(2, 2), -- Risas Incontrolables - Comedia
(3, 3), -- Corazones Rotos - Drama
(4, 4), -- Noche de Terror - Terror
(5, 5), -- Futuro Imaginado - Ciencia Ficción
(6, 6), -- Amor Eterno - Romance
(7, 7), -- El Mundo Real - Documental
(8, 1), -- La Serie de la Ciudad - Acción
(9, 2), -- Risas en la Oficina - Comedia
(10, 3), -- El Drama Familiar - Drama
(11, 4), -- Terror en la Casa Embrujada - Terror
(12, 5), -- Viaje al Espacio - Ciencia Ficción
(13, 6), -- Amor y Desamor - Romance
(14, 7), -- Documental sobre la Naturaleza - Documental
(15, 7); -- La Historia del Arte - Documental

-- Insertar visualizaciones de prueba
INSERT INTO visualizacion (usuario_id, contenido_id, estado, tiempo_visto) VALUES
(2, 1, 'completado', 120), -- User - La Gran Aventura
(2, 2, 'completado', 90), -- User - Risas Incontrolables
(3, 3, 'viendo', 60), -- User2 - Corazones Rotos
(4, 4, 'completado', 100), -- User3 - Noche de Terror
(5, 5, 'viendo', 30), -- User4 - Futuro Imaginado
(6, 6, 'completado', 95), -- User5 - Amor Eterno
(7, 7, 'completado', 80), -- User6 - El Mundo Real
(8, 8, 'viendo', 20), -- User7 - La Serie de la Ciudad
(9, 9, 'completado', 30), -- User8 - Risas en la Oficina
(10, 10, 'viendo', 25), -- User9 - El Drama Familiar
(11, 11, 'completado', 40), -- User10 - Terror en la Casa Embrujada
(12, 12, 'viendo', 15), -- User11 - Viaje al Espacio
(13, 13, 'completado', 35), -- User12 - Amor y Desamor
(14, 14, 'completado', 70), -- User13 - Documental sobre la Naturaleza
(15, 15, 'viendo', 10); -- User14 - La Historia del Arte


-- Insertar favoritos de prueba
INSERT INTO favoritos (usuario_id, contenido_id) VALUES
(2, 1), -- User - La Gran Aventura
(2, 2), -- User - Risas Incontrolables
(3, 3), -- User2 - Corazones Rotos
(4, 4), -- User3 - Noche de Terror
(5, 5), -- User4 - Futuro Imaginado
(6, 6), -- User5 - Amor Eterno
(7, 7), -- User6 - El Mundo Real
(8, 8), -- User7 - La Serie de la Ciudad
(9, 9), -- User8 - Risas en la Oficina
(10, 10), -- User9 - El Drama Familiar
(11, 11), -- User10 - Terror en la Casa Embrujada
(12, 12), -- User11 - Viaje al Espacio
(13, 13), -- User12 - Amor y Desamor
(14, 14), -- User13 - Documental sobre la Naturaleza
(15, 15); -- User14 - La Historia del Arte