package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.WaterOrderEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.persistence.Transient;

public class TaskCompletePublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    private String waterOrderId;

    public TaskCompletePublisher(String waterOrderId) {
        this.waterOrderId = waterOrderId;
    }

    public TaskCompletePublisher(ApplicationEventPublisher publisher, String waterOrderId) {
        this.publisher = publisher;
        this.waterOrderId = waterOrderId;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public  void notifyTaskDone(){
        publisher.publishEvent(new WaterOrderTaskEvent(this, waterOrderId));
    }

    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }

    public String getWaterOrderId() {
        return waterOrderId;
    }

    public void setWaterOrderId(String waterOrderId) {
        this.waterOrderId = waterOrderId;
    }
}
