package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.context.ApplicationEvent;

public class WaterOrderEndTaskEvent extends ApplicationEvent {

    private WaterOrder waterOrder;

    public WaterOrderEndTaskEvent(Object source, WaterOrder waterOrder) {
        super(source);
        this.waterOrder = waterOrder;
    }

    public WaterOrder getWaterOrder() {

        return waterOrder;
    }

    public void setWaterOrder(WaterOrder waterOrder) {
        this.waterOrder = waterOrder;
    }
}
