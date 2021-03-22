package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.context.ApplicationEvent;

public class WaterOrderStartTaskEvent extends ApplicationEvent {

    private WaterOrder waterOrder;

    public WaterOrderStartTaskEvent(Object source, WaterOrder waterOrder) {
        super(source);
        this.waterOrder = waterOrder;
    }

    public WaterOrder getWaterOrder() {

        return waterOrder;
    }

    public void setWaterOrder(String waterOrderId) {
        this.waterOrder = waterOrder;
    }
}
