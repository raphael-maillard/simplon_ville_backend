#FROM openjdk:latest

FROM azul/zulu-openjdk-alpine:11
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=api/target/api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#FROM ubuntu
#COPY api/target/api.jar api.jar
#RUN  apt-get update && apt-get install -y default-jdk mysql-client

#FROM azul/zulu-openjdk-alpine:11
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

