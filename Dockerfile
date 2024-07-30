# Stage 1: Build the application
FROM maven:latest AS build
WORKDIR /app
# Copy the Maven wrapper and other necessary files
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Copy the rest of the application code
COPY src ./src
RUN ls -l


# Run Maven build
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

