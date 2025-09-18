# ========== Stage 1: Build ==========
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# copy source + pom.xml
COPY . .

# build jar
RUN mvn clean package -DskipTests

# ========== Stage 2: Run ==========
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# copy jar từ builder
COPY --from=builder /app/target/*.jar app.jar

# UserService chạy port 8083
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
