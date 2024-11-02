FROM openjdk:21-oracle
EXPOSE 9000
COPY target/ApiGateway-0.0.1-SNAPSHOT.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]