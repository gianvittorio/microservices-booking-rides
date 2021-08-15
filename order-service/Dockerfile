FROM openjdk:11

WORKDIR /opt/order-service

COPY ./target/order-service-0.0.1-SNAPSHOT.jar ./order-service.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom",  "-jar", "./order-service.jar"]
