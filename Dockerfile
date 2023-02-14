FROM openjdk:17-jdk-alpine
COPY /build/libs/smartix-task.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "smartix-task.jar"]