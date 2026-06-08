# Etapa 1: Build - Compilar la aplicación
FROM maven:3.9-eclipse-temurin-17 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY src ./src

# Descargar dependencias y compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime - Ejecutar la aplicación
FROM eclipse-temurin:17-jre-alpine

# Establecer directorio de trabajo
WORKDIR /app

# Crear directorio para EFS (almacenamiento temporal)
RUN mkdir -p /app/efs

# Crear usuario no root para seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN chown -R appuser:appgroup /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/sistema-guias.jar app.jar

# Cambiar al usuario no root
USER appuser

# Exponer el puerto de la aplicación
EXPOSE 8080

# Variables de entorno configurables
ENV AWS_REGION=us-east-1
ENV AWS_S3_BUCKET=guias-transportista
ENV EFS_MOUNT_PATH=/app/efs
ENV DB_HOST=localhost
ENV DB_USERNAME=root
ENV DB_PASSWORD=

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]