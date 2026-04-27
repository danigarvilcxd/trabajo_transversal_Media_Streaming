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

-- Las portadas se guardan en url_archivo.
INSERT INTO contenido (titulo, descripcion, tipo, url_archivo, duracion) VALUES
('Mad Max: Fury Road', 'En un futuro postapocalíptico, Max se une a Furiosa para huir de un tirano y atravesar el desierto en una persecución brutal.', 'pelicula', 'mad-max-fury-road.jpg', 120),
('Superbad', 'Dos amigos intentan disfrutar al máximo su última etapa de instituto antes de separarse al ir a la universidad.', 'pelicula', 'superbad.png', 113),
('El Padrino', 'La familia Corleone se mueve entre lealtades, poder y violencia dentro del crimen organizado de Nueva York.', 'pelicula', 'el-padrino.jpg', 175),
('El Exorcista', 'Una madre busca ayuda cuando su hija empieza a mostrar signos de una posesión aterradora.', 'pelicula', 'el-exorcista.jpg', 122),
('Blade Runner 2049', 'Un blade runner descubre un secreto que puede cambiar el equilibrio entre humanos y replicantes.', 'pelicula', 'blade-runner-2049.png', 164),
('Titanic', 'Un romance imposible nace a bordo del transatlántico más famoso de la historia durante su viaje inaugural.', 'pelicula', 'titanic.png', 195),
('John Wick', 'Un antiguo asesino regresa al mundo criminal para vengarse de quienes le arrebataron lo que más quería.', 'pelicula', 'john-wick.png', 101),
('La La Land', 'Una actriz y un pianista persiguen sus sueños en Los Ángeles mientras su relación se pone a prueba.', 'pelicula', 'la-la-land.png', 128),
('Stranger Things', 'Un grupo de niños descubre fuerzas sobrenaturales y secretos del gobierno tras la desaparición de un amigo.', 'serie', 'stranger-things.png', 50),
('The Office', 'Los empleados de una oficina de ventas de papel viven situaciones absurdas bajo la dirección de un jefe muy peculiar.', 'serie', 'the-office.png', 22),
('Breaking Bad', 'Un profesor de química con cáncer empieza a fabricar metanfetamina y se adentra en un mundo cada vez más peligroso.', 'serie', 'breaking-bad.png', 47),
('The Haunting of Hill House', 'Una familia marcada por una casa embrujada debe enfrentarse a los traumas de su pasado.', 'serie', 'hill-house.jpg', 55),
('Black Mirror', 'Historias independientes exploran el lado oscuro de la tecnología y su impacto en la sociedad.', 'serie', 'black-mirror.png', 60),
('Friends', 'Seis amigos comparten amor, trabajo y momentos inolvidables en Nueva York.', 'serie', 'friends.png', 22),
('Game of Thrones', 'Varias familias nobles luchan por el control del Trono de Hierro mientras una amenaza antigua despierta en el norte.', 'serie', 'game-of-thrones.png', 57),
('The Mandalorian', 'Un cazarrecompensas solitario protege a un misterioso niño mientras recorre los límites de la galaxia.', 'serie', 'the-mandalorian.png', 40);

-- Actualizar portadas si la base ya tenia los contenidos creados.
UPDATE contenido SET url_archivo = 'mad-max-fury-road.jpg' WHERE titulo = 'Mad Max: Fury Road';
UPDATE contenido SET url_archivo = 'superbad.png' WHERE titulo = 'Superbad';
UPDATE contenido SET url_archivo = 'el-padrino.jpg' WHERE titulo = 'El Padrino';
UPDATE contenido SET url_archivo = 'el-exorcista.jpg' WHERE titulo = 'El Exorcista';
UPDATE contenido SET url_archivo = 'blade-runner-2049.png' WHERE titulo = 'Blade Runner 2049';
UPDATE contenido SET url_archivo = 'titanic.png' WHERE titulo = 'Titanic';
UPDATE contenido SET url_archivo = 'john-wick.png' WHERE titulo = 'John Wick';
UPDATE contenido SET url_archivo = 'la-la-land.png' WHERE titulo = 'La La Land';
UPDATE contenido SET url_archivo = 'stranger-things.png' WHERE titulo = 'Stranger Things';
UPDATE contenido SET url_archivo = 'the-office.png' WHERE titulo = 'The Office';
UPDATE contenido SET url_archivo = 'breaking-bad.png' WHERE titulo = 'Breaking Bad';
UPDATE contenido SET url_archivo = 'hill-house.jpg' WHERE titulo = 'The Haunting of Hill House';
UPDATE contenido SET url_archivo = 'black-mirror.png' WHERE titulo = 'Black Mirror';
UPDATE contenido SET url_archivo = 'friends.png' WHERE titulo = 'Friends';
UPDATE contenido SET url_archivo = 'game-of-thrones.png' WHERE titulo = 'Game of Thrones';
UPDATE contenido SET url_archivo = 'the-mandalorian.png' WHERE titulo = 'The Mandalorian';

-- Insertar relaciones contenido-géneros de prueba
INSERT INTO contenido_generos (contenido_id, genero_id) VALUES
(1, 1), -- Mad Max: Fury Road - Acción
(2, 2), -- Superbad - Comedia
(3, 3), -- El Padrino - Drama
(4, 4), -- El Exorcista - Terror
(5, 5), -- Blade Runner 2049 - Ciencia Ficción
(6, 6), -- Titanic - Romance
(7, 1), -- John Wick - Acción
(8, 6), -- La La Land - Romance
(9, 5), -- Stranger Things - Ciencia Ficción
(10, 2), -- The Office - Comedia
(11, 3), -- Breaking Bad - Drama
(12, 4), -- The Haunting of Hill House - Terror
(13, 5), -- Black Mirror - Ciencia Ficción
(14, 2), -- Friends - Comedia
(15, 3), -- Game of Thrones - Drama
(16, 5); -- The Mandalorian - Ciencia Ficción

-- Insertar visualizaciones de prueba
INSERT INTO visualizacion (usuario_id, contenido_id, estado, tiempo_visto) VALUES
(2, 1, 'completado', 120), -- User - Mad Max: Fury Road
(2, 2, 'completado', 90), -- User - Superbad
(3, 3, 'viendo', 60), -- User2 - El Padrino
(4, 4, 'completado', 100), -- User3 - El Exorcista
(5, 5, 'viendo', 30), -- User4 - Blade Runner 2049
(6, 6, 'completado', 95), -- User5 - Titanic
(7, 7, 'completado', 80), -- User6 - John Wick
(8, 8, 'viendo', 20), -- User7 - La La Land
(9, 9, 'completado', 30), -- User8 - Stranger Things
(10, 10, 'viendo', 25), -- User9 - The Office
(11, 11, 'completado', 40), -- User10 - Breaking Bad
(12, 12, 'viendo', 15), -- User11 - The Haunting of Hill House
(13, 13, 'completado', 35), -- User12 - Black Mirror
(14, 14, 'completado', 70), -- User13 - Friends
(15, 15, 'viendo', 10); -- User14 - Game of Thrones


-- Insertar favoritos de prueba
INSERT INTO favoritos (usuario_id, contenido_id) VALUES
(2, 1), -- User - Mad Max: Fury Road
(2, 2), -- User - Superbad
(3, 3), -- User2 - El Padrino
(4, 4), -- User3 - El Exorcista
(5, 5), -- User4 - Blade Runner 2049
(6, 6), -- User5 - Titanic
(7, 7), -- User6 - John Wick
(8, 8), -- User7 - La La Land
(9, 9), -- User8 - Stranger Things
(10, 10), -- User9 - The Office
(11, 11), -- User10 - Breaking Bad
(12, 12), -- User11 - The Haunting of Hill House
(13, 13), -- User12 - Black Mirror
(14, 14), -- User13 - Friends
(15, 15); -- User14 - Game of Thrones
