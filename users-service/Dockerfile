FROM openjdk:11

WORKDIR /opt/users-service

COPY ./target/users-service-0.0.1-SNAPSHOT.jar ./users-service.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom",  "-jar", "./users-service.jar"]
