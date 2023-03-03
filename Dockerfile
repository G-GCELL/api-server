FROM openjdk:17
COPY build/libs/*.jar app/app.jar
WORKDIR /app
ENTRYPOINT ["java","-jar","app.jar"]
