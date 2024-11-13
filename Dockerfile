# Stage 1: build
FROM maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
ENV DBMS_CONNECTION=jdbc:mysql://mysql:3306/course_registration
ENV DBMS_USERNAME=root
ENV DBMS_PASSWORD=dongnv
ENV MAIL_PASSWORD='kyxk gutl wfbh ihgp'

RUN mvn package -DskipTests

# Stage 2: create image
FROM amazoncorretto:17.0.2

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]