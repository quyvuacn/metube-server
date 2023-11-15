FROM maven:3.8.4-openjdk-17

COPY . /usr/app/video-service
WORKDIR /usr/app/video-service

RUN mvn clean package

CMD ["java", "-jar", "target/video-service.jar"]