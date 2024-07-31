# Stage 1: Build the application
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the Maven project files
COPY pom.xml ./
COPY src ./src
COPY mvnw ./
COPY .mvn ./.mvn

# Make the Maven Wrapper script executable and build the application
RUN mvn -B -DskipTests clean install

# Stage 2: Run the application using JDK 21
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/target/*.jar ./app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
