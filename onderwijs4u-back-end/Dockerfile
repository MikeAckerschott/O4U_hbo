# Build the application
FROM maven:3.9.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/priemtester-0.0.1-SNAPSHOT.jar .
RUN apk add --no-cache curl
CMD ["java", "-jar", "priemtester-0.0.1-SNAPSHOT.jar"]
