# syntax=docker/dockerfile:1

# ---- Build stage -------------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy the wrapper and POM first so dependency resolution is cached
# until pom.xml actually changes.
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B -ntp dependency:resolve

# Then build the application itself.
COPY src src
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B -ntp clean package -DskipTests \
    && mv target/*.jar /app/app.jar

# ---- Runtime stage -----------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Run as an unprivileged user; /data is where the H2 file database lives.
RUN addgroup -S app && adduser -S -G app app \
    && mkdir -p /data && chown app:app /data

COPY --from=build --chown=app:app /app/app.jar app.jar

USER app
EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=12 \
    CMD wget -q --spider http://127.0.0.1:8080/api/v1/tasks || exit 1

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
