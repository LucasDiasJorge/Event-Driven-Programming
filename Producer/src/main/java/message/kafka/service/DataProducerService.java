package message.kafka.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;


@Service
public class DataProducerService {

    private final KafkaTemplate<String, Serializable> kafkaTemplate;
    private Logger logger = LogManager.getLogger("DataProducerLogger");

    public DataProducerService(KafkaTemplate<String, Serializable> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Implementation of send message to topicCreated
    public void sendMessage(Serializable message) {
        // Kafka callback Logging (https://www.baeldung.com/spring-kafka)
        CompletableFuture<SendResult<String, Serializable>> future = kafkaTemplate.send("json-topic",message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message: {} with offset: {} to topic: {}", message, result.getRecordMetadata().offset(), result.getRecordMetadata().topic());
            } else {
                logger.error(ex.getMessage(), ex);
            }
        });
    }
}
