FROM maven:latest AS build

WORKDIR /app

COPY mvnw pom.xml ./

RUN chmod +x ./mvnw



# Stage for running the application
FROM openjdk:17-slim-alpine

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
