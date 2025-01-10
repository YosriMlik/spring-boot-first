# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR/WAR file from the target directory to the container
COPY target/*.war app.war

# Expose the port your application will run on
EXPOSE 8083

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.war"]