FROM openjdk:17
COPY build/libs/*.jar app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-Xmx1024m","-jar","app.jar"]
