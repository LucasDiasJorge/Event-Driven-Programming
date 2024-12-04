package com.service.sse.models.events;

import com.service.sse.enums.EEventType;
import com.service.sse.models.data.Data;

import java.util.Map;

public class GoodByeEvent extends AbstractEvent {

    public GoodByeEvent(Map<?,?> content) {

        this.event = EEventType.GOODBYE.getValue();

        Data data = new Data();

        data.eventType = EEventType.GOODBYE;
        data.bool = false;
        data.company = null;
        data.title = EEventType.GOODBYE.getValue();
        data.content = Map.of("Good Bye", "Blue Sky");

        this.data = data;
    }
}
