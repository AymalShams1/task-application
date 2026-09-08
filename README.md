# Task Application

A task manager built with **Spring Boot 4** and **Java 21**, with a React user interface.

The backend source lives here. The frontend ships as a pre-built container image, so running
the whole app needs nothing but Docker — no Java, no Node, no Maven.

---

## Quick start

```bash
git clone https://github.com/aymalshams1/task-application.git
cd task-application
docker compose up -d
```

Open <http://localhost:3000>.

The first run takes a few minutes while the backend is compiled. Later runs start in seconds.

```bash
docker compose logs -f     # follow logs
docker compose down        # stop (tasks are kept)
docker compose down -v     # stop and delete all saved tasks
```

### Updating

```bash
docker compose pull        # get the latest published UI image
docker compose up -d --build
```

---

## Architecture

```
browser ──▶ ui (nginx, port 3000) ──/api/──▶ backend (Spring Boot, port 8080) ──▶ H2 file DB
              pulled from GHCR                 built from ./backend                on a volume
```

The UI container serves the compiled static bundle and reverse-proxies everything under
`/api/` to the backend, so the browser only talks to one origin and there is no CORS
configuration to maintain.

| Service   | Source                                  | Host port |
| --------- | --------------------------------------- | --------- |
| `ui`      | `ghcr.io/aymalshams1/task-app-frontend` | 3000      |
| `backend` | built from `./backend`                  | 8080      |

Ports and the image tag can be changed by copying `.env.example` to `.env` and editing it.
Compose reads `.env` automatically.

Tasks are stored in an H2 file database on the `task-data` volume, so they survive
`docker compose down`. For a throwaway in-memory database instead, delete the
`SPRING_DATASOURCE_*` and `SPRING_JPA_*` environment variables and the two `volumes` keys in
`docker-compose.yml`.

---

## The backend

Spring Boot 4.1 on Java 21, laid out in conventional layers:

```
controller/   REST endpoints and a @ControllerAdvice exception handler
service/      business logic behind an interface
repository/   Spring Data JPA
mapper/       entity <-> DTO conversion
domain/       entities, enums, request/response DTOs
```

Bean validation on incoming DTOs, a global exception handler that turns failures into
consistent JSON error bodies, and constructor injection throughout.

### API

| Method   | Path                 | Purpose        |
| -------- | -------------------- | -------------- |
| `GET`    | `/api/v1/tasks`      | List all tasks |
| `POST`   | `/api/v1/tasks`      | Create a task  |
| `PUT`    | `/api/v1/tasks/{id}` | Update a task  |
| `DELETE` | `/api/v1/tasks/{id}` | Delete a task  |

### Running it natively

Needs JDK 21:

```bash
cd backend
./mvnw spring-boot:run
```

Serves on port 8080 with an in-memory H2 database.

```bash
./mvnw verify    # run the tests
```

`.github/workflows/backend-ci.yml` runs the tests and verifies the Docker image builds on
every push and pull request.

---

## The frontend

React 19, Vite, TypeScript and Tailwind, served by nginx. It is built from a separate
repository and published to GitHub Container Registry; `docker compose` pulls it, so there is
nothing to build locally.

To point the UI at a backend running somewhere else:

```bash
docker run --rm -p 3000:3000 \
  -e BACKEND_HOST=host.docker.internal \
  -e BACKEND_PORT=8080 \
  ghcr.io/aymalshams1/task-app-frontend:latest
```
