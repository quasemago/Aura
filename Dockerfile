FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package

FROM azul/zulu-openjdk:25-jre
WORKDIR /app

COPY --from=builder /app/target/aura-*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
