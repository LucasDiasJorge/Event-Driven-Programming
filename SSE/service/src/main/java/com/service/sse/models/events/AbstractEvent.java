package com.service.sse.models.events;

import com.service.sse.models.data.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.util.Map;
import java.util.UUID;

public abstract class AbstractEvent {

    public UUID id = UUID.randomUUID();

    public String event;

    public Data data;

    public AbstractEvent() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public AbstractEvent(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public SseEventBuilder delivery() {

        SseEventBuilder eventBuilder = SseEmitter.event();
        eventBuilder.id(this.getId().toString());
        eventBuilder.name(this.getEvent());
        eventBuilder.data(this.getData());

        return eventBuilder;
    }

}
