# ========== Stage 1: Build ==========
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# copy source code và pom.xml
COPY . .

# build project, tạo file jar trong target/
RUN mvn clean package -DskipTests

# ========== Stage 2: Run ==========
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# copy jar từ stage builder
COPY --from=builder /app/target/*.jar app.jar

# expose port 8085 (theo application.yml)
EXPOSE 8085

# command chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
