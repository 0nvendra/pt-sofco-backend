# Step 1: Build menggunakan Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run menggunakan Java 17
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-Xmx512m","-jar","app.jar"]