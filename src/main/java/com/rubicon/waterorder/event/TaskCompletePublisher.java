package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class TaskCompletePublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    private WaterOrder waterOrder;

    public WaterOrder getWaterOrder() {
        return waterOrder;
    }

    public void setWaterOrder(WaterOrder waterOrder) {
        this.waterOrder = waterOrder;
    }

    public TaskCompletePublisher(WaterOrder waterOrder) {
        this.waterOrder = waterOrder;
    }

    public TaskCompletePublisher(ApplicationEventPublisher publisher, WaterOrder waterOrder) {
        this.publisher = publisher;
        this.waterOrder = waterOrder;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public  void notifyTaskDone(){
        if( this.waterOrder.getOrderStatus().toString().equals(Status.Requested.toString()) ){
            publisher.publishEvent(new WaterOrderStartTaskEvent(this, waterOrder));
        }
        if( this.waterOrder.getOrderStatus().toString().equals(Status.Started.toString()) ){
            publisher.publishEvent(new WaterOrderEndTaskEvent(this, waterOrder));
        }

    }

    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }


}
