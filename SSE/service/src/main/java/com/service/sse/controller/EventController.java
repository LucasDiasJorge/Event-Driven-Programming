package com.service.sse.controller;

import com.service.sse.service.sse.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class EventController {

    private final Long connectionKeep = 120L;

    Logger logger = LoggerFactory.getLogger(EventController.class);

    private final SseService sseService;

    public EventController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(path = "/real-time-reading/{mac}")
    public SseEmitter realTimeReading(@PathVariable String mac) {
        SseEmitter emitter = new SseEmitter(connectionKeep * 1000);

        new Thread(() -> sseService.sendRealTimeReading(emitter, connectionKeep, mac)).start();

        return emitter;
    }
}
