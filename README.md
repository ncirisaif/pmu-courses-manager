# Course Manager Application

This application is project designed to manage courses and participants for horse races. 
It follows the Outbox pattern for event-driven architecture and uses Testcontainers for integration testing.

## Prerequisites

- Java 21
- maven 3+
- Docker (for Testcontainers, postgresql and kafka)

To build and run the application:

```bash
mvn clean install && mvn spring-boot:run
```
Swagger URL: http://localhost:8080/swagger-ui/index.html


- This application implements the **Outbox Pattern** for event-driven communication. Events are persisted in an **Outbox** table in the database before being published to Kafka.
-  A scheduler runs every 5 seconds to check the Outbox table for new events. If events are found, they are published to Kafka using a Kafka producer.
- The application contains a Kafka consumer that consumes the events from Kafka and prints the event data to the console.