package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.context.ApplicationEvent;

public class WaterOrderTaskEvent extends ApplicationEvent {

    private String waterOrderId;

    public WaterOrderTaskEvent(Object source, String waterOrderId) {
        super(source);
        this.waterOrderId = waterOrderId;
    }

    public String getWaterOrderId() {

        return waterOrderId;
    }

    public void setWaterOrderId(String waterOrderId) {
        this.waterOrderId = waterOrderId;
    }
}
