FROM openjdk:11
ARG JAR_FILE=/target/*.jar
COPY $JAR_FILE pos-app.jar
ENTRYPOINT ["java", "-jar", "/pos-app.jar"]