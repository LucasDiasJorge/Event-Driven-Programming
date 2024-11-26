package com.service.sse.models.data;

import com.service.sse.enums.EEventType;

import java.util.Map;

public class Data {

    public Long id;
    public String title;
    public String message;
    public EEventType eventType;
    public Map<?,?> content;
    public Boolean bool;
    public Long company;

    public Data(Long id, String title, String message, EEventType eventType, Map<?, ?> content, Boolean bool, Long company) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.eventType = eventType;
        this.content = content;
        this.bool = bool;
        this.company = company;
    }

    public Data() {
        this.id = -1L;
        this.title = "Default Title";
        this.message = "Default Message";
        this.eventType = null;
        this.content = null;
        this.bool = false;
        this.company = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"id\":").append(id != null ? id : "null").append(",");
        sb.append("\"title\":\"").append(title != null ? title : "null").append("\",");
        sb.append("\"message\":\"").append(message != null ? message : "null").append("\",");
        sb.append("\"eventType\":\"").append(eventType != null ? eventType.toString() : "null").append("\",");
        sb.append("\"content\":").append(content != null ? content.toString() : "null").append(",");
        sb.append("\"bool\":").append(bool != null ? bool : "null").append(",");
        sb.append("\"company\":").append(company != null ? company : "null");
        sb.append("}");

        return sb.toString();
    }

}
