package com.service.sse.enums;

import com.service.sse.enums.base.EnumOptionBase;

public enum EEventType implements EnumOptionBase {

    HANDSHAKE("Handshake"),
    GOODBYE("Good Bye"),
    REAL_TIME_READING("Real Time Reading");

    private String value;

    EEventType(String value) {
        this.value = value;
    }

    @Override
    public String getEnumName() {
        return "EventType";
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Boolean enableSerialization() {
        return true;
    }
}
