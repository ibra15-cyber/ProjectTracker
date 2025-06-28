# ProjectTracker

A comprehensive project management application built with Spring Boot, PostgreSQL, MongoDB, and Redis, designed to track projects, tasks, and developer assignments. The application includes features for auditing changes, caching, dynamic project filtering, JWT authentication, OAuth2 integration, and automated email notifications.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [Local Development Setup](#local-development-setup)
    - [Running with Docker Compose](#running-with-docker-compose)
- [Configuration](#configuration)
    - [Database Configuration](#database-configuration)
    - [Security Configuration](#security-configuration)
    - [Email Configuration](#email-configuration)
- [Security Features](#security-features)
    - [JWT Authentication](#jwt-authentication)
    - [OAuth2 Integration](#oauth2-integration)
    - [API Security](#api-security)
- [Event Scheduling](#event-scheduling)
    - [Email Notifications](#email-notifications)
    - [Cron Job Configuration](#cron-job-configuration)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Authentication Endpoints](#authentication-endpoints)
- [Caching](#caching)
- [Audit Logging](#audit-logging)
- [Error Handling](#error-handling)
- [Building the Project](#building-the-project)
- [Testing](#testing)

## Project Overview

ProjectTracker is a secure backend application that provides a robust API for managing software development projects. It allows for the creation and management of projects, tasks within projects, and the assignment of tasks to developers. Key aspects include:

- **Project Management**: Create, update, delete, and list projects with statuses and deadlines.
- **Task Management**: Define tasks for projects, track their status, and assign deadlines.
- **Developer Management**: Manage developer profiles, including their skills.
- **Task Assignments**: Assign tasks to developers and track their individual progress.
- **Security**: JWT-based authentication with OAuth2 support (Google & GitHub).
- **Email Notifications**: Automated email alerts for deadlines and project updates.
- **Auditing**: Comprehensive logging of critical actions to MongoDB.
- **Caching**: Utilizes Redis for efficient data retrieval.
- **Dynamic Filtering**: Advanced search capabilities for projects using JPA Specifications.
- **Scheduled Tasks**: Automated background processes for monitoring and notifications.

## Features

* **CRUD Operations**: Full Create, Read, Update, Delete functionality for Projects, Tasks, Developers, and Task Assignments.
* **JWT Authentication**: Secure token-based authentication system.
* **OAuth2 Integration**: Login with Google and GitHub accounts.
* **Role-Based Access Control**: Different permission levels for users.
* **Email Notifications**: Automated email alerts for deadlines, assignments, and project updates.
* **Scheduled Tasks**: Cron jobs running every 5 minutes to scan for upcoming deadlines and send notifications.
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
* **Spring Security**: Authentication and authorization framework.
* **Spring Data JPA**: For PostgreSQL interactions.
* **Spring Data MongoDB**: For MongoDB interactions.
* **Spring Data Redis**: For Redis interactions and caching.
* **JWT (JSON Web Tokens)**: For stateless authentication.
* **OAuth2**: For third-party authentication (Google, GitHub).
* **Spring Boot Mail**: For email notifications.
* **Spring Scheduler**: For automated tasks and cron jobs.
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
* **Email Service Account**: Gmail or other SMTP service for email notifications.
* **OAuth2 Applications**: Google and GitHub OAuth applications for social login.

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

3.  **Environment Variables Setup:**
    Create a `.env` file in the project root with the following variables:

    ```env
    # Database Configuration
    DB_HOST=localhost
    DB_PORT=5432
    DB_NAME=project_tracker
    DB_USERNAME=postgres
    DB_PASSWORD=postgres
    MONGODB_URI=mongodb://localhost:27017/project_tracker_log
    REDIS_HOST=localhost
    REDIS_PORT=6379

    # JWT Configuration
    JWT_SECRET=your-256-bit-secret-key-here
    JWT_EXPIRATION=86400000
    JWT_REFRESH_EXPIRATION=604800000

    # OAuth2 Configuration
    GOOGLE_CLIENT_ID=your-google-client-id
    GOOGLE_CLIENT_SECRET=your-google-client-secret
    GITHUB_CLIENT_ID=your-github-client-id
    GITHUB_CLIENT_SECRET=your-github-client-secret

    # Email Configuration
    MAIL_HOST=smtp.gmail.com
    MAIL_PORT=587
    MAIL_USERNAME=your-email@gmail.com
    MAIL_PASSWORD=your-app-password
    MAIL_FROM=your-email@gmail.com

    # Application Configuration
    SERVER_PORT=3232
    FRONTEND_URL=http://localhost:3000
    ```

4.  **Configure OAuth2 Applications:**

    **Google OAuth2:**
    - Go to [Google Cloud Console](https://console.cloud.google.com/)
    - Create a new project or select existing one
    - Enable Google+ API
    - Create OAuth2 credentials
    - Add `http://localhost:3232/oauth2/callback/google` to authorized redirect URIs

    **GitHub OAuth2:**
    - Go to GitHub Settings > Developer settings > OAuth Apps
    - Create a new OAuth App
    - Set Authorization callback URL to `http://localhost:3232/oauth2/callback/github`

5.  **Run the application:**
    You can run the Spring Boot application using Gradle:

    ```bash
    ./gradlew bootRun
    ```

    The application will start on `http://localhost:3232`.

### Running with Docker Compose

For a fully containerized environment, you can use Docker Compose:

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
    env_file:
      - .env
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/project_tracker
      SPRING_DATA_MONGODB_URI: mongodb://mongo:27017/project_tracker_log
      SPRING_DATA_REDIS_HOST: redis
    depends_on:
      - postgres
      - redis
      - mongo

volumes:
  postgres_data:
  redis_data:
  mongo_data:
```

1.  **Build and start all services:**

    ```bash
    docker compose up --build -d
    ```

The application will be accessible at `http://localhost:3232`.

## Configuration

### Database Configuration

The main configuration is in `src/main/resources/application.properties`:

```properties
spring.application.name=ProjectTracker
server.port=${SERVER_PORT:3232}

# PostgreSQL connection
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:project_tracker}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# MongoDB connection
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/project_tracker_log}

# Redis configuration
spring.cache.type=redis
spring.data.redis.host=${REDIS_HOST:localhost}
spring.data.redis.port=${REDIS_PORT:6379}
```

### Security Configuration

```properties
# JWT Configuration
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}
jwt.refresh.expiration=${JWT_REFRESH_EXPIRATION:604800000}

# OAuth2 Configuration
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri=${FRONTEND_URL}/oauth2/callback/google

spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=user:email
spring.security.oauth2.client.registration.github.redirect-uri=${FRONTEND_URL}/oauth2/callback/github
```

### Email Configuration

```properties
# Email Configuration
spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_PORT:587}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
app.mail.from=${MAIL_FROM}
```

## Security Features

### JWT Authentication

The application uses JWT (JSON Web Tokens) for stateless authentication:

**Token Structure:**
- **Access Token**: Short-lived (24 hours by default) for API access
- **Refresh Token**: Long-lived (7 days by default) for token renewal

**Usage:**

1. **Login to get tokens:**
   ```bash
   POST /api/auth/login
   Content-Type: application/json
   
   {
     "email": "user@example.com",
     "password": "password123"
   }
   ```

2. **Response:**
   ```json
   {
     "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "tokenType": "Bearer",
     "expiresIn": 86400
   }
   ```

3. **Use token in API calls:**
   ```bash
   GET /api/projects
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

4. **Refresh expired tokens:**
   ```bash
   POST /api/auth/refresh
   Content-Type: application/json
   
   {
     "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }
   ```

### OAuth2 Integration

The application supports OAuth2 login with Google and GitHub:

**Google OAuth2 Login:**
```
GET /oauth2/authorization/google
```

**GitHub OAuth2 Login:**
```
GET /oauth2/authorization/github
```

**OAuth2 Flow:**
1. User clicks login with Google/GitHub
2. User is redirected to OAuth provider
3. After successful authentication, user is redirected back with authorization code
4. Application exchanges code for access token
5. Application creates or updates user account
6. JWT tokens are generated and returned

### API Security

**Protected Endpoints:**
- All `/api/**` endpoints require authentication
- Public endpoints: `/api/auth/**`, `/oauth2/**`, `/swagger-ui/**`

**Role-Based Access:**
- `ADMIN`: Full access to all resources
- `PROJECT_MANAGER`: Can manage projects and assign tasks
- `DEVELOPER`: Can view and update assigned tasks
- `USER`: Read-only access to own data

## Event Scheduling

### Email Notifications

The application automatically sends email notifications for:
- **Deadline Reminders**: Sent 24 hours before task/project deadlines
- **Overdue Alerts**: Sent for tasks/projects past their deadlines
- **Assignment Notifications**: Sent when tasks are assigned to developers
- **Status Updates**: Sent when project/task status changes

### Cron Job Configuration

**Deadline Scanner Job:**
Runs every 5 minutes to check for upcoming deadlines and overdue items:

```java
@Scheduled(fixedRate = 300000) // 5 minutes = 300,000 milliseconds
public void scanForUpcomingDeadlines() {
    // Scans for deadlines within next 24 hours
    // Sends email notifications
    // Updates notification status
}
```

**Configuration:**
```properties
# Enable scheduling
spring.task.scheduling.enabled=true

# Thread pool configuration
spring.task.scheduling.pool.size=5
spring.task.scheduling.thread-name-prefix=scheduler-
```

**Email Templates:**
The application uses customizable email templates for different notification types:
- `deadline-reminder.html`
- `overdue-alert.html`
- `task-assignment.html`
- `status-update.html`

## Database Schema

The relational database (PostgreSQL) schema is managed by Spring Data JPA and Hibernate based on the entity definitions.

### Entities

* **User**: Represents a system user with authentication details.
    * `userId` (PK)
    * `email` (Unique)
    * `password` (Encrypted)
    * `firstName`
    * `lastName`
    * `role` (Enum: `ADMIN`, `PROJECT_MANAGER`, `DEVELOPER`, `USER`)
    * `provider` (Enum: `LOCAL`, `GOOGLE`, `GITHUB`)
    * `providerId`
    * `emailVerified`
    * `createdAt`
    * `lastLoginAt`

* **Developer**: Represents a developer (extends User).
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
    * `createdBy` (Many-to-One relationship with `User`)

* **Task**: Represents a task within a project.
    * `taskId` (PK)
    * `title`
    * `description`
    * `status` (Enum: `TODO`, `IN_PROGRESS`, `UNDER_REVIEW`, `DONE`, `BLOCKED`, `CANCELLED`)
    * `createdAt`
    * `deadline`
    * `project` (Many-to-One relationship with `Project`)
    * `taskAssignments` (One-to-Many relationship with `TaskAssignment`)
    * `createdBy` (Many-to-One relationship with `User`)

* **TaskAssignment**: Represents the assignment of a task to a developer.
    * `taskAssignmentId` (PK)
    * `task` (Many-to-One relationship with `Task`)
    * `developer` (Many-to-One relationship with `Developer`)
    * `assignedOn`
    * `status` (Enum: `ACTIVE`, `COMPLETED`, `REMOVED`)
    * `deadline`
    * `completedOn`
    * `assignedBy` (Many-to-One relationship with `User`)

* **EmailNotification**: Tracks sent email notifications.
    * `notificationId` (PK)
    * `recipientEmail`
    * `subject`
    * `notificationType` (Enum: `DEADLINE_REMINDER`, `OVERDUE_ALERT`, `ASSIGNMENT`, `STATUS_UPDATE`)
    * `entityType` (e.g., "PROJECT", "TASK")
    * `entityId`
    * `sentAt`
    * `status` (Enum: `PENDING`, `SENT`, `FAILED`)

### Audit Logs (MongoDB)

* **AuditLog**: Stores audit events in a MongoDB collection.
    * `id` (MongoDB PK)
    * `actionType` (e.g., "CREATE", "UPDATE", "DELETE", "LOGIN", "DEADLINE_CHANGE")
    * `entityType` (e.g., "PROJECT", "DEVELOPER", "TASK", "USER")
    * `entityId` (ID of the entity being audited)
    * `timestamp`
    * `userId` (ID of the user performing the action)
    * `actorName` (e.g., username or "SYSTEM")
    * `payload` (JSON string containing relevant change details)

## API Endpoints

The API documentation is available via Swagger UI. Once the application is running, navigate to:
[http://localhost:3232/swagger-ui.html](http://localhost:3232/swagger-ui.html)

This will provide an interactive interface to explore all available endpoints, request/response schemas, and even try out API calls.

## Authentication Endpoints

* **User Registration & Login:**
    * `POST /api/auth/register`: Register a new user account
    * `POST /api/auth/login`: Login with email/password
    * `POST /api/auth/refresh`: Refresh access token using refresh token
    * `POST /api/auth/logout`: Logout and invalidate tokens
    * `POST /api/auth/forgot-password`: Request password reset
    * `POST /api/auth/reset-password`: Reset password with token

* **OAuth2 Authentication:**
    * `GET /oauth2/authorization/google`: Initiate Google OAuth2 login
    * `GET /oauth2/authorization/github`: Initiate GitHub OAuth2 login
    * `GET /oauth2/callback/google`: Google OAuth2 callback endpoint
    * `GET /oauth2/callback/github`: GitHub OAuth2 callback endpoint

* **User Profile:**
    * `GET /api/auth/me`: Get current user profile
    * `PUT /api/auth/me`: Update current user profile
    * `POST /api/auth/change-password`: Change user password

Common endpoint patterns for other resources (require authentication):

* **Projects:** `POST /api/projects`, `GET /api/projects/{id}`, `PUT /api/projects/{id}`, `DELETE /api/projects/{id}`, `GET /api/projects`, `GET /api/projects/search`

* **Tasks:** `POST /api/tasks`, `GET /api/tasks/{id}`, `PUT /api/tasks/{id}`, `DELETE /api/tasks/{id}`, `GET /api/tasks`, `GET /api/tasks/overdue`, `GET /api/tasks/counts`

* **Developers:** `POST /api/developers`, `GET /api/developers/{id}`, `PUT /api/developers/{id}`, `DELETE /api/developers/{id}`, `GET /api/developers`, `GET /api/developers/top-5`

* **Task Assignments:** `POST /api/task-assignments`, `GET /api/task-assignments/{id}`, `PUT /api/task-assignments/{id}`, `DELETE /api/task-assignments/{id}`, `GET /api/task-assignments/developer/{developerId}`

* **Notifications:** `GET /api/notifications`, `PUT /api/notifications/{id}/mark-read`, `DELETE /api/notifications/{id}`

* **Audit Logs:** `GET /api/audit-logs/entity/{entityType}/{entityId}`, `GET /api/audit-logs/actor/{actorName}`, `GET /api/audit-logs/action/{actionType}`, `GET /api/audit-logs/recent`

## Caching

The application leverages Spring Cache with Redis as the caching provider.

* The `@EnableCaching` annotation is present in `ProjectTrackerApplication.java`.
* `ProjectServiceImpl` uses `@Cacheable` and `@CachePut` on methods like `getProjectById` and `updateProject`, and `@CacheEvict` on `deleteProject` to manage the cache.
* JWT tokens are cached in Redis for quick validation.
* User session data is cached to improve authentication performance.

## Audit Logging

Audit logs are stored in a MongoDB database. The `AuditLogService` is responsible for recording various actions, including authentication events, entity changes, and system activities.

* `AuditLog.java` defines the structure of audit records in MongoDB.
* `AuditLogRepository.java` provides data access methods for audit logs.
* `AuditLogService.java` contains methods to log specific actions and query audit records.

Example logging actions:
* `logLogin(userId, ipAddress, userAgent)`
* `logLogout(userId)`
* `logCreate(entityType, entityId, entity, userId)`
* `logUpdate(entityType, entityId, oldEntity, newEntity, userId)`
* `logDelete(entityType, entityId, entity, userId)`
* `logProjectDeadlineChange(projectId, oldDeadline, newDeadline, userId)`
* `logPasswordChange(userId)`
* `logFailedLogin(email, ipAddress)`

## Error Handling

The application implements global exception handling using `@ControllerAdvice` and `@ExceptionHandler` in `GlobalExceptionHandler.java`. This ensures consistent and informative error responses for various exceptions:

* **Authentication Errors**: Invalid credentials, expired tokens, insufficient permissions
* **Validation Errors**: Request body validation failures
* **Business Logic Errors**: Resource not found, duplicate entities, invalid operations
* **System Errors**: Database connectivity, email service failures

## Building the Project

To build the project JAR file:

```bash
./gradlew clean build
```

This will create a JAR file in the `build/libs` directory.

To build a Docker image:

```bash
./gradlew bootBuildImage --imageName=project-tracker:latest
```

## Testing

To run the unit and integration tests:

```bash
./gradlew test
```

For testing with security features:

```bash
# Run tests with security context
./gradlew test --tests="*SecurityTest"

# Run integration tests
./gradlew integrationTest
```

**Test Categories:**
- Unit tests for services and repositories
- Integration tests for API endpoints with authentication
- Security tests for JWT and OAuth2 flows
- Email service tests (with mock SMTP server)
- Scheduler tests for cron jobs

**Testing JWT Authentication:**
Use the provided test utilities to generate test tokens for integration tests:

```java
@TestComponent
public class JwtTestUtils {
    public String generateTestToken(String email, String role) {
        // Generate test JWT token
    }
}
```