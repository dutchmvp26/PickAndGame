FROM gradle:9.7.1-jdk17 AS build
COPY . .
RUN gradle bootJar
FROM eclipse-temurin:17-jdk
COPY --from=build /build/libs/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]   