package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public class TaskCompletePublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    private WaterOrder waterOrder;

    public TaskCompletePublisher() {
    }

    public TaskCompletePublisher(WaterOrder waterOrder) {

        this.waterOrder = waterOrder;
    }




    public WaterOrder getWaterOrder() {
        return waterOrder;
    }

    public void setWaterOrder(WaterOrder waterOrder) {
        this.waterOrder = waterOrder;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public  void notifyTaskDone(){

        if( this.waterOrder.getOrderStatus().toString().equals(Status.Requested.toString()) ){
            publisher.publishEvent(new WaterOrderStartTaskEvent(this, this.waterOrder));
        }
        if( this.waterOrder.getOrderStatus().toString().equals(Status.Started.toString()) ){
            publisher.publishEvent(new WaterOrderEndTaskEvent(this, this.waterOrder));
        }
    }

}
