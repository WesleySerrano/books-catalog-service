# Stage 1: Build and package the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy only the dependency definition file to cache downloaded libraries
COPY pom.xml .

RUN apk add --no-cache libc6-compat
# Download dependencies in offline mode (saves time on subsequent builds)
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Package the application, skipping unit tests for faster build times
RUN mvn clean package -DskipTests

# Stage 2: Create the minimal runtime image
FROM eclipse-temurin:21-jre-alpine

# Create a non-root system user for security purposes
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set the working directory for the runtime container
WORKDIR /app

# Copy the compiled JAR file from the builder stage and rename it
COPY --from=builder /app/target/*.jar app.jar

# Ensure the non-root user owns the application files
RUN chown -R appuser:appgroup /app

# Switch to the non-root user
USER appuser

# Expose the default Spring Boot port
EXPOSE 8081
EXPOSE 9090

# Configure JVM flags for efficient container memory usage and run the application
ENTRYPOINT ["java", "-XX:+UseG1GC", "-jar", "app.jar"]
