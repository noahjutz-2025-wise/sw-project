FROM docker.io/library/eclipse-temurin:25-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw package

FROM docker.io/library/eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
