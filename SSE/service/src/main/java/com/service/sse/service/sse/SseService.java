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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
public class SseService {

    Logger logger = LoggerFactory.getLogger(SseService.class);

    public void sendRealTimeReading(SseEmitter emitter, long connectionKeep, String topic) {

        LocalDateTime connectionTime = LocalDateTime.now();
        LocalDateTime disconnectTime = connectionTime.plusSeconds(connectionKeep);

        try {
            emitter.send(new HandShakeEvent(Map.of()).delivery());
        } catch (IOException e) {
            logger.error("HandShakeEvent error: {}", e.getMessage());
        }

        KafkaConsumer<String, String> consumer = null;

        try {
            consumer = new KafkaConsumer<String, String>(new KafkaConsumerConfig().consumerFactory().getConfigurationProperties());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        consumer.subscribe(Collections.singleton(topic));

        try {
            while (LocalDateTime.now().isBefore(disconnectTime)) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String,String> data: records) {
                    emitter.send(new ReadingEvent(Map.of("epc",data.value())).delivery());
                }
                Thread.sleep(1000);
            }
            emitter.complete();
        } catch (IOException | InterruptedException ex) {
            logger.error("ReadingEvent error: {}", ex.getMessage()); // Usuário provavelmente desconectou
            emitter.completeWithError(ex);
        }

        consumer.unsubscribe();
        consumer.close();
    }
}

