FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/payments-0.0.1-SNAPSHOT.jar payments.jar
ENTRYPOINT ["java","-jar","/payments.jar"]