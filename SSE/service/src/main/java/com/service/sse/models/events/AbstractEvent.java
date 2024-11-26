package com.service.sse.models.events;

import com.service.sse.models.data.Data;

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

    public String delivery() {
        StringBuilder sb = new StringBuilder();

        sb.append("id:").append(this.id);
        sb.append("\nevent:").append(this.event);
        sb.append("\ndata:").append(this.data);

        return sb.toString();
    }

}
