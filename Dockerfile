# Use an official OpenJDK image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy build jar (adjust path if needed)
COPY build/libs/*.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]