# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Install maven and build
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
