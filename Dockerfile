# Etapa 1: Build
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# Etapa 2: Runtime
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]