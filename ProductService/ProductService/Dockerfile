# ========== Stage 1: Build ==========
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# copy code và pom.xml
COPY . .

# build project tạo file jar
RUN mvn clean package -DskipTests

# ========== Stage 2: Run ==========
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# copy jar từ stage build
COPY --from=builder /app/target/*.jar app.jar

# expose port 8088 (theo application.yml)
EXPOSE 8088

# chạy service
ENTRYPOINT ["java", "-jar", "app.jar"]
