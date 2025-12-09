# Multi-stage build for optimized image size

# Stage 1: Build
# FROM eclipse-temurin:21-jdk-alpine AS build

# WORKDIR /app

# Copy Maven wrapper and pom.xml
# COPY .mvn/ .mvn
# COPY mvnw pom.xml ./

# Download dependencies (cached layer)
# RUN ./mvnw dependency:go-offline

# Copy source code
# COPY src ./src

# Build application
# RUN ./mvnw clean package -DskipTests

# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package -DskipTests

# # Runtime stage
# FROM eclipse-temurin:21-jre
# WORKDIR /app
# COPY --from=build /app/target/currency-converter-0.0.1-SNAPSHOT.jar app.jar

# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]


# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8085

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8085/actuator/health || exit 1

# Set JVM options
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
