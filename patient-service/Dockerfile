# Use an official Maven image as a build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .

# Download the project dependencies, -B for batch mode
RUN mvn dependency:go-offline -B

# Copy the entire project source code to the container
COPY src ./src

# clean and package the application, skipping tests for faster build
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre AS runner

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the builder stage
COPY --from=builder /app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the application port
EXPOSE 4000

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]