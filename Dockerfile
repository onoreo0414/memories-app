# OpenJDK 17 ベース
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/memories-app-0.0.1-SNAPSHOT.jar"]
