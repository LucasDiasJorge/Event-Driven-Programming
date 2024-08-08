package message.kafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public DataProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Implementation of send message to topic
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
