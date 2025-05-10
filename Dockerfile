# Usa una versione di Maven con OpenJDK 17 come base per la build
FROM maven:3-openjdk-17 AS build

# Copia tutto il codice sorgente nel container
COPY . .

# Esegui la build del progetto e crea il JAR, saltando i test
RUN mvn clean package -DskipTests

# Usa una versione di Java 17 (Alpine per ridurre le dimensioni dell'immagine finale)
FROM eclipse-temurin:17-alpine

# Copia il JAR costruito dalla fase di build
COPY --from=build /target/*.jar streamingPlatform-0.0.1-SNAPSHOT.jar

# Esponi la porta 8080, su cui l'applicazione Spring Boot sarà in ascolto
EXPOSE 8080

# Definisci l'entry point per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "streamingPlatform-0.0.1-SNAPSHOT.jar"]
