package com.service.sse.service;

import com.service.sse.events.HandShakeEvent;
import com.service.sse.events.ReadingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
public class SseService {

    Logger logger = LoggerFactory.getLogger(SseService.class);

    public void sendRealTimeReading(SseEmitter emitter, long connectionKeep) {
        try {
            emitter.send(new HandShakeEvent(Map.of()).delivery());
        } catch (IOException e) {
            logger.error("HandShakeEvent error: {}", e.getMessage());
        }

        try {
            for (int i = 0; i < (connectionKeep / 1000); i++) {
                emitter.send(new ReadingEvent(Map.of()).delivery());
                Thread.sleep(1000);
            }
            emitter.complete();
        } catch (IOException | InterruptedException ex) {
            emitter.completeWithError(ex);
        }
    }
}
