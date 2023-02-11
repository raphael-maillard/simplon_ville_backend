FROM openjdk:latest
COPY target/api.jar api.jar
ENTRYPOINT ["java","-jar","/api.jar"]

#FROM ubuntu
#COPY api/target/api.jar api.jar
#RUN  apt-get update && apt-get install -y default-jdk mysql-client

