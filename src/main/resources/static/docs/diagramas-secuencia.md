# Diagramas de secuencia - MediaStreaming

## 1. Inicio de sesion

```mermaid
sequenceDiagram
    actor Usuario
    participant Vista as login.html
    participant AuthController
    participant UsuarioService
    participant UsuarioRepository
    participant Session as HttpSession

    Usuario->>Vista: Introduce correo y contrasena
    Vista->>AuthController: POST /auth/login
    AuthController->>UsuarioService: autenticar(correo, contrasena)
    UsuarioService->>UsuarioRepository: findByCorreo(correo)
    UsuarioRepository-->>UsuarioService: Usuario / vacio
    UsuarioService-->>AuthController: Usuario autenticado / null

    alt credenciales validas y usuario admin
        AuthController->>Session: setAttribute(usuario, usuarioId)
        AuthController-->>Usuario: redirect:/admin/dashboard
    else credenciales validas y usuario normal
        AuthController->>Session: setAttribute(usuario, usuarioId)
        AuthController-->>Usuario: redirect:/usuario/dashboard
    else credenciales invalidas
        AuthController-->>Vista: login con mensaje de error
    end
```

## 2. Registro de usuario

```mermaid
sequenceDiagram
    actor Visitante
    participant Vista as login.html
    participant AuthController
    participant UsuarioService
    participant UsuarioRepository

    Visitante->>Vista: Completa formulario de registro
    Vista->>AuthController: POST /auth/register

    alt contrasenas no coinciden o son cortas
        AuthController-->>Vista: login con mensaje de error
    else datos validos
        AuthController->>UsuarioService: registrar(apodo, correo, contrasena)
        UsuarioService->>UsuarioRepository: existsByCorreo(correo)
        UsuarioService->>UsuarioRepository: existsByApodo(apodo)

        alt correo o apodo existentes
            UsuarioService-->>AuthController: null
            AuthController-->>Vista: login con mensaje de error
        else usuario disponible
            UsuarioService->>UsuarioRepository: save(nuevoUsuario)
            UsuarioRepository-->>UsuarioService: Usuario guardado
            UsuarioService-->>AuthController: Usuario creado
            AuthController-->>Vista: login con mensaje de exito
        end
    end
```

## 3. Dashboard de usuario

```mermaid
sequenceDiagram
    actor Usuario
    participant UsuarioController
    participant Session as HttpSession
    participant VisualizacionService
    participant ContenidoService
    participant Vista as usuario/dashboard.html

    Usuario->>UsuarioController: GET /usuario/dashboard
    UsuarioController->>Session: getAttribute(usuario)

    alt no hay sesion o no es USUARIO
        UsuarioController-->>Usuario: redirect:/login
    else sesion valida
        UsuarioController->>VisualizacionService: obtenerContinuarViendo(usuario)
        VisualizacionService-->>UsuarioController: lista de visualizaciones
        UsuarioController->>ContenidoService: obtenerRecomendados()
        ContenidoService-->>UsuarioController: lista de contenidos
        UsuarioController-->>Vista: usuario, continuarViendo, recomendados
    end
```

## 4. Explorar y filtrar contenido

```mermaid
sequenceDiagram
    actor Usuario
    participant UsuarioController
    participant Session as HttpSession
    participant GeneroService
    participant ContenidoService
    participant FavoritoService
    participant Vista as usuario/contenido.html

    Usuario->>UsuarioController: GET /usuario/contenido?genero=&busqueda=
    UsuarioController->>Session: getAttribute(usuario)

    alt no hay sesion o no es USUARIO
        UsuarioController-->>Usuario: redirect:/login
    else sesion valida
        UsuarioController->>GeneroService: obtenerTodos()
        GeneroService-->>UsuarioController: generos
        UsuarioController->>ContenidoService: buscar(genero, busqueda)
        ContenidoService-->>UsuarioController: contenidos filtrados
        UsuarioController->>FavoritoService: obtenerIdsFavoritos(usuario)
        FavoritoService-->>UsuarioController: ids favoritos
        UsuarioController-->>Vista: generos, contenidos, favoritosIds
    end
```

## 5. Ver detalle de contenido

```mermaid
sequenceDiagram
    actor Usuario
    participant UsuarioController
    participant Session as HttpSession
    participant ContenidoService
    participant VisualizacionService
    participant FavoritoService
    participant Vista as usuario/detalle.html

    Usuario->>UsuarioController: GET /usuario/contenido/ver/{id}
    UsuarioController->>Session: getAttribute(usuario)
    UsuarioController->>ContenidoService: obtenerPorId(id)
    ContenidoService-->>UsuarioController: contenido

    alt contenido existe
        UsuarioController->>VisualizacionService: registrarOActualizarVisualizacion(usuario, contenido, "viendo", 0)
        UsuarioController-->>Usuario: redirect:/usuario/contenido/detalle/{id}
    else contenido no existe
        UsuarioController-->>Usuario: redirect:/usuario/contenido
    end

    Usuario->>UsuarioController: GET /usuario/contenido/detalle/{id}
    UsuarioController->>ContenidoService: obtenerPorId(id)
    UsuarioController->>FavoritoService: esFavorito(usuario, contenido)
    FavoritoService-->>UsuarioController: true / false
    UsuarioController-->>Vista: contenido, esFavorito
```

## 6. Agregar o quitar favorito

```mermaid
sequenceDiagram
    actor Usuario
    participant UsuarioController
    participant Session as HttpSession
    participant ContenidoService
    participant FavoritoService
    participant FavoritoRepository

    Usuario->>UsuarioController: GET /usuario/contenido/favorito/{id}
    UsuarioController->>Session: getAttribute(usuario)

    alt no hay sesion o no es USUARIO
        UsuarioController-->>Usuario: redirect:/login
    else sesion valida
        UsuarioController->>ContenidoService: obtenerPorId(id)
        ContenidoService-->>UsuarioController: contenido
        UsuarioController->>FavoritoService: toggleFavorito(usuario, contenido)
        FavoritoService->>FavoritoRepository: exists/find favorito

        alt ya existe favorito
            FavoritoService->>FavoritoRepository: delete favorito
        else no existe favorito
            FavoritoService->>FavoritoRepository: save favorito
        end

        UsuarioController-->>Usuario: redirect segun parametro redirect
    end
```

## 7. CRUD de contenido desde admin

```mermaid
sequenceDiagram
    actor Admin
    participant AdminController
    participant Session as HttpSession
    participant ContenidoService
    participant GeneroRepository
    participant ContenidoRepository
    participant Archivo as Sistema de archivos
    participant Vista as admin/contenido.html

    Admin->>AdminController: GET /admin/contenido
    AdminController->>Session: getAttribute(usuario)

    alt no hay sesion o no es ADMIN
        AdminController-->>Admin: redirect:/login
    else sesion valida
        AdminController->>ContenidoService: obtenerTodos()
        ContenidoService->>ContenidoRepository: findAll()
        ContenidoRepository-->>ContenidoService: contenidos
        AdminController->>GeneroRepository: findAll()
        GeneroRepository-->>AdminController: generos
        AdminController-->>Vista: contenidos, generos
    end

    Admin->>AdminController: POST /admin/contenido/guardar
    AdminController->>ContenidoService: guardar(id, titulo, descripcion, tipo, imagenArchivo, duracion, generoIds)

    alt hay imagen subida
        ContenidoService->>Archivo: guardar imagen en media.upload-dir
    end

    ContenidoService->>GeneroRepository: findAllById(generoIds)
    GeneroRepository-->>ContenidoService: generos seleccionados
    ContenidoService->>ContenidoRepository: save(contenido)
    ContenidoRepository-->>ContenidoService: contenido guardado
    AdminController-->>Admin: redirect:/admin/contenido

    Admin->>AdminController: POST /admin/contenido/eliminar
    AdminController->>ContenidoService: eliminar(id)
    ContenidoService->>ContenidoRepository: findById(id)
    ContenidoService->>ContenidoRepository: delete(contenido)
    AdminController-->>Admin: redirect:/admin/contenido
```

## 8. CRUD de usuarios desde admin

```mermaid
sequenceDiagram
    actor Admin
    participant AdminController
    participant Session as HttpSession
    participant UsuarioService
    participant UsuarioRepository
    participant FavoritoRepository
    participant VisualizacionRepository
    participant Vista as admin/usuarios.html

    Admin->>AdminController: GET /admin/usuarios
    AdminController->>Session: getAttribute(usuario)

    alt no hay sesion o no es ADMIN
        AdminController-->>Admin: redirect:/login
    else sesion valida
        AdminController->>UsuarioService: obtenerTodos()
        UsuarioService->>UsuarioRepository: findAll()
        UsuarioRepository-->>UsuarioService: usuarios
        AdminController-->>Vista: usuarios
    end

    Admin->>AdminController: POST /admin/usuarios/guardar
    AdminController->>UsuarioService: guardar(id, apodo, correo, contrasena, tipoUsuario)
    UsuarioService->>UsuarioRepository: findByCorreo(correo)
    UsuarioService->>UsuarioRepository: findByApodo(apodo)

    alt correo o apodo duplicado
        UsuarioService-->>AdminController: error
        AdminController-->>Admin: redirect con mensaje de error
    else datos validos
        UsuarioService->>UsuarioRepository: save(usuario)
        AdminController-->>Admin: redirect:/admin/usuarios
    end

    Admin->>AdminController: POST /admin/usuarios/eliminar

    alt intenta eliminar su propia cuenta
        AdminController-->>Admin: redirect con mensaje de error
    else elimina otro usuario
        AdminController->>UsuarioService: eliminar(id)
        UsuarioService->>UsuarioRepository: findById(id)
        UsuarioService->>FavoritoRepository: deleteByUsuario(usuario)
        UsuarioService->>VisualizacionRepository: deleteByUsuario(usuario)
        UsuarioService->>UsuarioRepository: delete(usuario)
        AdminController-->>Admin: redirect:/admin/usuarios
    end
```

## 9. CRUD de generos desde admin

```mermaid
sequenceDiagram
    actor Admin
    participant AdminController
    participant Session as HttpSession
    participant GeneroService
    participant GeneroRepository
    participant ContenidoRepository
    participant Vista as admin/generos.html

    Admin->>AdminController: GET /admin/generos
    AdminController->>Session: getAttribute(usuario)

    alt no hay sesion o no es ADMIN
        AdminController-->>Admin: redirect:/login
    else sesion valida
        AdminController->>GeneroService: obtenerTodos()
        GeneroService->>GeneroRepository: findAll()
        GeneroRepository-->>GeneroService: generos
        AdminController-->>Vista: generos
    end

    Admin->>AdminController: POST /admin/generos/guardar
    AdminController->>GeneroService: guardar(id, nombre, descripcion)
    GeneroService->>GeneroRepository: findByNombre(nombre)

    alt nombre duplicado
        GeneroService-->>AdminController: error
        AdminController-->>Admin: redirect con mensaje de error
    else datos validos
        GeneroService->>GeneroRepository: save(genero)
        AdminController-->>Admin: redirect:/admin/generos
    end

    Admin->>AdminController: POST /admin/generos/eliminar
    AdminController->>GeneroService: eliminar(id)
    GeneroService->>GeneroRepository: findById(id)
    GeneroService->>ContenidoRepository: findByGenerosId(id)
    GeneroService->>ContenidoRepository: quitar genero de contenidos asociados
    GeneroService->>GeneroRepository: delete(genero)
    AdminController-->>Admin: redirect:/admin/generos
```

## 10. Logout

```mermaid
sequenceDiagram
    actor Usuario
    participant AuthController
    participant Session as HttpSession

    Usuario->>AuthController: GET /auth/logout
    AuthController->>Session: invalidate()
    AuthController-->>Usuario: redirect:/
```
