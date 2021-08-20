# Booking Rides Microservice Landscape

![Domain](https://github.com/gianvittorio/microservices-booking-rides/blob/main/images/Booking%20Rides.png)

Use the following instructions to deploy the application.

# Requirements
- [Java 11 runtime environment (SE JRE)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven 3](https://maven.apache.org/docs/history.html)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [Docker and docker-compose](https://docs.docker.com/get-docker/).

# Setup
Download or clone this repository.

    $ git clone https://github.com/gianvittorio/microservices-booking-rides.git

## Assumptions

## Data Model
![Domain](https://github.com/gianvittorio/microservices-booking-rides/blob/main/images/Domain%20Entities.png)



## The Hexagonal Architecture

We are going to structure our project following through the <strong>Hexagonal Architecture</strong>, on a bottom-up approach:
- The domain/entity, along with the very core business logic, wrapped up as a library, will be POJOS, totally independent from any web framework we might use
- The service will be a mere implementation of our use cases, which will instance and share the entity, according to our underlying business logc
- The API itself, will then be responsible for handling the requests/responses and delegate the computation to the service underneath

![Hexagonal Architecture](https://lucid.app/publicSegments/view/98f07e52-1840-45b8-b1bc-29d4a19fa4bd/image.png)
We can benefit from the above design as it decouples our system into smaller, simpler bits, separating concerns and making both development and testing each component independently. It entirely relies on the <strong>Dependency Injection Principle</strong>.
Moreover, it allows us to find a nice middleground between <strong>Domain Driven Design</strong> and <strong>Test Driven Development</strong> as we can easily write tests for our domain as we model it.


Therefore, our project tree will basically be structured as 3 main packages:
- <strong>Controller</strong>
- <strong>Service</strong>
- <strong>Repository (PostgreSQL) </strong>

## The Business Logic
- Users may register themselves, query, update and delete their own personal data, through the users/ api
- Drivers may register themselves, query, update and delete their own personal data, through the users/ api
- Users may create new orders, providing id, origin, destination and local time for the ride, as they may also query the status of an order, update it and delete it, as long as it is still pending
- The Order Service shall dispath orders within a 30 minutes timeframe, once every minute, notify user by email and update order's status

## The Implementation
As the programming language, we go with Java 11. We are picking the Spring WebFlux framework, an event-driven, reactive architecture, along with R2DBC Postgres driver connector. As opposed to Spring Web MVC, which makes use of the blocking servlet architecture (one thread per request), it will only require a number of threads equal to 2 the number of available cores, which will also lead to less memory being used by the JVM. For slow services it always runs faster than its blocking counterpart. The downside is: the client must also be non-blocking, making use of asynchronous calls/drivers.

## Running The Project
First of all, provide a suitable <strong>@gmail address</strong> and <strong>password</strong> by replacing the values for the environment variables <strong>app.mail.sender.username</strong> and <strong>app.mail.sender.password at lines 41 and 42 within docker-compose.yml.
Then, we run the following script, to building a fat jar for each and every service and building a docker image out of them, only to spin the whole thing, attaching 3 volumes for the Postgres containers and an underlying network.
```console
bash set_up.sh
```

The whole thing shall take a handful of minutes. You may check on the overall progress by typing:
```console
docker-compose logs --tail 1 --follow <service-name>
```
On either discovery-service or order-service, as they play a key role across our landscape.

As soon as we are up and running, we may access http://localhost:8761, in order to verify all our services are registered, API gateway included.
You may be able to access the 3 API' documentation on:
- http://localhost:8082/orders-service/swagger-ui/
- http://localhost:8082/users-service/swagger-ui/
- http://localhost:8082/drivers-matching-service/swagger-ui/

You may also connect to any of the 3 PostgreSQL containers at the below URL's:
- http://localhost:5433 (order-service db)
- http://localhost:5434 (users-service db)
- http://localhost:5435 (drivers-matching service db)

We strongly suggest [DBeaver](https://dbeaver.io/download/), which comes with quite a friendly GUI.

Please, follow up with the usecase down below (guide yourself through the example DTO's provided with the Swagger documentation):
1 - Create a user (/users api);
2 - Create a driver (/drivers-matching api);
3 - Book a ride (/orders api)
4 - Expect an email from noreply@booking-rides.com. The subject will be the invoice number and the very invoice as the body (json format)


You may finally take the whole cluster down, gracefully, by running:
```console
bash tear_down.sh
```
