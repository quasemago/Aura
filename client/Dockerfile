FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .
RUN --mount=type=cache,target=/root/.m2,rw mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
