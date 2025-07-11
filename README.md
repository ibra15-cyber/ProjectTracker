# ProjectTracker

A comprehensive project management application built with Spring Boot, PostgreSQL, MongoDB, and Redis, designed to track projects, tasks, and developer assignments. The application includes features for auditing changes, caching, dynamic project filtering, JWT authentication, OAuth2 integration, automated email notifications, and comprehensive monitoring capabilities.

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
    - [Monitoring Configuration](#monitoring-configuration)
- [Security Features](#security-features)
    - [JWT Authentication](#jwt-authentication)
    - [OAuth2 Integration](#oauth2-integration)
    - [API Security](#api-security)
- [Event Scheduling](#event-scheduling)
    - [Email Notifications](#email-notifications)
    - [Cron Job Configuration](#cron-job-configuration)
- [Monitoring and Performance](#monitoring-and-performance)
    - [Multi-Tool Performance Monitoring](#multi-tool-performance-monitoring)
    - [Metrics and Health Checks](#metrics-and-health-checks)
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
- **Email Notifications**: Automated email alerts for deadlines and project updates with retry mechanisms.
- **Auditing**: Comprehensive logging of critical actions to MongoDB.
- **Caching**: Utilizes Redis for efficient data retrieval with statistics.
- **Dynamic Filtering**: Advanced search capabilities for projects using JPA Specifications.
- **Scheduled Tasks**: Automated background processes for monitoring and notifications.
- **Performance Monitoring**: Integrated multi-tool monitoring with JMeter, JProfiler, Grafana+Prometheus, and VisualVM.

## Features

* **CRUD Operations**: Full Create, Read, Update, Delete functionality for Projects, Tasks, Developers, and Task Assignments.
* **JWT Authentication**: Secure token-based authentication system.
* **OAuth2 Integration**: Login with Google and GitHub accounts.
* **Role-Based Access Control**: Different permission levels for users.
* **Email Notifications**: Automated email alerts with robust retry mechanisms and timeout configurations.
* **Scheduled Tasks**: Cron jobs running every 5 minutes to scan for upcoming deadlines and send notifications.
* **Caching with Redis**: Improves performance for frequently accessed data with statistics and TTL management.
* **Audit Logging with MongoDB**: Records all significant changes and actions within the system for traceability.
* **Dynamic Project Filtering**: Filter projects by name, description, status, deadlines, and creation dates using Spring Data JPA Specifications.
* **Pagination and Sorting**: Retrieve paginated and sorted lists of entities.
* **Custom Queries**: Identify top developers by task assignments and overdue tasks.
* **Global Exception Handling**: Provides consistent error responses for various exceptions.
* **DTO Mapping**: Separate DTOs for data transfer, ensuring clear API contracts.
* **API Documentation**: Integrated with Springdoc OpenAPI for interactive API documentation (Swagger UI).
* **Comprehensive Performance Monitoring**: Multi-tool monitoring stack with JMeter, JProfiler, Grafana+Prometheus, and VisualVM.
* **Health Checks**: Built-in health monitoring endpoints.
* **Response Compression**: Automatic compression for improved performance.

## Technologies Used

* **Spring Boot**: 3.5.0
* **Java**: 21
* **Gradle**: Build automation
* **PostgreSQL**: 15-alpine - Relational database for core project, task, and developer data.
* **MongoDB**: 6.0 - NoSQL database for audit logs.
* **Redis**: 7.0-alpine - In-memory data store for caching.
* **Spring Security**: Authentication and authorization framework.
* **Spring Data JPA**: For PostgreSQL interactions.
* **Spring Data MongoDB**: For MongoDB interactions.
* **Spring Data Redis**: For Redis interactions and caching.
* **JWT (JSON Web Tokens)**: For stateless authentication.
* **OAuth2**: For third-party authentication (Google, GitHub).
* **Spring Boot Mail**: For email notifications with retry mechanisms.
* **Spring Scheduler**: For automated tasks and cron jobs.
* **Spring Boot Actuator**: For application monitoring and metrics.
* **Lombok**: Reduces boilerplate code.
* **Springdoc OpenAPI UI**: For API documentation (Swagger UI).
* **Docker**: Containerization with multi-stage builds.
* **JMeter**: Performance and load testing.
* **JProfiler**: Advanced Java profiling and analysis.
* **Grafana**: Metrics visualization and dashboards.
* **Prometheus**: Metrics collection and monitoring.
* **VisualVM**: JVM monitoring and profiling.

## Prerequisites

Before running the application, ensure you have the following installed:

* **Java Development Kit (JDK)**: Version 21
* **Gradle**: (Usually comes bundled with Spring Boot projects or can be installed separately)
* **Docker** and **Docker Compose**: For easily setting up PostgreSQL, MongoDB, and Redis.
* **JMeter**: For performance testing and load testing.
* **JProfiler**: For advanced Java profiling (optional, commercial license required).
* **Grafana**: For metrics visualization and dashboards.
* **Prometheus**: For metrics collection.
* **VisualVM**: For JVM monitoring and profiling.
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
    # Start only the databases for local development
    docker compose up -d postgresql mongodb redis
    ```

    This will start:
    - PostgreSQL on `5433` (mapped from container port 5432)
    - Redis on `6380` (mapped from container port 6379)
    - MongoDB on `27018` (mapped from container port 27017)

3.  **Environment Variables Setup:**
    Create a `.env` file in the project root with the following variables:

    ```env
    # Database Configuration
    postgres_user=postgres
    postgres_password=your_postgres_password

    # JWT Configuration
    secretJwtString=your-256-bit-secret-key-here

    # OAuth2 Configuration
    GOOGLE_CLIENT_ID=your-google-client-id
    GOOGLE_CLIENT_SECRET=your-google-client-secret
    GITHUB_CLIENT_ID=your-github-client-id
    GITHUB_CLIENT_SECRET=your-github-client-secret

    # Email Configuration
    gmail_account=your-email@gmail.com
    gmail_password=your-app-password
    ```

4.  **Configure OAuth2 Applications:**

    **Google OAuth2:**
    - Go to [Google Cloud Console](https://console.cloud.google.com/)
    - Create a new project or select existing one
    - Enable Google+ API
    - Create OAuth2 credentials
    - Add `http://localhost:3232/login/oauth2/code/google` to authorized redirect URIs

    **GitHub OAuth2:**
    - Go to GitHub Settings > Developer settings > OAuth Apps
    - Create a new OAuth App
    - Set Authorization callback URL to `http://localhost:3232/login/oauth2/code/github`

5.  **Update local database configuration:**
    If running databases via Docker Compose, update your local database ports in `application.properties`:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5433/project_tracker
    spring.data.mongodb.uri=mongodb://localhost:27018/project_tracker_log
    spring.data.redis.port=6380
    ```

6.  **Run the application:**
    You can run the Spring Boot application using Gradle:

    ```bash
    ./gradlew bootRun
    ```

    The application will start on `http://localhost:3232`.

### Running with Docker Compose

For a fully containerized environment, you can use Docker Compose:

1.  **Create environment file:**
    Ensure your `.env` file is properly configured as shown above.

2.  **Start all services:**

    ```bash
    docker compose up --build -d
    ```

    This will start:
    - **Application**: Available at `http://localhost:3333`
    - **PostgreSQL**: Available at `localhost:5433`
    - **MongoDB**: Available at `localhost:27018`
    - **Redis**: Available at `localhost:6380`
    - **JMX JProfiler**: Available at `localhost:9010`

3.  **View logs:**

    ```bash
    # View all services logs
    docker compose logs -f

    # View specific service logs
    docker compose logs -f app
    ```

4.  **Stop services:**

    ```bash
    docker compose down
    ```

## Configuration

### Database Configuration

The main configuration is in `src/main/resources/application.properties`:

```properties
spring.application.name=ProjectTracker
server.port=3232

# PostgreSQL connection
spring.datasource.url=jdbc:postgresql://localhost:5432/project_tracker
spring.datasource.username=${postgres_user}
spring.datasource.password=${postgres_password}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update

# MongoDB connection
spring.data.mongodb.uri=mongodb://localhost:27017/project_tracker_log

# Redis configuration
spring.cache.type=redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.cache.redis.cache-null-values=false
spring.cache.redis.key-prefix=yourApp:
spring.cache.redis.time-to-live=1h
spring.data.redis.client-type=lettuce
```

### Security Configuration

```properties
# JWT Configuration
secretJwtString=${secretJwtString}

# OAuth2 Configuration
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.provider.google.token-uri=https://oauth2.googleapis.com/token
spring.security.oauth2.client.provider.google.authorization-uri=https://accounts.google.com/o/oauth2/auth
spring.security.oauth2.client.provider.google.user-info-uri=https://www.googleapis.com/oauth2/v3/userinfo
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/google

spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=user:email,read:user
spring.security.oauth2.client.registration.github.redirect-uri={baseUrl}/login/oauth2/code/github
```

### Email Configuration

The application now includes robust email configuration with retry mechanisms and timeout settings:

```properties
# SMTP Connection
spring.mail.host=smtp.gmail.com
spring.mail.port=465
spring.mail.username=${gmail_account}
spring.mail.password=${gmail_password}

# Timeout Settings
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.properties.mail.smtp.connectiontimeout=5000

# Retry Settings
spring.mail.properties.mail.smtp.retry.enable=true
spring.mail.properties.mail.smtp.maxretries=3
spring.mail.properties.mail.smtp.initialretrydelay=1000
spring.mail.properties.mail.smtp.retryinterval=2000

# SSL/Auth Configuration
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.socketFactory.port=465
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
spring.mail.properties.mail.smtp.socketFactory.fallback=false
spring.mail.properties.mail.smtp.ssl.enable=true
```

### Monitoring Configuration

```properties
# Response compression
server.compression.enabled=true
server.compression.mime-types=text/html,text/xml,text/plain,text/css,application/javascript,application/json
server.compression.min-response-size=1024

# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus,heapdump
management.endpoint.metrics.access=read_only
management.prometheus.metrics.export.enabled=true
management.endpoint.heapdump.access=read_only

# Cache statistics
spring.cache.redis.enable-statistics=true
```

## Security Features

### JWT Authentication

The application uses JWT (JSON Web Tokens) for stateless authentication:

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

2. **Use token in API calls:**
   ```bash
   GET /api/projects
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

### OAuth2 Integration

The application supports OAuth2 login with Google and GitHub using dynamic redirect URIs:

**Google OAuth2 Login:**
```
GET /oauth2/authorization/google
```

**GitHub OAuth2 Login:**
```
GET /oauth2/authorization/github
```

**OAuth2 Callback URLs:**
- Google: `{baseUrl}/login/oauth2/code/google`
- GitHub: `{baseUrl}/login/oauth2/code/github`

Where `{baseUrl}` is automatically resolved by Spring Security based on the request.

### API Security

**Protected Endpoints:**
- All `/api/**` endpoints require authentication
- Public endpoints: `/api/auth/**`, `/oauth2/**`, `/swagger-ui/**`, `/actuator/health`

## Event Scheduling

### Email Notifications

The application automatically sends email notifications with improved reliability:
- **Robust Retry Mechanism**: Up to 3 retry attempts with configurable delays
- **Timeout Protection**: Connection, read, and write timeouts to prevent hanging
- **SSL Security**: Secure email transmission over SSL
- **Deadline Reminders**: Sent 24 hours before task/project deadlines
- **Overdue Alerts**: Sent for tasks/projects past their deadlines
- **Assignment Notifications**: Sent when tasks are assigned to developers
- **Status Updates**: Sent when project/task status changes

### Cron Job Configuration

**Deadline Scanner Job:**
Runs every 5 minutes to check for upcoming deadlines and overdue items with improved email delivery reliability.

## Monitoring and Performance

### Multi-Tool Performance Monitoring

The application includes comprehensive monitoring capabilities using multiple professional tools:

**JMeter Integration:**
- Performance and load testing
- API endpoint stress testing
- Response time analysis
- Concurrent user simulation

**JProfiler Integration:**
- Advanced Java profiling
- Memory leak detection
- CPU hotspot analysis
- Database connection monitoring

**Grafana + Prometheus Integration:**
- Real-time metrics visualization
- Custom dashboard creation
- Alert configuration
- Historical data analysis
- System performance trends

**VisualVM Integration:**
- JVM monitoring and profiling
- Heap dump analysis
- Thread monitoring
- GC analysis

**JMX Configuration:**
- Port: `9010`
- No authentication required (development only)
- Accessible via `host.docker.internal` from monitoring tools

### Metrics and Health Checks

**Available Endpoints:**
- `/actuator/health`: Application health status
- `/actuator/info`: Application information
- `/actuator/metrics`: Application metrics
- `/actuator/prometheus`: Prometheus-formatted metrics
- `/actuator/heapdump`: Heap dump for analysis

**Cache Statistics:**
Redis cache statistics are enabled to monitor cache performance and hit ratios.

## Database Schema

The relational database (PostgreSQL 15) schema is managed by Spring Data JPA and Hibernate based on the entity definitions.

### Entities

Based on the actual entity implementations:

* **User** (Base Entity): Represents a system user with authentication details.
    * `id` (PK, Long)
    * `firstName`
    * `lastName`
    * `password` (excluded from toString)
    * `email` (Unique, not null)
    * `phoneNumber`
    * `refreshToken` (excluded from toString)
    * `userRole` (Enum: `ADMIN`, `MANAGER`, `DEVELOPER`)

* **Admin** (extends User): Represents an administrator.
    * `adminLevel` (String)
    * Inherits all User fields
    * Uses `JOINED` inheritance strategy
    * Discriminator value: "ADMIN"

* **Manager** (extends User): Represents a project manager.
    * `department` (String)
    * Inherits all User fields
    * Uses `JOINED` inheritance strategy
    * Discriminator value: "MANAGER"
    * Note: Constructor currently sets role to ADMIN (potential bug to fix)

* **Developer** (extends User): Represents a developer.
    * `skill` (Enum: DevSkills - `FRONTEND`, `BACKEND`, `DEVOPS`, `QA`, etc.)
    * `taskAssignments` (One-to-Many relationship with `TaskAssignment`)
    * Inherits all User fields
    * Uses `JOINED` inheritance strategy
    * Discriminator value: "DEVELOPER"

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

### Audit Logs (MongoDB 6.0)

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

**Local Development:**
[http://localhost:3232/swagger-ui.html](http://localhost:3232/swagger-ui.html)

**Docker Compose:**
[http://localhost:3333/swagger-ui.html](http://localhost:3333/swagger-ui.html)

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
    * `GET /login/oauth2/code/google`: Google OAuth2 callback endpoint
    * `GET /login/oauth2/code/github`: GitHub OAuth2 callback endpoint

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

The application leverages Spring Cache with Redis 7.0 as the caching provider with enhanced configuration:

* **Cache Configuration:**
  - TTL: 1 hour for cached entries
  - Key prefix: `yourApp:` for namespace isolation
  - Null values are not cached
  - Statistics enabled for monitoring
  - Lettuce client for better performance

* **Cached Operations:**
  - Project details (`@Cacheable`, `@CachePut`, `@CacheEvict`)
  - JWT tokens for quick validation
  - User session data for improved authentication performance

## Audit Logging

Audit logs are stored in MongoDB 6.0. The `AuditLogService` is responsible for recording various actions, including authentication events, entity changes, and system activities.

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
* **Email Service Errors**: SMTP timeouts, authentication failures with retry mechanisms

## Building the Project

The project uses a multi-stage Docker build for optimized production images:

**Build locally:**
```bash
./gradlew clean build
```

**Build Docker image:**
```bash
docker build -t project-tracker:latest .
```

**Build and run with Docker Compose:**
```bash
docker compose up --build -d
```

The Dockerfile uses:
- `eclipse-temurin:21-jdk-jammy` for the build stage
- `eclipse-temurin:21-jre-jammy` for the runtime stage
- Multi-stage build for smaller production images
- Gradle wrapper for consistent builds

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
- Email service tests (with mock SMTP server and retry testing)
- Scheduler tests for cron jobs
- Cache performance tests
- Multi-tool profiling tests

**Performance Testing with Multiple Tools:**
Use the comprehensive monitoring stack to profile the application:

1. **JMeter Load Testing:**
   - Create test plans for API endpoints
   - Simulate concurrent users
   - Monitor response times and throughput
   - Generate performance reports

2. **JProfiler Analysis:**
   - Connect to the running application
   - Analyze memory usage patterns
   - Identify CPU bottlenecks
   - Monitor database connection pools

3. **Grafana + Prometheus Monitoring:**
   - Access Grafana dashboards for real-time metrics
   - Monitor application performance trends
   - Set up alerts for critical thresholds
   - Track business metrics and system health

4. **VisualVM Profiling:**
   - Start the application with Docker Compose
   - Connect VisualVM to `host.docker.internal:9010`
   - Monitor heap dumps and thread behavior
   - Analyze GC performance

**Testing Email Retry Mechanisms:**
The email service now includes comprehensive retry testing to ensure robustness against network issues and SMTP server problems.