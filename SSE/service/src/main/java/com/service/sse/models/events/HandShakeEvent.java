package com.service.sse.models.events;

import com.service.sse.enums.EEventType;
import com.service.sse.models.data.Data;

import java.util.Map;

public class HandShakeEvent extends AbstractEvent {

    public HandShakeEvent(Map<?,?> content) {

        this.event = EEventType.HANDSHAKE.getValue();

        Data data = new Data();

        data.eventType = EEventType.HANDSHAKE;
        data.bool = false;
        data.company = null;
        data.title = EEventType.HANDSHAKE.getValue();
        data.content = content;

        this.data = data;
    }
}
