# 1. Aşama: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Önce sadece pom.xml'i kopyala (bağımlılıklar cache'lenir, build hızlanır)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Sonra kaynak kodu kopyala ve derle
COPY src ./src
RUN mvn -B clean package -DskipTests

# 2. Aşama: Çalıştırma
FROM eclipse-temurin:17-jre
WORKDIR /app

# Root olmayan kullanıcı ile çalıştır
RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build /app/target/*.jar app.jar
USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]