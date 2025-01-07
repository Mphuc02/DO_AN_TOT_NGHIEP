FROM openjdk:21
EXPOSE 8090
COPY target/ChatService-0.0.1-SNAPSHOT.jar ChatService.jar
ENTRYPOINT ["java", "-jar", "ChatService.jar"]