package com.service.sse.service.sse;

import com.service.sse.kafka.config.KafkaConsumerConfig;
import com.service.sse.models.events.HandShakeEvent;
import com.service.sse.models.events.ReadingEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(SseService.class);

    public void sendRealTimeReading(SseEmitter emitter, long connectionKeep, String topic) {

        LocalDateTime disconnectTime = LocalDateTime.now().plusSeconds(connectionKeep);
        KafkaConsumer<String, String> consumer = null;

        try {
            // Envia o evento de handshake no início da conexão
            try {
                SseEmitter.SseEventBuilder handshakeEvent = new HandShakeEvent(Map.of()).delivery();
                emitter.send(handshakeEvent.build());
            } catch (IOException e) {
                logger.error("Handshake event error: {}", e.getMessage());
                emitter.completeWithError(e);
                return; // Saia se o handshake falhar
            }

            consumer = new KafkaConsumer<>(new KafkaConsumerConfig().consumerFactory().getConfigurationProperties());

            try {
                consumer.subscribe(List.of(topic));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            LocalDateTime lastHeartbeatTime = LocalDateTime.now();

            while (LocalDateTime.now().isBefore(disconnectTime)) {

                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(250));

                for (ConsumerRecord<String, String> data : records) {
                    try {
                        emitter.send(new ReadingEvent(Map.of("epc", data.value())).delivery().build());
                    } catch (IOException e) {
                        logger.error("Error sending reading event: {}", e.getMessage());
                        emitter.completeWithError(e);
                        return; // Saia se o cliente desconectar
                    }
                }

                // Envia heartbeat a cada 30 segundos
                if (Duration.between(lastHeartbeatTime, LocalDateTime.now()).getSeconds() >= 30) {
                    try {
                        emitter.send(SseEmitter.event().name("heartbeat").data("ping").id(UUID.randomUUID().toString()).build());
                        lastHeartbeatTime = LocalDateTime.now();
                    } catch (IOException e) {
                        logger.error("Error sending heartbeat: {}", e.getMessage());
                        emitter.completeWithError(e);
                        return; // Saia se o cliente desconectar
                    }
                }
                Thread.sleep(250);
            }
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
        } finally {
            System.out.println("FINALLY");
            if (consumer != null) {
                try {
                    consumer.unsubscribe();
                    consumer.close();
                } catch (Exception e) {
                    logger.warn("Error closing consumer: {}", e.getMessage());
                }
            }
            emitter.complete();
        }
    }

}

