# Stage 1 - build
FROM maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY report ./report

RUN mvn package -DskipTests


# Stage 2
FROM amazoncorretto:17.0.2

WORKDIR /app/target
COPY --from=build /app/target/*.jar app.jar

ENV DBMS_CONNECTION=''
ENV DBMS_USERNAME=''
ENV DBMS_PASSWORD=''
ENV MAIL_USERNAME=''
ENV MAIL_PASSWORD=''

ENTRYPOINT ["sh", "-c", "sleep 30 && java -jar app.jar"]
