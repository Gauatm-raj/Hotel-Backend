## Stage 1: Build the application
#FROM maven:3.9.0-eclipse-temurin-17 AS build
#WORKDIR /app
#
## Copy the Maven project files
#COPY pom.xml ./
#COPY src ./src
#COPY mvnw ./
#COPY .mvn ./.mvn
#
## Make the Maven Wrapper script executable and build the application
#RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests
#
## Stage 2: Run the application using JDK 21
#FROM eclipse-temurin:17-jdk-alpine
#WORKDIR /app
#
## Copy the built application from the build stage
#COPY --from=build /app/target/*.jar ./app.jar
#
## Command to run the application
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]
#
#
#

# Use the official JDK 21 base image
FROM eclipse-temurin:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build files
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apt-get update && apt-get install -y maven

# Package the application
RUN mvn clean package

# Copy the JAR file to the container
COPY target/*.jar app.jar

# Specify the command to run the application
CMD ["java", "-jar", "app.jar"]

