FROM maven:3.8.4-openjdk-17

COPY . /usr/src/app/video-service
WORKDIR /usr/src/app/video-service

RUN mvn clean package

CMD ["java", "-jar", "target/video-service.jar"]