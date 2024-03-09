#FROM openjdk:21
#ARG JAR_FILE=target/*.jar
#ADD ./target/BicycleRental-0.0.1-SNAPSHOT.jar bicyclerental.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/bicyclerental.jar"]

FROM maven:3.8.4-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21
COPY --from=build ./target/BicycleRental-0.0.1-SNAPSHOT.jar bicyclerental.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/bicyclerental.jar"]
