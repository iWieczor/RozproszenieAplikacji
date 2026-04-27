FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY shared/ shared/
COPY writer/ writer/
COPY reader/ reader/

RUN chmod +x gradlew && ./gradlew :writer:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/writer/build/libs/writer.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
