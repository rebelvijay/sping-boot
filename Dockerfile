# Use official OpenJDK runtime
FROM eclipse-temurin:21-jdk

# Set working directory inside container
WORKDIR /app

# Copy the built JAR file from target folder
COPY target/*.jar app.jar

# Expose application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]