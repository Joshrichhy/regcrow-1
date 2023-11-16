# First stage: Build the application
FROM maven:3.8.3 AS build
WORKDIR /usr/src/app
COPY . .
RUN mvn package -DskipTests

# Second stage: Create the final image
FROM eclipse-temurin:17

WORKDIR /app
COPY --from=build /usr/src/app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
