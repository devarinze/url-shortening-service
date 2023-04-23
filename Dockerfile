FROM openjdk:8-jdk-alpine

VOLUME /tmp
COPY target/*.jar tinyurl.jar
ENTRYPOINT ["java","-jar","/tinyurl.jar"]
