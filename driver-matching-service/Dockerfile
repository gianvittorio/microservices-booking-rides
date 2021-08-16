FROM openjdk:11

WORKDIR /opt/driver-matching-service

COPY ./target/driver-matching-service-0.0.1-SNAPSHOT.jar ./driver-matching-service.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom",  "-jar", "./driver-matching-service.jar"]
