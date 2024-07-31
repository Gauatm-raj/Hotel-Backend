FROM openjdk:21-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/BOOK-HOTEL-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]