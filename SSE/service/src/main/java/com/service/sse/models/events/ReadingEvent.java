package com.service.sse.models.events;

import com.service.sse.enums.EEventType;
import com.service.sse.models.data.Data;

import java.util.Map;

public class ReadingEvent extends AbstractEvent {

    public ReadingEvent(Map<?,?> content, Long dataId) {

        this.event = EEventType.REAL_TIME_READING.getValue();

        Data data = new Data();

        data.id = dataId;
        data.eventType = EEventType.REAL_TIME_READING;
        data.bool = false;
        data.company = null;
        data.title = EEventType.REAL_TIME_READING.getValue();

        data.content = content;

        this.data = data;
    }

}
