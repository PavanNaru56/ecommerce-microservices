#image
FROM eclipse-temurin:17-jre
#Cur working dir in Docker container
WORKDIR /app
#copy the jar into the container
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
#port
EXPOSE 8081
#Whenever the container start which jar to be triggered
ENTRYPOINT ["java", "-jar", "app.jar"]
