package com.service.sse.kafka.entities;

import java.util.Map;

public class Form {

    private Map<?,?> message;
    private String topic;

    // No-argument constructor is required for deserialization
    public Form() {}

    public Map<?,?> getMessage() {
        return message;
    }

    public void setMessage(Map<?,?> message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}