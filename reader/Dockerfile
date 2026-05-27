FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY shared/ shared/
COPY writer/ writer/
COPY reader/ reader/
COPY cleaner/ cleaner/

RUN chmod +x gradlew && ./gradlew :reader:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/reader/build/libs/reader.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
