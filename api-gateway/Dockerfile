FROM openjdk:11

WORKDIR /opt/discovery-service

COPY ./target/api-gateway-0.0.1-SNAPSHOT.jar ./api-gateway.jar

EXPOSE 8082

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom",  "-jar", "./api-gateway.jar"]
