FROM openjdk:24-jdk-slim
ARG JAR_FILE=target/sis-0.0.1.jar
COPY ${JAR_FILE} app_sis.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app_sis.jar"]