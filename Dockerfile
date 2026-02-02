FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw -B -q dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Xms128m","-Xmx384m","-jar","app.jar"]
