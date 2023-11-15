FROM maven:3.8.4-openjdk-17

COPY . /usr/src/app/api-gateway
WORKDIR /usr/src/app/api-gateway

RUN mvn clean package

CMD ["java", "-jar", "target/api-gateway.jar"]