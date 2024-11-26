package com.service.sse.controller;

import com.service.sse.service.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class EventController {

    @Value("${spring.mvc.async.request-timeout}")
    private Long connectionKeep;

    Logger logger = LoggerFactory.getLogger(EventController.class);

    private final SseService sseService;

    public EventController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(path = "/real-time-reading")
    public SseEmitter realTimeReading() {
        SseEmitter emitter = new SseEmitter(connectionKeep);

        new Thread(() -> sseService.sendRealTimeReading(emitter, connectionKeep)).start();

        return emitter;
    }
}
