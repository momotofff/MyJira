# Container for build 
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy pom.xml to container and download it's dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src
COPY swagger ./swagger

# Build without tests
RUN mvn clean package -DskipTests

# Container for application
FROM openjdk:17-jdk-slim AS application
WORKDIR /app

# Copy built app from build container
COPY --from=build /app/target/swagger-spring-1.0.0.jar /app/swagger-spring.jar
EXPOSE 9000

# Run app
ENTRYPOINT ["java", "-jar", "swagger-spring.jar"]
