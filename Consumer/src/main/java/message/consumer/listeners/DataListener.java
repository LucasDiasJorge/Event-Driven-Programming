package message.consumer.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DataListener {

    private Logger logger = LogManager.getLogger("DataListenerLogger");

    // How can i set multiple topics here ?
    @KafkaListener(groupId = "ProducerId", topics = "fromDataController", containerFactory = "kafkaContainerFactory")
    public void listener(String message) {
        logger.info("Message caught was: {}",message);
    }
}
