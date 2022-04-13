FROM maven AS MAVEN_BUILD
COPY pom.xml /webapp/
COPY src /webapp/src/
WORKDIR /webapp/
RUN mvn clean package

FROM openjdk:11
COPY --from=MAVEN_BUILD /webapp/target/*.jar pos-app.jar
ENTRYPOINT ["java", "-jar", "/pos-app.jar"]