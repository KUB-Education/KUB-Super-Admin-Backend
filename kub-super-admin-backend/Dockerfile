FROM maven:3.8.5-openjdk-17 AS DEPENDENCIES
COPY pom.xml .
# resolve and download all dependencies specified in copied pom.xml file
RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline #-DexcludeArtifactIds=domain

# build the jar
FROM maven:3.8.5-openjdk-17 AS BUILDER
# copying the necessary resources (dependencires, code)
COPY --from=DEPENDENCIES . .
COPY /src /src
RUN mvn -B -e clean install -DskipTests # compile the code and build JAR file, skip running tests

FROM openjdk:17-slim
# prepeare runtime env

COPY --from=BUILDER /target/*.jar /app/*.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/*.jar"]

# source: https://www.baeldung.com/docker-maven-build-multi-module-projects