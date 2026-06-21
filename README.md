# 📚 Microservicio Biblioteca

Microservicio que gestiona la biblioteca de juegos de cada usuario en la plataforma **Monsoon** (qué juegos posee, fecha de adquisición y horas jugadas). Expone una API REST con soporte de **HATEOAS** y documentación interactiva con **Swagger / OpenAPI**.

## 🛠 Tecnologías

- Java 17
- Spring Boot 3.5.13
- Spring Data JPA
- Spring HATEOAS
- Spring WebFlux (WebClient)
- Springdoc OpenAPI (Swagger UI)
- MySQL
- Lombok
- Maven
- Docker / Docker Compose

## 📂 Arquitectura

```
controller   → recibe las peticiones HTTP
service      → lógica de negocio
repository   → acceso a datos (Spring Data JPA)
model        → entidad bibliotecaus
dto          → objetos de transferencia expuestos por la API
Assembler    → construye los DTO y agrega enlaces HATEOAS
```

## 🧾 Modelo

**Entidad `bibliotecaus`** (tabla `biblioteca`):

```java
private Long id;
private Long usuarioId;
private Long juegoId;
private LocalDate fechaAdquisicion;
private Double horasJugadas;
```

**DTO de salida `DTOBiblioteca`** (incluye los datos del juego embebidos):

```java
private Long id;
private Long usuarioId;
private LocalDate fechaAdquisicion;
private Double horasJugadas;
private DTOjuego juego;
```

```java
// DTOjuego
private Long id;
private String titulo;
private String genero;
private String descripcion;
private Double precio;
private String desarrollador;
```

## ⚙️ Configuración

### Opción A — Ejecución local

Crea la base de datos antes de ejecutar:

```sql
CREATE DATABASE db_biblioteca;
```

`application.properties`:

```properties
spring.application.name=biblioteca
server.port=8084

spring.datasource.url=jdbc:mysql://localhost:3306/db_biblioteca?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Si usas Laragon, cambia `password=` por `password=root`.

### Opción B — Docker Compose

El proyecto incluye `Dockerfile` y `docker-compose.yml`:

```bash
docker compose up --build
```

Esto expone:

| Servicio | Puerto host | Puerto interno |
|---|---|---|
| App Spring Boot | `8084` | `8080` |

> El `docker-compose.yml` de este servicio no incluye un contenedor de base de datos; debes apuntar `SPRING_DATASOURCE_URL` a una instancia de MySQL accesible (local o en otro contenedor/red Docker).

## ▶️ Cómo ejecutar

**Local con Maven:**

```bash
mvn spring-boot:run
```

**Con Docker:**

```bash
docker compose up --build
```

La API quedará disponible en `http://localhost:8084`.

## 📌 Endpoints

Todos bajo el prefijo `/api/v0/biblioteca`.

| Método | URL | Descripción |
|---|---|---|
| GET | `/api/v0/biblioteca` | Obtener todas las entradas de la biblioteca |
| GET | `/api/v0/biblioteca/{id}` | Obtener una entrada por ID |
| POST | `/api/v0/biblioteca` | Crear una entrada |
| PUT | `/api/v0/biblioteca/{id}` | Actualizar una entrada |
| DELETE | `/api/v0/biblioteca/{id}` | Eliminar una entrada |

### Ejemplo POST `/api/v0/biblioteca`

```
POST http://localhost:8084/api/v0/biblioteca
```

```json
{
  "usuarioId": 5,
  "juegoId": 100,
  "fechaAdquisicion": "2026-01-15",
  "horasJugadas": 0
}
```

### Ejemplo GET por ID

```
GET http://localhost:8084/api/v0/biblioteca/1
```

### Ejemplo de respuesta (con HATEOAS)

```json
{
  "id": 1,
  "usuarioId": 5,
  "fechaAdquisicion": "2026-01-15",
  "horasJugadas": 10.0,
  "juego": {
    "id": 100,
    "titulo": "...",
    "genero": "...",
    "descripcion": "...",
    "precio": 0.0,
    "desarrollador": "..."
  },
  "_links": {
    "self": { "href": "http://localhost:8084/api/v0/biblioteca/1" },
    "lista-completa": { "href": "http://localhost:8084/api/v0/biblioteca" }
  }
}
```

> ℹ️ Si el microservicio de juegos no responde al consultar `juego`, este servicio devuelve un `DTOjuego` parcial (solo con el `id`) en lugar de fallar.

## 📖 Documentación interactiva (Swagger)

Una vez levantado el proyecto:

```
http://localhost:8084/swagger-ui/index.html
```

## 🧪 Tests

El proyecto incluye pruebas con `MockMvc` sobre la capa de controlador (`ControlbibliotecaTest`), que validan:

- Obtención de una entrada por ID, incluyendo los enlaces `_links.self` y `_links.lista-completa`.
- Creación de una entrada, validando el código `200 OK`.

```bash
mvn test
```

## 📁 Estructura del proyecto

```
src/main/java/com/example/biblioteca/
├── Assembler/      # BibliotecaAssembler (HATEOAS)
├── controller/     # controlbiblioteca
├── dto/            # DTOBiblioteca, DTOjuego
├── model/          # bibliotecaus
├── repository/     # repobiblioteca
└── service/        # serviciobiblioteca
```

## 🚧 Notas / mejoras pendientes

- Los endpoints `POST`, `PUT` y `DELETE` no devuelven el recurso con sus enlaces HATEOAS (a diferencia de `GET`); podría enriquecerse para consistencia.
- No hay manejo explícito de `404 Not Found` en `actualizarEntrada` ni `eliminarEntrada` cuando el `id` no existe.
- El servicio no tiene seguridad/autenticación configurada en el código mostrado.
