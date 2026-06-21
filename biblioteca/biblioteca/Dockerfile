# Paso 1: Construcción (Build) usando Maven con JDK 17
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias
COPY pom.xml .

# Descargar las dependencias para acelerar construcciones futuras
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el archivo JAR saltando los tests (ya los aprobamos)
COPY src ./src
RUN mvn clean package -DskipTests

# Paso 2: Imagen final ligera para ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde el paso anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto en el que corre tu aplicación Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]