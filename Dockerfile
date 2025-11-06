# ---- build ----
FROM gradle:8.9-jdk21 AS builder
WORKDIR /workspace
COPY gradle gradle
COPY gradlew .
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY src src
RUN ./gradlew --no-daemon clean bootJar -x test

# ---- run ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
