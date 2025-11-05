FROM openjdk:24-jdk-slim
ARG JAR_FILE=target/SIS-0.0.1.jar
COPY ${JAR_FILE} app_SIS.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app_SIS.jar"]