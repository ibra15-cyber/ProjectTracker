# ProjectTracker

A comprehensive project management application built with Spring Boot, PostgreSQL, MongoDB, and Redis, designed to track projects, tasks, and developer assignments. The application includes features for auditing changes, caching, and dynamic project filtering.

## Table of Contents

- [Project Overview](https://www.google.com/search?q=%23project-overview)
- [Features](https://www.google.com/search?q=%23features)
- [Technologies Used](https://www.google.com/search?q=%23technologies-used)
- [Prerequisites](https://www.google.com/search?q=%23prerequisites)
- [Getting Started](https://www.google.com/search?q=%23getting-started)
    - [Local Development Setup](https://www.google.com/search?q=%23local-development-setup)
    - [Running with Docker Compose](https://www.google.com/search?q=%23running-with-docker-compose)
- [Configuration](https://www.google.com/search?q=%23configuration)
- [Database Schema](https://www.google.com/search?q=%23database-schema)
- [API Endpoints](https://www.google.com/search?q=%23api-endpoints)
- [Caching](https://www.google.com/search?q=%23caching)
- [Audit Logging](https://www.google.com/search?q=%23audit-logging)
- [Error Handling](https://www.google.com/search?q=%23error-handling)
- [Building the Project](https://www.google.com/search?q=%23building-the-project)
- [Testing](https://www.google.com/search?q=%23testing)

## Project Overview

ProjectTracker is a backend application that provides a robust API for managing software development projects. It allows for the creation and management of projects, tasks within projects, and the assignment of tasks to developers. Key aspects include:

- **Project Management**: Create, update, delete, and list projects with statuses and deadlines.
- **Task Management**: Define tasks for projects, track their status, and assign deadlines.
- **Developer Management**: Manage developer profiles, including their skills.
- **Task Assignments**: Assign tasks to developers and track their individual progress.
- **Auditing**: Comprehensive logging of critical actions to MongoDB.
- **Caching**: Utilizes Redis for efficient data retrieval.
- **Dynamic Filtering**: Advanced search capabilities for projects using JPA Specifications.

## Features

* **CRUD Operations**: Full Create, Read, Update, Delete functionality for Projects, Tasks, Developers, and Task Assignments.
* **Caching with Redis**: Improves performance for frequently accessed data (e.g., Project details).
* **Audit Logging with MongoDB**: Records all significant changes and actions within the system for traceability.
* **Dynamic Project Filtering**: Filter projects by name, description, status, deadlines, and creation dates using Spring Data JPA Specifications.
* **Pagination and Sorting**: Retrieve paginated and sorted lists of entities.
* **Custom Queries**: Identify top developers by task assignments and overdue tasks.
* **Global Exception Handling**: Provides consistent error responses for various exceptions.
* **DTO Mapping**: Separate DTOs for data transfer, ensuring clear API contracts.
* **API Documentation**: Integrated with Springdoc OpenAPI for interactive API documentation (Swagger UI).
* **Environment Variables**: Uses `spring-dotenv` for managing environment-specific configurations.

## Technologies Used

* **Spring Boot**: 3.5.0
* **Java**: 21
* **Gradle**: Build automation
* **PostgreSQL**: Relational database for core project, task, and developer data.
* **MongoDB**: NoSQL database for audit logs.
* **Redis**: In-memory data store for caching.
* **Spring Data JPA**: For PostgreSQL interactions.
* **Spring Data MongoDB**: For MongoDB interactions.
* **Spring Data Redis**: For Redis interactions and caching.
* **Lombok**: Reduces boilerplate code.
* **Springdoc OpenAPI UI**: For API documentation (Swagger UI).
* **`spring-dotenv`**: For loading environment variables from `.env` files.
* **Validation**: `jakarta.validation.constraints` for request body validation.

## Prerequisites

Before running the application, ensure you have the following installed:

* **Java Development Kit (JDK)**: Version 21
* **Gradle**: (Usually comes bundled with Spring Boot projects or can be installed separately)
* **Docker** and **Docker Compose**: For easily setting up PostgreSQL, MongoDB, and Redis.
* **PostgreSQL Client**: Optional, for direct database interaction.
* **MongoDB Client**: Optional, for direct database interaction.
* **Redis CLI**: Optional, for direct cache interaction.

## Getting Started

You can run this application either locally or using Docker Compose.

### Local Development Setup

1.  **Clone the repository:**

    ```bash
    git clone <your-repository-url>
    cd ProjectTracker
    ```

2.  **Database Setup:**
    The application uses PostgreSQL as the primary database and MongoDB for audit logs, along with Redis for caching. The `application.properties` file is configured for local instances running on default ports.

    **Using Docker Compose (Recommended for local setup):**
    Ensure Docker Desktop is running. You can start all necessary database services with a single command:

    ```bash
    # Assuming you have a docker-compose.yml in the project root
    docker compose up -d postgres redis mongo
    ```

    This will start PostgreSQL on `5432`, Redis on `6379`, and MongoDB on `27017`.

3.  **Configure `application.properties`:**
    The `application.properties` file located in `src/main/resources/` contains the database and Redis connection details.

    ```properties
    spring.application.name=ProjectTracker
    server.port=3232

    # PostgreSQL connection
    spring.datasource.url=jdbc:postgresql://localhost:5432/project_tracker
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

    # MongoDB connection
    spring.data.mongodb.uri=mongodb://localhost:27017/project_tracker_log

    # Redis configuration
    spring.cache.type=redis
    spring.data.redis.host=localhost
    spring.data.redis.port=6379
    ```

    Ensure these match your local database setups. If you're using Docker Compose as recommended, these settings will work out of the box.

4.  **Run the application:**
    You can run the Spring Boot application using Gradle:

    ```bash
    ./gradlew bootRun
    ```

    The application will start on `http://localhost:3232`.

### Running with Docker Compose

For a fully containerized environment, you can use Docker Compose. This assumes you have a `docker-compose.yml` file similar to the following (which was implicitly confirmed):

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: project_tracker
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: redis:latest
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

  mongo:
    image: mongo:latest
    ports:
      - "27017:27017"
    volumes:
      - mongo_data:/data/db

  app:
    build: .
    ports:
      - "3232:3232"
    environment:
      # Ensure these match your application.properties or .env if used
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/project_tracker
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      SPRING_DATA_MONGODB_URI: mongodb://mongo:27017/project_tracker_log
      SPRING_DATA_REDIS_HOST: redis
      SPRING_DATA_REDIS_PORT: 6379
    depends_on:
      - postgres
      - redis
      - mongo
    # If using spring-dotenv, ensure your .env is copied into the container or environment variables are set.

volumes:
  postgres_data:
  redis_data:
  mongo_data:
```

1.  **Build the Docker image for the application:**

    ```bash
    ./gradlew bootBuildImage --imageName=project-tracker:latest
    ```

    Or, if you prefer building the Dockerfile directly:

    ```bash
    docker build -t project-tracker:latest .
    ```

2.  **Start all services using Docker Compose:**

    ```bash
    docker compose up --build -d
    ```

    This command will build your application image (if not already built), and then start all services (PostgreSQL, Redis, MongoDB, and your Spring Boot application).

The application will be accessible at `http://localhost:3232`.

## Configuration

The main configuration is in `src/main/resources/application.properties`.

* `server.port=3232`: The port on which the application runs.
* **PostgreSQL**: Configured for `jdbc:postgresql://localhost:5432/project_tracker`.
* **MongoDB**: Configured for `mongodb://localhost:27017/project_tracker_log` (for audit logs).
* **Redis**: Configured for `localhost:6379` (for caching).
* `spring.jpa.hibernate.ddl-auto=update`: Hibernate will update the schema based on entity definitions.
* `spring.cache.type=redis`: Enables Redis as the caching provider.

The project uses `spring-dotenv` for loading environment variables. If you intend to use `.env` files for configuration, ensure they are set up correctly.

## Database Schema

The relational database (PostgreSQL) schema is managed by Spring Data JPA and Hibernate based on the entity definitions.

### Entities

* **Developer**: Represents a developer.

    * `developerId` (PK)
    * `name`
    * `email`
    * `skills` (Enum: `FRONTEND`, `BACKEND`, `DEVOPS`, `QA`)
    * `taskAssignments` (One-to-Many relationship with `TaskAssignment`)

* **Project**: Represents a project.

    * `projectId` (PK)
    * `name`
    * `description`
    * `createdAt`
    * `deadline`
    * `status` (Enum: `PLANNING`, `ACTIVE`, `ON_HOLD`, `COMPLETED`, `CANCELLED`, `ARCHIVED`)
    * `tasks` (One-to-Many relationship with `Task`)

* **Task**: Represents a task within a project.

    * `taskId` (PK)
    * `title`
    * `description`
    * `status` (Enum: `TODO`, `IN_PROGRESS`, `UNDER_REVIEW`, `DONE`, `BLOCKED`, `CANCELLED`)
    * `createdAt`
    * `deadline`
    * `project` (Many-to-One relationship with `Project`)
    * `taskAssignments` (One-to-Many relationship with `TaskAssignment`)

* **TaskAssignment**: Represents the assignment of a task to a developer.

    * `taskAssignmentId` (PK)
    * `task` (Many-to-One relationship with `Task`)
    * `developer` (Many-to-One relationship with `Developer`)
    * `assignedOn`
    * `status` (Enum: `ACTIVE`, `COMPLETED`, `REMOVED`)
    * `deadline`
    * `completedOn`

### Audit Logs (MongoDB)

* **AuditLog**: Stores audit events in a MongoDB collection.
    * `id` (MongoDB PK)
    * `actionType` (e.g., "CREATE", "UPDATE", "DELETE", "DEADLINE\_CHANGE")
    * `entityType` (e.g., "PROJECT", "DEVELOPER", "TASK")
    * `entityId` (ID of the entity being audited)
    * `timestamp`
    * `actorName` (e.g., "SYSTEM")
    * `payload` (JSON string containing relevant change details)

## API Endpoints

The API documentation is available via Swagger UI. Once the application is running, navigate to:
[http://localhost:3232/swagger-ui.html](https://www.google.com/search?q=http://localhost:3232/swagger-ui.html)

This will provide an interactive interface to explore all available endpoints, request/response schemas, and even try out API calls.

Common endpoint patterns (specific paths might vary):

* **Projects:**

    * `POST /api/projects`: Create a new project.
    * `GET /api/projects/{id}`: Get a project by ID.
    * `PUT /api/projects/{id}`: Update an existing project.
    * `DELETE /api/projects/{id}`: Delete a project.
    * `GET /api/projects`: Get all projects (with pagination/filtering support).
    * `GET /api/projects/search`: Dynamically filter projects based on various criteria (e.g., `name`, `status`, `deadline`).

* **Tasks:**

    * `POST /api/tasks`: Create a new task.
    * `GET /api/tasks/{id}`: Get a task by ID.
    * `PUT /api/tasks/{id}`: Update an existing task.
    * `DELETE /api/tasks/{id}`: Delete a task.
    * `GET /api/tasks`: Get all tasks (with sorting).
    * `GET /api/tasks/overdue`: Get all overdue tasks.
    * `GET /api/tasks/counts`: Get task counts grouped by status and project.

* **Developers:**

    * `POST /api/developers`: Create a new developer.
    * `GET /api/developers/{id}`: Get a developer by ID.
    * `PUT /api/developers/{id}`: Update an existing developer.
    * `DELETE /api/developers/{id}`: Delete a developer.
    * `GET /api/developers`: Get all developers (with pagination).
    * `GET /api/developers/top-5`: Get top 5 developers with most assigned tasks.

* **Task Assignments:**

    * `POST /api/task-assignments`: Assign a task to a developer.
    * `GET /api/task-assignments/{id}`: Get a task assignment by ID.
    * `PUT /api/task-assignments/{id}`: Update a task assignment.
    * `DELETE /api/task-assignments/{id}`: Delete a task assignment.
    * `GET /api/task-assignments/developer/{developerId}`: Get all task assignments for a specific developer.

* **Audit Logs:**

    * `GET /api/audit-logs/entity/{entityType}/{entityId}`: Get audit logs for a specific entity.
    * `GET /api/audit-logs/actor/{actorName}`: Get audit logs by actor.
    * `GET /api/audit-logs/action/{actionType}`: Get audit logs by action type.
    * `GET /api/audit-logs/recent`: Get recent audit logs.

## Caching

The application leverages Spring Cache with Redis as the caching provider.

* The `@EnableCaching` annotation is present in `ProjectTrackerApplication.java`.
* `ProjectServiceImpl` uses `@Cacheable` and `@CachePut` on methods like `getProjectById` and `updateProject`, and `@CacheEvict` on `deleteProject` to manage the cache.
* The `application.properties` configures Redis as the cache type:
  ```properties
  spring.cache.type=redis
  spring.data.redis.host=localhost
  spring.data.redis.port=6379
  ```

## Audit Logging

Audit logs are stored in a MongoDB database. The `AuditLogService` is responsible for recording various actions, such as creation, updates, and deletions of entities, as well as specific events like project deadline changes.

* `AuditLog.java` defines the structure of audit records in MongoDB.
* `AuditLogRepository.java` provides data access methods for audit logs.
* `AuditLogService.java` contains methods to log specific actions and query audit records.

Example logging actions:

* `logCreate(entityType, entityId, entity)`
* `logUpdate(entityType, entityId, oldEntity, newEntity)`
* `logDelete(entityType, entityId, entity)`
* `logProjectDeadlineChange(projectId, oldDeadline, newDeadline)`

## Error Handling

The application implements global exception handling using `@ControllerAdvice` and `@ExceptionHandler` in `GlobalException.java`. This ensures consistent and informative error responses in `Response` DTO format for different types of exceptions, such as `ResourceNotFoundException`.

## Building the Project

To build the project JAR file:

```bash
./gradlew clean build
```

This will create a JAR file in the `build/libs` directory.

## Testing

To run the unit and integration tests:

```bash
./gradlew test
```