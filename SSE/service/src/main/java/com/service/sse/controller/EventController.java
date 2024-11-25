package com.service.sse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/sse")
public class EventController {

    @GetMapping("/events")
    public ResponseBodyEmitter handleSSE() {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    emitter.send(String.format("Event %d\n", i));
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                    return;
                }
            }
            emitter.complete();
        });

        return emitter;
    }


    private final SseEmitter sseEmitter = new SseEmitter();

    public void sendEvent(String event) {
        try {
            sseEmitter.send(event);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
