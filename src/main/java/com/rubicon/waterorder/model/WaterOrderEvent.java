package com.rubicon.waterorder.model;

import org.springframework.context.ApplicationEvent;

public class WaterOrderEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String eventType;
    private WaterOrder waterOrder;

    public WaterOrderEvent(Object source, String eventType, WaterOrder waterOrder) {
        super(source);
        this.eventType = eventType;
        this.waterOrder = waterOrder;
    }

    public String getEventType() {
        return eventType;
    }

    public WaterOrder getWaterOrder() {
        return waterOrder;
    }
}
