FROM docker.io/alpine/git:latest AS fetch
WORKDIR /app
RUN apk add --no-cache git-lfs && \
    git lfs install
RUN git clone https://github.com/noahjutz-2025-wise/sw-project . && \
    git lfs pull

FROM docker.io/library/eclipse-temurin:25-alpine AS build
WORKDIR /app
COPY --from=fetch /app .
RUN ./mvnw package

FROM docker.io/library/eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
