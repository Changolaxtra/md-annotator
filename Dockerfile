# Use a base image with Java installed
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file (assuming you've built it)
COPY target/*.jar app.jar

# Expose the port your Spring Boot app listens on
EXPOSE 8088

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]