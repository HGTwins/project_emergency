# 1. 빌드 스테이지
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
# 실행 권한 부여 및 빌드 (테스트 제외)
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# 2. 실행 스테이지
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# 메이븐은 빌드 결과물이 target 폴더에 생성됩니다.
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
