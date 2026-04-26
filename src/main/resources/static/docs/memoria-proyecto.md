# Memoria del proyecto MediaStreaming

## 1. Introduccion

El proyecto **MediaStreaming** consiste en una aplicacion web desarrollada con **Spring Boot**, **Thymeleaf**, **MySQL**, HTML, CSS y Bootstrap. Su finalidad es simular una plataforma de streaming en la que existen dos tipos principales de usuarios: usuarios normales y administradores.

La aplicacion permite a los usuarios explorar contenido multimedia, consultar detalles, marcar favoritos y mantener un historial de visualizacion. Por otra parte, el administrador dispone de un panel propio desde el que puede gestionar usuarios, generos y contenidos mediante operaciones CRUD.

El proyecto se ha completado adaptando una estructura Spring Boot ya existente, ampliandola con nuevas vistas, controladores, servicios, repositorios, estilos responsive, documentacion tecnica y configuracion Docker.

## 2. Objetivos del proyecto

Los objetivos principales que marque para el proyecto:

- Crear una aplicacion web funcional de gestion y consumo de contenido multimedia.
- Diferenciar el acceso entre administradores y usuarios normales.
- Implementar autenticacion, registro y cierre de sesion.
- Permitir al usuario explorar contenido, filtrar por genero y buscar por titulo.
- Permitir al usuario marcar y desmarcar favoritos.
- Registrar contenido visto o en progreso dentro del historial.
- Implementar CRUD administrativo para contenidos, generos y usuarios.
- Permitir la subida de imagenes de portada para los contenidos.
- Adaptar la interfaz a movil, tableta y monitor mediante CSS responsive.
- Documentar el proyecto mediante diagramas de secuencia y memoria tecnica.
- Preparar el proyecto para su ejecucion con Docker y MySQL.

## 3. Tecnologias utilizadas

El proyecto utiliza las siguientes tecnologias:

- **Java 21** como lenguaje principal.
- **Spring Boot 4** como framework de desarrollo.
- **Spring MVC** para la gestion de rutas y controladores.
- **Spring Data JPA** para el acceso a datos.
- **Thymeleaf** para la generacion de vistas HTML dinamicas.
- **MySQL** como sistema gestor de base de datos.
- **Bootstrap 5** como apoyo visual en formularios, tablas y modales.
- **CSS propio** para el diseno de las zonas de usuario, administrador y paginas generales.
- **Docker y Docker Compose** para facilitar el despliegue de la aplicacion junto con la base de datos.
- **Mermaid** para la documentacion de diagramas de secuencia.

## 4. Estructura general del proyecto

La aplicacion sigue una organizacion por capas:

- **Controller**: contiene los controladores encargados de recibir las peticiones HTTP y devolver las vistas correspondientes.
- **Service**: contiene la logica de negocio de usuarios, contenidos, generos, favoritos y visualizaciones.
- **Repository**: contiene las interfaces JPA para acceder a la base de datos.
- **Model**: contiene las entidades del dominio, como `Usuario`, `Contenido`, `Genero`, `Favorito` y `Visualizacion`.
- **templates**: contiene las vistas Thymeleaf separadas entre zona publica, zona de usuario y zona de administrador.
- **static/css**: contiene los estilos de la aplicacion.
- **static/img**: contiene el logo y las imagenes de contenido.
- **static/docs**: contiene documentacion, diagramas y scripts SQL.

## 5. Funcionamiento general

Al acceder a la aplicacion, el visitante puede entrar a la pantalla de inicio y posteriormente iniciar sesion o registrarse. Durante el registro se comprueba que las contrasenas coincidan, que tengan una longitud minima y que no exista otro usuario con el mismo correo o apodo.

Cuando el usuario inicia sesion, el sistema consulta sus credenciales en la base de datos. Si son correctas, se guarda el usuario en la sesion HTTP y se redirige segun su tipo:

- Si el usuario es **ADMIN**, se redirige a `/admin/dashboard`.
- Si el usuario es **USUARIO**, se redirige a `/usuario/dashboard`.

Este control de sesion se repite en las rutas privadas. Si no existe una sesion valida, el sistema redirige al login.

## 6. Zona de usuario

La zona de usuario esta pensada para el consumo del contenido de la plataforma.

### Dashboard de usuario

En `/usuario/dashboard`, el usuario puede ver:

- Contenidos recomendados.
- Contenidos que ha comenzado a ver.
- Accesos rapidos a explorar contenido, historial y favoritos.

### Exploracion de contenido

En `/usuario/contenido`, el usuario puede:

- Ver todos los contenidos disponibles.
- Buscar por titulo.
- Filtrar por genero.
- Ver si un contenido esta marcado como favorito.
- Acceder al detalle de cada contenido.

### Detalle de contenido

Al pulsar en un contenido, se registra o actualiza una visualizacion y se redirige al detalle. En esta pantalla se muestra:

- Titulo.
- Descripcion.
- Tipo.
- Duracion.
- Generos asociados.
- Imagen de portada.
- Boton para agregar o quitar de favoritos.

### Favoritos

La seccion de favoritos permite consultar los contenidos que el usuario ha marcado. El sistema puede agregar o eliminar favoritos dependiendo de si ya existia previamente la relacion entre usuario y contenido.

### Historial

El historial muestra los contenidos que el usuario ha visualizado o comenzado a visualizar. Esta funcionalidad se apoya en la entidad `Visualizacion`.

## 7. Zona de administrador

La zona de administrador permite gestionar la informacion principal de la plataforma.

### Dashboard de administrador

En `/admin/dashboard`, el administrador ve indicadores generales como:

- Total de usuarios.
- Total de contenidos.
- Total de visualizaciones.
- Total de favoritos.

Tambien cuenta con enlaces a las secciones de gestion.

### CRUD de contenido

En `/admin/contenido`, el administrador puede:

- Crear contenido.
- Editar contenido existente.
- Eliminar contenido.
- Asignar generos.
- Subir imagen de portada.

La imagen se sube mediante un formulario `multipart/form-data`. El archivo se guarda en la ruta configurada mediante `media.upload-dir`, actualmente `uploads/img_contenidos`, una carpeta ubicada fuera de `src` pero dentro del proyecto. En la base de datos se guarda el nombre del archivo para que luego pueda mostrarse desde las vistas de usuario mediante la URL publica `/img/img_contenidos/...`.

El campo `urlArchivo` se dejo como opcional en el backend para no romper datos o estructuras anteriores, aunque la gestion visible del contenido se centra actualmente en la imagen de portada.

### CRUD de generos

En `/admin/generos`, el administrador puede:

- Crear generos.
- Editar nombre y descripcion.
- Eliminar generos.

Antes de eliminar un genero, el sistema lo desvincula de los contenidos asociados para evitar errores de integridad referencial.

### CRUD de usuarios

En `/admin/usuarios`, el administrador puede:

- Crear usuarios.
- Editar apodo, correo, contrasena y tipo de usuario.
- Eliminar usuarios.

El sistema valida que no se repitan correos ni apodos. Ademas, antes de eliminar un usuario se borran sus favoritos y visualizaciones asociadas. Tambien se evita que el administrador elimine su propia cuenta mientras tiene la sesion activa.

## 8. Base de datos

La base de datos principal se llama `mediastreaming`. El script de inicializacion se encuentra en:

`src/main/resources/static/docs/init.sql`

Las tablas principales son:

- `usuarios`: almacena los datos de acceso y tipo de usuario.
- `contenido`: almacena peliculas, series o documentales.
- `generos`: almacena los generos disponibles.
- `contenido_generos`: relacion muchos a muchos entre contenido y generos.
- `visualizacion`: almacena el historial o progreso de visualizacion.
- `favoritos`: almacena los contenidos favoritos por usuario.

El script incluye datos de prueba, entre ellos un usuario administrador y usuarios normales para facilitar las pruebas iniciales.

## 9. Interfaz y diseno responsive

La aplicacion cuenta con estilos separados para:

- Zona de administrador: `admin.css`.
- Zona de usuario: `usuario.css`.
- Paginas generales, login e index: `styles.css`.

Se definieron tres tramos responsive basados en el ancho de pantalla:

- **Movil**: hasta `767px`.
- **Tableta**: de `768px` a `1199px`.
- **Monitor**: desde `1200px`.

En movil, los menus se apilan, las tablas tienen desplazamiento horizontal y los formularios ocupan todo el ancho disponible. En tableta se usan distribuciones intermedias, normalmente de dos columnas. En monitor se mantiene una distribucion amplia y comoda para paneles, tablas y grids de contenido.

Tambien se sustituyo el texto de cabecera por el logo ubicado en la carpeta `static/img`, manteniendo el texto alternativo para accesibilidad.

## 10. Dockerizacion

El proyecto incluye archivos Docker para facilitar su ejecucion:

- `Dockerfile`: compila la aplicacion con Maven y la ejecuta con Java 21.
- `docker-compose.yml`: levanta la aplicacion y una base de datos MySQL.
- `.dockerignore`: evita copiar archivos innecesarios al contexto de Docker.

El comando principal para levantar el entorno es:

```bash
docker compose up --build
```

El servicio web queda disponible en:

`http://localhost:8080`

La base de datos MySQL se expone en el puerto `3306`, usando la base `mediastreaming` y el usuario `root`.

## 11. Documentacion tecnica

Ademas de esta memoria, se genero un documento de diagramas de secuencia en:

`src/main/resources/static/docs/diagramas-secuencia.md`

Este documento incluye diagramas Mermaid para:

- Inicio de sesion.
- Registro.
- Dashboard de usuario.
- Exploracion de contenido.
- Detalle de contenido.
- Favoritos.
- CRUD de contenido.
- CRUD de usuarios.
- CRUD de generos.
- Logout.

Estos diagramas ayudan a comprender el flujo de comunicacion entre usuario, vistas, controladores, servicios, repositorios y base de datos.

## 12. Complicaciones y factores externos

Durante el desarrollo del proyecto surgieron varias complicaciones externas que afectaron al ritmo normal de trabajo.

### Perdida de la plantilla original

Una de las principales dificultades fue la perdida de la plantilla original sobre la que se iba a trabajar. Esto obligo a reconstruir partes de la interfaz y adaptar la estructura visual desde los archivos disponibles en el proyecto. Como consecuencia, fue necesario rehacer vistas, reorganizar formularios y asegurar que las nuevas plantillas mantuvieran coherencia con la estructura Spring Boot y Thymeleaf existente.

Esta incidencia tambien afecto al tiempo de desarrollo, ya que parte del trabajo consistio en recuperar funcionalidad y apariencia antes de poder avanzar con nuevas caracteristicas.

### Adaptacion progresiva del CRUD

El CRUD administrativo no estaba completamente conectado al principio. Las vistas contenian botones de editar, agregar o eliminar, pero estos no tenian todavia rutas funcionales ni formularios asociados. Fue necesario implementar endpoints POST, servicios transaccionales, validaciones y modales de Bootstrap para completar la funcionalidad.

Tambien se ajusto el CRUD de contenido para pasar de un campo manual de imagen a una subida real de archivo, usando la ruta de carga establecida en la configuracion del proyecto.

### Configuracion de Java en el entorno

Durante algunas pruebas, la terminal no reconocia Java porque `JAVA_HOME` no estaba configurado correctamente. Esto impidio ejecutar `mvn test` desde ese entorno concreto. La solucion planteada fue configurar un JDK 21, definir `JAVA_HOME` y agregar `%JAVA_HOME%\bin` al `Path`.

Aunque las pruebas se podian ejecutar desde otro entorno, esta situacion afecto a la verificacion directa desde la terminal inicial.

### Estado de salud afectado por el cambio de tiempo

Otro factor externo que influyo en el avance del proyecto fue el estado de salud afectado por el cambio de tiempo. Esta situacion redujo la disponibilidad y la capacidad de mantener un ritmo constante durante algunas fases del desarrollo. A pesar de ello, se continuo avanzando por bloques, priorizando primero las funcionalidades principales y posteriormente la documentacion, el diseno responsive y la preparacion Docker.

## 13. Posibles mejoras futuras

Algunas mejoras posibles para futuras versiones son:

- Anadir cifrado de contrasenas.
- Sustituir el control manual de sesion por Spring Security.
- Implementar paginacion en tablas administrativas.
- Permitir eliminar imagenes antiguas cuando se reemplaza la portada de un contenido.
- Crear roles y permisos mas detallados.

## 14. Conclusion

MediaStreaming es una aplicacion web funcional que integra autenticacion, gestion de usuarios, gestion de contenidos, generos, favoritos, historial y paneles diferenciados para usuario y administrador.

El proyecto evoluciono desde una estructura inicial incompleta hasta una aplicacion con CRUD administrativo, subida de imagenes, diseno responsive, documentacion tecnica y soporte Docker. A pesar de las complicaciones externas, como la perdida de la plantilla original, los problemas de configuracion del entorno Java y el impacto del cambio de tiempo en la salud, se logro consolidar una base funcional y ampliable para una plataforma de streaming.
