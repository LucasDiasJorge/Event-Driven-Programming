package com.example.socketio.listeners;

import com.example.socketio.message.Form;
import com.example.socketio.services.SocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final SocketService socketService;

    public DataListener(SocketService socketService) {
        this.socketService = socketService;
    }

    @KafkaListener(groupId = "ProducerId", topics = {"json-topic", "another-topic"}, containerFactory = "kafkaContainerFactory")
    public void listener(ConsumerRecord<String, Object> consumerRecord) {

        Object value = consumerRecord.value();
        Form form = null;

        if (value instanceof String) {
            try {
                form = objectMapper.readValue((String) value, Form.class);
            } catch (JsonProcessingException e) {
                logger.error("Failed to deserialize JSON string to Form object", e);
            }
        } else if (value instanceof Map) {
            form = objectMapper.convertValue(value, Form.class);
        } else {
            logger.error("Unexpected record value type: {}", value.getClass().getName());
        }

        if (form != null) {

            String message = form.getMessage();
            String topic = consumerRecord.topic();

            // Enviar a mensagem para todos os clientes conectados ao Socket.IO
            socketService.sendMessage(message, "get_message", null, topic);

            logger.info("Message treated is: {}", message);
        }
    }
}
