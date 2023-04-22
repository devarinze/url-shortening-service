FROM openjdk:8-jdk-alpine

# JRE fails to load fonts if there are no standard fonts in the image; DejaVu is a good choice,
RUN apk add --no-cache fontconfig ttf-dejavu

VOLUME /tmp
COPY target/*.jar tinyurl.jar
ENTRYPOINT ["java","-jar","/tinyurl.jar"]
