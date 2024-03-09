FROM openjdk:21
ARG JAR_FILE=target/*.jar
ADD ./target/BicycleRental-0.0.1-SNAPSHOT.jar bicyclerental.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/bicyclerental.jar"]