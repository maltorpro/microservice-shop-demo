FROM openjdk:8-jdk-alpine

EXPOSE 8080

ADD ./build/libs/*.jar app.jar
ADD ./src/main/resources/cert.p12 src/main/resources/cert.p12

ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar /app.jar