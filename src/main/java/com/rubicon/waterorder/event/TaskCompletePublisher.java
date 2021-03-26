package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
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
            log.debug("Water Order Id : [ " + this.waterOrder.getId() + " ] is triggered to start.");
            publisher.publishEvent(new WaterOrderStartTaskEvent(this, this.waterOrder));
        }
        if( this.waterOrder.getOrderStatus().toString().equals(Status.Started.toString()) ){
            log.debug("Water Order Id : [ " + this.waterOrder.getId() + " ] is triggered to stop.");
            publisher.publishEvent(new WaterOrderEndTaskEvent(this, this.waterOrder));
        }
    }

}
