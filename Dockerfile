# Use a multi-stage build to reduce final image size
FROM eclipse-temurin:17-jdk-jammy AS builder
 
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src

# Give execute permission to the mvnw script
RUN chmod +x ./mvnw

RUN ./mvnw clean package -DskipTests
 
# Final stage
FROM eclipse-temurin:17-jre-jammy
 
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar

 # Allow passing profile as a build ARG (default to "local")
ARG SPRING_PROFILE=local
ENV SPRING_PROFILE=${SPRING_PROFILE}

# Activate the "prod" profile by default
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "app.jar"]


