package com.service.sse.service.sse;

import com.service.sse.models.events.HandShakeEvent;
import com.service.sse.models.events.ReadingEvent;
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
                emitter.send(new ReadingEvent(Map.of(/*Last Readed Tag*/)).delivery());
                Thread.sleep(1000);
            }
            emitter.complete();
        } catch (IOException | InterruptedException ex) {
            logger.error("ReadingEvent error: {}", ex.getMessage()); // User probably disconnect
            emitter.completeWithError(ex);
        }
    }
}
