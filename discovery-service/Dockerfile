FROM openjdk:11

WORKDIR /opt/discovery-service

COPY ./target/discovery-service-0.0.1-SNAPSHOT.jar ./discovery-service.jar

EXPOSE 8761

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom",  "-jar", "./discovery-service.jar"]
