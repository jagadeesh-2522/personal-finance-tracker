# Docker Deployment Guide

This project includes a production-ready Dockerfile for deploying the Finance Tracker Spring Boot application.

## Building the Docker Image

```bash
docker build -t finance-tracker:latest .
```

## Running the Container

### Basic Usage (with default port 8080)

```bash
docker run \
  -e DB_URL=jdbc:postgresql://db-host:5432/finance_tracker \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=your_password \
  -p 8080:8080 \
  finance-tracker:latest
```

### Custom Port

```bash
docker run \
  -e PORT=3000 \
  -e DB_URL=jdbc:postgresql://db-host:5432/finance_tracker \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=your_password \
  -p 3000:3000 \
  finance-tracker:latest
```

### With Additional JVM Options

```bash
docker run \
  -e PORT=8080 \
  -e SPRING_BOOT_OPTS="-Xmx512m -Xms256m" \
  -e DB_URL=jdbc:postgresql://db-host:5432/finance_tracker \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=your_password \
  -p 8080:8080 \
  finance-tracker:latest
```

## Environment Variables

The application requires the following environment variables:

| Variable | Description | Example |
|----------|-------------|---------|
| `PORT` | Application server port | `8080` (default) |
| `DB_URL` | PostgreSQL database URL | `jdbc:postgresql://localhost:5432/finance_tracker` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | (your password) |
| `SPRING_BOOT_OPTS` | Additional JVM options | `-Xmx512m -Xms256m` (optional) |

## Docker Compose Example

Create a `docker-compose.yml` file to run the application with PostgreSQL:

```yaml
version: '3.8'

services:
  db:
    image: postgres:17
    environment:
      POSTGRES_DB: finance_tracker
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    environment:
      PORT: 8080
      DB_URL: jdbc:postgresql://db:5432/finance_tracker
      DB_USERNAME: postgres
      DB_PASSWORD: postgres
    ports:
      - "8080:8080"
    depends_on:
      - db

volumes:
  postgres_data:
```

Then run:

```bash
docker-compose up
```

## Multi-stage Build

The Dockerfile uses a multi-stage build approach:

1. **Builder Stage**: Compiles the project using Maven and JDK 21
2. **Runtime Stage**: Runs the application using the smaller JRE 21 image

This reduces the final image size by excluding build tools that aren't needed at runtime.

## Health Check

The image includes a built-in health check that verifies the application is running. Docker will automatically mark the container as unhealthy if the check fails.

## Production Recommendations

1. **Use environment variables for all secrets** (database credentials, API keys)
2. **Set appropriate JVM memory limits** via `SPRING_BOOT_OPTS`
3. **Run multiple container replicas** behind a load balancer for high availability
4. **Use a container orchestration platform** like Kubernetes for production deployments
5. **Enable logging** and monitor application health
6. **Use a private Docker registry** to store built images securely

## Troubleshooting

### Database Connection Issues

If the application fails to connect to the database:

1. Verify database credentials are correct
2. Ensure database host is reachable from the container
3. Check database URL format: `jdbc:postgresql://host:port/database`

### Port Already in Use

If port 8080 is already in use, specify a different port:

```bash
docker run -e PORT=9000 -p 9000:9000 finance-tracker:latest
```

### Viewing Application Logs

```bash
docker logs <container-id>
```

For continuous log streaming:

```bash
docker logs -f <container-id>
```
