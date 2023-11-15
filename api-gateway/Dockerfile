FROM maven:3.8.4-openjdk-17

COPY . /usr/app/api-gateway
WORKDIR /usr/app/api-gateway

RUN mvn clean package

CMD ["java", "-jar", "target/api-gateway.jar"]