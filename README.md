# Kafka Java Application - Producer and Consumer

This project demonstrates a Kafka setup in a microservices architecture, implemented with Java and Spring Boot. It includes a **Producer** service to send messages to Kafka topics and a **Consumer** service to listen and process messages from these topics. Below is the project structure and setup instructions.

## Description

- **Consumer Application**:
    - Listens to Kafka topics and processes incoming messages.
    - Main classes:
        - `DataListener.java`: The listener for Kafka messages.
        - `DataConsumerFactoryConfig.java`: Kafka consumer configuration.
        - `Form.java`: Entity for deserializing the consumed data.

- **Producer Application**:
    - Produces and sends messages to Kafka topics.
    - Main classes:
        - `DataProducerController.java`: Exposes endpoints for producing messages.
        - `DataProducerService.java`: Service for handling business logic of message production.
        - `JsonProducerConfig.java`: Configuration for Kafka producer.
        - `KafkaAdminConfig.java`: Configuration for Kafka Admin to manage topics.

- **Docker Compose**:
    - The project includes a `docker-compose.yml` file to orchestrate Kafka and Zookeeper setup locally for testing the producer and consumer.

## Prerequisites

- Java 17+
- Apache Kafka
- Docker & Docker Compose (for local Kafka instance)
- Maven 3.8+

## How to Run

1. **Start Kafka with Docker Compose**:
    - Navigate to the root of the project and run the following command:
      ```bash
      docker-compose up
      ```

2. **Build and Run the Producer**:
    - Navigate to the `Producer` directory:
      ```bash
      cd Producer
      ```
    - Build the application using Maven:
      ```bash
      ./mvnw clean install
      ```
    - Run the Producer application:
      ```bash
      ./mvnw spring-boot:run
      ```

3. **Build and Run the Consumer**:
    - Navigate to the `Consumer` directory:
      ```bash
      cd ../Consumer
      ```
    - Build the application using Maven:
      ```bash
      ./mvnw clean install
      ```
    - Run the Consumer application:
      ```bash
      ./mvnw spring-boot:run
      ```

## Configuration

Kafka configurations are defined in the `application.yml` files in both the Producer and Consumer applications.

Example Consumer `application.yml`:
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: ProducerId
    listener:
      missing-topics-fatal: false
```

Example Producer `application.yml`:
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

## Endpoints

### Producer

- **POST /produce**  
  Produces a message to the Kafka topic.
    - Example request:
      ```json
      {
        "message": "Hello, Kafka!"
      }
      ```

## Kafka Docker-Compose Configuration

This Docker Compose file sets up a Kafka environment with three key services: Zookeeper, Kafka, and Kafdrop. Below is an explanation of each component:

### 1. **Version: '3'**
This specifies the version of Docker Compose being used. Version 3 is commonly used for modern Docker Compose files, allowing the use of features like service dependencies and resource limits.

### 2. **Services**

#### a. **Zookeeper**
Zookeeper is essential for managing and coordinating distributed Kafka brokers. It tracks and maintains metadata about the Kafka clusters, such as broker locations and configurations.
- **Image**: `confluentinc/cp-zookeeper:latest` – uses the Confluent Zookeeper image.
- **Ports**: The client port `2181` is exposed, which Zookeeper uses to listen for Kafka broker requests.
- **Tick Time**: `2000` milliseconds sets the interval between heartbeats from the Zookeeper server to its clients.
- **Network**: The Zookeeper container connects to the `broker-kafka` network.

#### b. **Kafka**
Kafka is a distributed streaming platform that allows the creation of real-time data pipelines and stream processing applications.
- **Image**: `confluentinc/cp-kafka:latest` – uses the Confluent Kafka image.
- **Depends On**: Zookeeper, indicating Kafka will wait until Zookeeper is up and running before it starts.
- **Ports**: The internal port `29092` is mapped to the external `9092`, exposing Kafka on the host machine at `localhost:9092`.
- **Environment**:
    - **KAFKA_BROKER_ID**: A unique identifier for the Kafka broker.
    - **KAFKA_ZOOKEEPER_CONNECT**: Connects Kafka to Zookeeper using `zookeeper:2181`.
    - **KAFKA_ADVERTISED_LISTENERS**: Specifies the addresses used by Kafka clients to connect (`PLAINTEXT_HOST` is used to connect externally).
    - **KAFKA_LISTENER_SECURITY_PROTOCOL_MAP**: Defines how listeners correspond to security protocols (`PLAINTEXT` in this case).
    - **KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR**: `1` means no replication of topic data across Kafka brokers, suitable for single-broker setups.

#### c. **Kafdrop**
Kafdrop is a web-based UI for viewing Kafka topics, partitions, and consumer groups.
- **Image**: `obsidiandynamics/kafdrop:latest` – uses the latest Kafdrop image.
- **Depends On**: Kafka, ensuring Kafdrop will only start once Kafka is up and running.
- **Ports**: `19000:9000` maps Kafdrop's internal port `9000` to `19000` on the host machine.
- **Environment**:
    - **KAFKA_BROKERCONNECT**: Defines the Kafka broker connection using `kafka:29092` to connect internally to the Kafka broker.

### 3. **Networks**
All the services (Zookeeper, Kafka, and Kafdrop) are connected via the `broker-kafka` bridge network. This allows them to communicate with each other internally without exposing all ports to the outside world.

### 4. **Resource Limits**
Each service is assigned specific memory limits to control resource usage:
- **Kafka**: Limited to 1GB (`1g`) of memory.
- **Zookeeper**: Limited to 400MB (`400m`) of memory.
- **Kafdrop**: Limited to 500MB (`500m`) of memory.

### Summary
This Docker Compose file provides an efficient way to set up a Kafka environment locally for development or testing. It includes Zookeeper for Kafka coordination, Kafka for message streaming, and Kafdrop for monitoring Kafka activity via a user-friendly UI. The memory limits help in managing system resources effectively.

### Broker Configs

_[doc](https://kafka.apache.org/documentation/#brokerconfigs)_
