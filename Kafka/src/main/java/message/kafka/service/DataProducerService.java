package message.kafka.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class DataProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private Logger logger = LogManager.getRootLogger();

    public DataProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Implementation of send message to topic from Client
    public void sendMessage(String topic, String message) {

        // Kafka callback Logging (https://www.baeldung.com/spring-kafka)
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message: {} with offset: {} to topic: {}", message, result.getRecordMetadata().offset(), result.getRecordMetadata().topic());
            } else {
                logger.error(ex.getMessage(), ex);
            }
        });
    }

    // Implementation of send message to topicCreated
    public void sendMessage(String message) {
        kafkaTemplate.send("TopicCreated", message);
    }
}
