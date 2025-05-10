FROM eclipse-temurin:17-jdk
COPY jar/streamingPlatform-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
