FROM maven:3.8.4-openjdk-17

COPY . /usr/src/app/service-registry
WORKDIR /usr/src/app/service-registry

RUN mvn clean package

CMD ["java", "-jar", "target/service-registry.jar"]