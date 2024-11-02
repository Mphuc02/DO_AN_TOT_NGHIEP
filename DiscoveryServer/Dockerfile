FROM openjdk:21-oracle
EXPOSE 8761
COPY target/DiscoveryServer-0.0.1-SNAPSHOT.jar discovery-server.jar
ENTRYPOINT ["java", "-jar", "discovery-server.jar"]