# Multi-stage build: Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Build the application
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/finance-tracker-*.jar app.jar

# Health check (optional but recommended)
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Expose port (default 8080, can be overridden with PORT env var at runtime)
EXPOSE 8080

# Set environment variable for Spring Boot server port
# Default to 8080 if PORT is not set
ENV PORT=8080
ENV SPRING_BOOT_OPTS=""

# Run the application
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} ${SPRING_BOOT_OPTS} -jar app.jar"]
