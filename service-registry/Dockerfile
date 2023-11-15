FROM maven:3.8.4-openjdk-17

COPY . /usr/app/service-registry
WORKDIR /usr/app/service-registry

RUN mvn clean package

CMD ["java", "-jar", "target/service-registry.jar"]