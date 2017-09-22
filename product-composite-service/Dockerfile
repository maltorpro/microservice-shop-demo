FROM openjdk:8-jre-alpine

EXPOSE 8080

ADD ./build/libs/*.jar app.jar

# Regarding settings of java.security.egd, see http://wiki.apache.org/tomcat/HowTo/FasterStartUp#Entropy_Source
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-Djavax.net.ssl.trustStorePassword=password","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
