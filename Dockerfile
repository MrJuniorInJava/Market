FROM openjdk:17
ADD /target/Market-0.0.1-SNAPSHOT.jar backend.jar
CMD ["java","-jar","backend.jar"]
EXPOSE 8080
