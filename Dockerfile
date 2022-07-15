FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /src

COPY . .
RUN mvn clean package


FROM openjdk:11 AS final
WORKDIR /opt
EXPOSE 4100
COPY --from=build /src/target/*.jar iot-server.jar
ENTRYPOINT ["java", "-Xmx512m","-Xms512m", "-jar", "iot-server.jar"]