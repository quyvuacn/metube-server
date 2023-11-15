FROM maven:3.8.4-openjdk-17

COPY . /usr/src/app/personal-service
WORKDIR /usr/src/app/personal-service

RUN mvn clean package

CMD ["java", "-jar", "target/personal-service.jar"]