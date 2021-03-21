package com.rubicon.waterorder.model;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity
public class WaterOrderTask {
    //@Id
    //@GeneratedValue
    //private Long id;

    //@ManyToOne
    //@JoinColumn(name = "WATER_ORDER_ID")
    private WaterOrder waterOrder;

    //@Column(name = "SCHEDULED_FOR")
    private Status nextStatus;

    //@Column(name = "SCHEDULED_FOR")
    //private Status orderStatus;

    private LocalDateTime startDateTime;


    public WaterOrderTask() {
    }

    public WaterOrderTask(WaterOrder waterOrder, Status nextStatus) {
        this.waterOrder = waterOrder;
        this.nextStatus = nextStatus;
    }

    public WaterOrder getWaterOrder() {
        return waterOrder;
    }

    public Status getNextStatus() {
        return nextStatus;
    }

    public void setWaterOrder(WaterOrder waterOrder) {
        this.waterOrder = waterOrder;
    }

    public void setNextStatus(Status nextStatus) {
        this.nextStatus = nextStatus;
    }
}
