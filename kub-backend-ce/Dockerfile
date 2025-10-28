# ===== build stage =====
ARG JDK_VERSION=25
FROM maven:3.9-eclipse-temurin-${JDK_VERSION} AS build
WORKDIR /src

# Cache deps first
COPY pom.xml .
RUN mvn -q -U -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -q -DskipTests package

# ===== runtime stage =====
FROM eclipse-temurin:${JDK_VERSION}-jre
WORKDIR /app

# Non-root user
RUN useradd -r -u 10001 appuser && mkdir -p /config && chown -R appuser:appuser /config

# App
COPY --from=build /src/target/kub-backend-ce-0.0.1-SNAPSHOT.jar /app/app.jar

# Tell Spring to also load /config/secrets.properties if present.
# (maps to `spring.config.import=optional:file:/config/secrets.properties,optional:vault://`)
ENV SPRING_CONFIG_IMPORT="optional:file:/config/secrets.properties,optional:vault://" \
    JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

USER appuser

# expose your HTTP port only if you run web/actuator; harmless otherwise
EXPOSE 8080 9999

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]