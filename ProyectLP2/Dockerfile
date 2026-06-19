# Estructura multi-etapa para compilar rápido
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Imagen ligera para ejecutar la aplicación
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/ProyectLP2-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]