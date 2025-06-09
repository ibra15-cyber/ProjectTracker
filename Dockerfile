FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle

COPY build.gradle settings.gradle ./

COPY src src

RUN chmod +x gradlew


RUN ./gradlew bootJar --no-daemon -x test


FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 3333

ENTRYPOINT ["java", "-jar", "app.jar"]
