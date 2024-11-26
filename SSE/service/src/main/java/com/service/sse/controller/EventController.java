package com.service.sse.controller;

import com.service.sse.events.HandShakeEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/sse")
public class EventController {

    @Value("${spring.mvc.async.request-timeout}")
    private Long connectionKeep;

    @GetMapping(path = "/events")
    public SseEmitter handleSSE() {
        SseEmitter emitter = new SseEmitter(connectionKeep);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                for (int i = 0; i < (connectionKeep / 1000); i++) {

                    emitter.send(new HandShakeEvent(Map.of()).delivery());
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (IOException | InterruptedException ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }
}
