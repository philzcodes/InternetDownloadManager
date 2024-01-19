
#
# Build stage
#
FROM maven:3.9.1-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package

#
# Package stage
#
FROM eclipse-temurin:17-jdk
COPY --from=build /target/IDM-App-0.0.1-SNAPSHOT.jar IDM-App.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","IDM-App.jar"]



