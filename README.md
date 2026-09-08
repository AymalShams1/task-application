# Task Application

A full-stack task management application built with **Spring Boot, Java, and React**.

The application provides a REST API for managing tasks, a React frontend, persistent storage, and a Docker-based setup for running the complete stack.

## Tech Stack

**Backend:** Java 21, Spring Boot 4, Spring Data JPA, H2, Maven  
**Frontend:** React, TypeScript, Vite, Tailwind CSS, nginx  
**DevOps:** Docker, Docker Compose, GitHub Actions

## Getting Started

The entire application can be run with Docker. No local Java, Node.js, or Maven installation is required.

```bash
git clone https://github.com/aymalshams1/task-application.git
cd task-application
docker compose up -d
```

Open `http://localhost:3000`.

The initial build may take a few minutes. Subsequent starts are much faster.

```bash
docker compose logs -f     # View logs
docker compose down        # Stop the application
docker compose down -v     # Stop and remove saved task data
```

## Architecture

```text
Browser
   │
   ▼
React + nginx :3000
   │
   │ /api/*
   ▼
Spring Boot :8080
   │
   ▼
H2 Database
```

nginx serves the React frontend and proxies `/api/` requests to the Spring Boot backend. Task data is persisted using an H2 file database backed by a Docker volume.

## API

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/v1/tasks` | Get all tasks |
| `POST` | `/api/v1/tasks` | Create a task |
| `PUT` | `/api/v1/tasks/{id}` | Update a task |
| `DELETE` | `/api/v1/tasks/{id}` | Delete a task |

## Backend

The backend follows a layered structure:

```text
controller/   REST endpoints and exception handling
service/      Business logic
repository/   Data access with Spring Data JPA
mapper/       Entity and DTO conversion
domain/       Entities, enums, and DTOs
```

It includes request validation, constructor injection, global exception handling, and consistent JSON error responses.

To run the backend directly with JDK 21:

```bash
cd backend
./mvnw spring-boot:run
```

Run the tests with:

```bash
./mvnw verify
```

## Frontend

The frontend is built with **React, TypeScript, Vite, and Tailwind CSS** and served through nginx.

It is maintained separately and published as a container image to GitHub Container Registry. Docker Compose pulls the image automatically when starting the application.

## CI

GitHub Actions runs the backend test suite and verifies the Docker build on pushes and pull requests.
