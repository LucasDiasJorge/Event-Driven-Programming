package message.consumer.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import message.consumer.entities.Form;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataListener {

    private final Logger logger = LogManager.getLogger("DataListenerLogger");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(groupId = "ProducerId", topics = {"json-topic", "another-topic"}, containerFactory = "kafkaContainerFactory")
    public void listener(ConsumerRecord<String, Object> consumerRecord) {

        Object value = consumerRecord.value();
        Form form = null;

        if (value instanceof Map) {
            form = objectMapper.convertValue(value, Form.class);
        } else {
            logger.error("Unexpected record value type: {}", value.getClass().getName());
        }

        if (form != null) {
            logger.info("Message treated is: {}", form.getMessage());
        }
    }
}
