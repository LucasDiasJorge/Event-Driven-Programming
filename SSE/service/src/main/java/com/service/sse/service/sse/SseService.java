package com.service.sse.service.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.sse.kafka.config.KafkaConsumerConfig;
import com.service.sse.models.events.HandShakeEvent;
import com.service.sse.models.events.ReadingEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SseService.class);

    @Value("${app.settings.timeout}")
    private long pollTimeout; // Em milissegundos

    @Value("${app.settings.heartbeat-interval}")
    private long heartbeatInterval; // Em segundos

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processRealTimeReading(SseEmitter emitter, String topic, long connectionKeepAlive) {
        new Thread(() -> handleSseConnection(emitter, topic, connectionKeepAlive)).start();
    }

    private void handleSseConnection(SseEmitter emitter, String topic, long connectionKeepAlive) {
        LocalDateTime disconnectTime = LocalDateTime.now().plusSeconds(connectionKeepAlive);
        LocalDateTime lastHeartbeatTime = LocalDateTime.now();
        try (KafkaConsumer<String, String> consumer = createKafkaConsumer(topic)) {
            sendHandshakeEvent(emitter);
            processKafkaRecords(emitter, consumer, disconnectTime, lastHeartbeatTime);
        } catch (Exception e) {
            LOGGER.error("Unexpected error: {}", e.getMessage());
            emitter.completeWithError(e);
        } finally {
            emitter.complete();
        }
    }

    private KafkaConsumer<String, String> createKafkaConsumer(String topic) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(new KafkaConsumerConfig().consumerFactory().getConfigurationProperties());
        consumer.subscribe(List.of(topic));
        return consumer;
    }

    private void sendHandshakeEvent(SseEmitter emitter) throws IOException {
        emitter.send(new HandShakeEvent(Map.of()).delivery().build());
    }

    private void processKafkaRecords(SseEmitter emitter, KafkaConsumer<String, String> consumer,
                                     LocalDateTime disconnectTime, LocalDateTime lastHeartbeatTime) {
        while (LocalDateTime.now().isBefore(disconnectTime)) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(pollTimeout));
            sendReadingEvents(emitter, records);
        }
    }

    private void sendReadingEvents(SseEmitter emitter, ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            try {

                Map<?, ?> data = objectMapper.readValue(record.value(), Map.class);
                emitter.send(new ReadingEvent(data).delivery().build());

            } catch (IOException e) {
                LOGGER.error("Error sending reading event: {}", e.getMessage());
                emitter.completeWithError(e);
                throw new RuntimeException(e);
            }
        }
    }

    private void sendHeartbeat(SseEmitter emitter) throws IOException {
        emitter.send(SseEmitter.event().name("heartbeat").data("ping").id(UUID.randomUUID().toString()).build());
    }
}
