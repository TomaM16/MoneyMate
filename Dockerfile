# Use the official Maven image as the base image
FROM maven:3.9.6-eclipse-temurin-17-alpine

# Set the working directory
WORKDIR /usr/src/app

# Copy the entire project to the container
COPY . .

# Download the Maven dependencies
RUN mvn dependency:go-offline

# Build the application
RUN mvn package

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "target/moneymate.jar"]
