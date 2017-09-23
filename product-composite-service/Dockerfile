FROM openjdk:8-jre-alpine

EXPOSE 8080

ADD ./build/libs/*.jar app.jar
ADD ./app.conf app.conf

ENTRYPOINT ["java","-jar","/app.jar"]
