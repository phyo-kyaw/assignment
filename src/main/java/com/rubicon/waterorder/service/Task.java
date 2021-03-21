package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.TaskCompleteEvent;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class Task implements Callable<WaterOrder>
{

    WaterOrder waterOrder;

    public Task(WaterOrder waterOrder) {
        this.waterOrder = waterOrder;
    }

    /*    @Override
    public TaskCompleteEvent call() throws Exception {

        System.out.println("xxx");
        applicationEventPublisher.publishEvent(taskCompleteEvent);
        System.out.println("hhh");

        return taskCompleteEvent;
    }*/


   @Override
    public WaterOrder call() throws Exception {

        System.out.println("It called " + waterOrder.getOrderStatus().toString().equals(Status.Requested.toString()) );
        System.out.println("It called " + waterOrder.getOrderStatus().toString().equals(Status.Started.toString()) );

        if(waterOrder.getOrderStatus().toString().equals(Status.Requested.toString())) {


            waterOrder.setOrderStatus(Status.Started);
            System.out.println("Water Order Task [" + waterOrder.getId() + "] executed and changed to : "
                    + waterOrder.getOrderStatus() + " status at " + LocalDateTime.now().toString()
                    + " and will end at " + LocalDateTime.now().plusSeconds(waterOrder.getFlowDuration().getSeconds()).toString());
            //TaskCompleteEvent taskCompleteEvent = new TaskCompleteEvent(this);
            //applicationEventPublisher.publishEvent(taskCompleteEvent);
            System.out.println("xxx");
            waterOrder.notifiyTaskDone();
            System.out.println("hhh");
        }
        else{
            if(waterOrder.getOrderStatus().toString().equals(Status.Started.toString())) {
                System.out.println("Water Order Task [" + waterOrder.getId() + "] changed to : "
                        + waterOrder.getOrderStatus() + " status at " + LocalDateTime.now().toString());
                waterOrder.setOrderStatus(Status.Delivered);
            }
        }
        return waterOrder;
    }

}