package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

public class TaskCompleteEvent extends ApplicationEvent {

    private WaterOrder waterOrder;

    public TaskCompleteEvent(Object source, WaterOrder waterOrder) {
        super(source);
        this.waterOrder = waterOrder;
    }

    public void setWaterOrder(WaterOrder waterOrder) {
        this.waterOrder = waterOrder;
    }

    public WaterOrder getWaterOrder() {
        return waterOrder;
    }
}
