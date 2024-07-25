FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY . .
RUN --mount=type=cache,target=/root/.m2,rw ./mvnw clean package -DskipTests

FROM openjdk:17
WORKDIR /app

COPY --from=build /app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
