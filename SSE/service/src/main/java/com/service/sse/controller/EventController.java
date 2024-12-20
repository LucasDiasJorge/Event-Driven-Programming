package com.service.sse.controller;

import com.service.sse.service.sse.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class EventController {

    @Value("${app.settings.connection-keepalive}")
    private long connectionKeepAlive; // Em milisegundos
    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    private final SseService sseService;

    public EventController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping("/real-time-reading/{mac}")
    public SseEmitter realTimeReading(@PathVariable String mac) {
        SseEmitter emitter = new SseEmitter(connectionKeepAlive * 1000);
        sseService.processRealTimeReading(emitter, mac, connectionKeepAlive);
        return emitter;
    }
}
