FROM openjdk:21-oracle
EXPOSE 9000
COPY target/ApiGateway.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]