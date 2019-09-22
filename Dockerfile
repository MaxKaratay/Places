FROM java:8
EXPOSE 8080
ADD target/Places-api.jar Places-api.jar
ENTRYPOINT ["java", "-jar", "Places-api.jar"]