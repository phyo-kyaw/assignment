package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.TaskCompletePublisher;
import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Service
public class SchedulerService {

    private ApplicationEventPublisher applicationEventPublisher;

    private ScheduledExecutorService executor;

    private static SchedulerService INSTANCE = new SchedulerService();

    private SchedulerService() {
        executor = Executors.newScheduledThreadPool(1);
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public static SchedulerService getInstance() {
        return INSTANCE;
    }

    //ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    public ScheduledFuture<TaskCompletePublisher> scheduleStartTask(WaterOrder waterOrder){
        TaskCompletePublisher taskCompletePublisher = new TaskCompletePublisher(waterOrder);
        taskCompletePublisher.setApplicationEventPublisher(this.applicationEventPublisher);
        ScheduleTask waterOrderScheduleTask = new ScheduleTask(taskCompletePublisher);
        Long delayInSec = Duration.between(LocalDateTime.now(), waterOrder.getStartDateTime()).getSeconds();
        System.out.println("Water Order [" + waterOrder.getId() + "] scheduled from : "
                + waterOrder.getOrderStatus() + " status at " + LocalDateTime.now().toString()
                + " and will start at " + waterOrder.getStartDateTime().toString()
                + " and will end at " + waterOrder.getStartDateTime().plusSeconds(delayInSec) + ".");

        //System.out.println(this.applicationEventPublisher);
        //System.out.println(taskCompletePublisher.getPublisher());

        ScheduledFuture<TaskCompletePublisher> futureOrder = executor.schedule(waterOrderScheduleTask, delayInSec, TimeUnit.SECONDS);
        //String id = waterOrder.getId().toString() + Long.toString(System.currentTimeMillis());
        return futureOrder;
    }

    public ScheduledFuture<TaskCompletePublisher> scheduleEndTask(WaterOrder waterOrder){
        TaskCompletePublisher taskCompletePublisher = new TaskCompletePublisher(waterOrder);
        taskCompletePublisher.setApplicationEventPublisher(this.applicationEventPublisher);
        ScheduleTask waterOrderScheduleTask = new ScheduleTask(taskCompletePublisher);
        //Long delayInSec = Duration.between(LocalDateTime.now(), waterOrder.getStartDateTime()).getSeconds();
        Long delayInSec = waterOrder.getFlowDuration().getSeconds();
        System.out.println("Water Order [" + waterOrder.getId() + "] scheduled from : "
                + waterOrder.getOrderStatus() + " status at " + LocalDateTime.now().toString()
                +  " = " + waterOrder.getStartDateTime().toString()
                + " and will end at " + waterOrder.getStartDateTime().plusSeconds(delayInSec).toString()
                + " --- in seconds " + delayInSec + ".");

        //System.out.println(this.applicationEventPublisher);
        //System.out.println(taskCompletePublisher.getPublisher());

        ScheduledFuture<TaskCompletePublisher> futureOrder = executor.schedule(waterOrderScheduleTask, delayInSec, TimeUnit.SECONDS);
        //String id = waterOrder.getId().toString() + Long.toString(System.currentTimeMillis());
        return futureOrder;
    }
/*    public ScheduledFuture<TaskCompleteEvent> scheduleStartTask(TaskCompleteEvent taskCompleteEvent){
        Task waterOrderScheduledTask = new Task(taskCompleteEvent);
        Long delayInSec = Duration.between(LocalDateTime.now(), taskCompleteEvent.getWaterOrder().getStartDateTime()).getSeconds();
        System.out.println("Water Order Task [" + taskCompleteEvent.getWaterOrder().getId() + "] scheduled from : "
                + taskCompleteEvent.getWaterOrder().getOrderStatus() + " status at " + LocalDateTime.now().toString()
                + " and will start at " + taskCompleteEvent.getWaterOrder().getStartDateTime().toString() );
        System.out.println(delayInSec);

        ScheduledFuture<TaskCompleteEvent> futureOrder = executor.schedule(waterOrderScheduledTask, delayInSec, TimeUnit.SECONDS);
        String id = taskCompleteEvent.getWaterOrder().getId().toString() + Long.toString(System.currentTimeMillis());
        scheduledOrderList.put(id , futureOrder);

        System.out.println(scheduledOrderList.size());
        System.out.println(scheduledOrderList.get(id));

        return futureOrder;
    }*/

    /*public ScheduledFuture<WaterOrder> scheduleStopTask(WaterOrder waterOrder){
        Task waterOrderScheduledTask = new Task( waterOrder);
        Long delayInSec = waterOrder.getFlowDuration().getSeconds();
        System.out.println("Stop called");
*//*        System.out.println("Water Order Task [" + waterOrder.getId() + "] started with : "
                + waterOrder.getOrderStatus() + " status at " + LocalDateTime.now().toString());*//*

        ScheduledFuture<WaterOrder> futureOrder = executor.schedule(waterOrderScheduledTask, delayInSec, TimeUnit.SECONDS);
        String id = waterOrder.getId().toString() + Long.toString(System.currentTimeMillis());
        scheduledOrderList.put(id , futureOrder);

        System.out.println(scheduledOrderList.size());
        System.out.println(scheduledOrderList.get(id));

        return futureOrder;
    }*/

    public void cancelTask(WaterOrder waterOrder){

    }

    public void test(String waterOrderId){
        System.out.println("Test in SchedulerService: wo_id " + waterOrderId);
    }


    public void scheduleCancelTask(Long id) {
    }
}

