# Use a base image with Java 21 (like AdoptOpenJDK)
FROM openjdk:21-jdk as build

# Set a working directory
WORKDIR /workspace/app

# Copy the Maven pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the application
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Use a smaller base image for the final layer
FROM openjdk:21-jdk

# Set a working directory
WORKDIR /app

# Copy the built application from the build layer
COPY --from=build /workspace/app/target/*.jar app.jar

# Set the startup command
ENTRYPOINT ["java","-jar","/app/app.jar"]