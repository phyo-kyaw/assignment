package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.TaskCompletePublisher;
import com.rubicon.waterorder.event.WaterOrderCancelTaskEvent;
import com.rubicon.waterorder.model.WaterOrder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class SchedulerService {

    private ApplicationEventPublisher applicationEventPublisher;

    private ScheduledExecutorService executor;

    private static SchedulerService INSTANCE = new SchedulerService();

    private SchedulerService() {
        executor = Executors.newScheduledThreadPool(1);
    }

    private Map<Long, ScheduledFuture<TaskCompletePublisher>> scheduledOrderList = new HashMap<>();

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.applicationEventPublisher = publisher;
    }

    public static SchedulerService getInstance() {
        return INSTANCE;
    }

    //ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    public void scheduleStartTask(WaterOrder waterOrder){

        if(isInQueue(waterOrder)){
            System.out.println("Water Order [" + waterOrder.getId() + "] is in queue. Please investigate.");
            return;
        }

        TaskCompletePublisher taskCompletePublisher = new TaskCompletePublisher(waterOrder);
        taskCompletePublisher.setApplicationEventPublisher(this.applicationEventPublisher);
        ScheduleTask waterOrderScheduleTask = new ScheduleTask(taskCompletePublisher);
        Long delayInSec = Duration.between(LocalDateTime.now(), waterOrder.getStartDateTime()).getSeconds();
        System.out.println("Water Order [" + waterOrder.getId() + "] scheduled from : "
                + waterOrder.getOrderStatus() + " status at " + LocalDateTime.now().toString()
                + " and will start at " + waterOrder.getStartDateTime().toString()
                + " and will end at " + waterOrder.getStartDateTime().plusSeconds(delayInSec)
                + " --- in next seconds " + delayInSec + ".");

        ScheduledFuture<TaskCompletePublisher> futureOrder = executor.schedule(waterOrderScheduleTask, delayInSec, TimeUnit.SECONDS);

        scheduledOrderList.put( waterOrder.getId(), futureOrder);
    }

    private boolean isInQueue(WaterOrder waterOrder) {
        return scheduledOrderList.containsKey(waterOrder.getId());
    }

    public void scheduleEndTask(WaterOrder waterOrder){

        if(isInQueue(waterOrder)){
            System.out.println("Water Order [" + waterOrder.getId() + "] is in queue. Please investigate.");
            return;
        }

        TaskCompletePublisher taskCompletePublisher = new TaskCompletePublisher(waterOrder);
        taskCompletePublisher.setApplicationEventPublisher(this.applicationEventPublisher);
        ScheduleTask waterOrderScheduleTask = new ScheduleTask(taskCompletePublisher);
        //Long delayInSec = Duration.between(LocalDateTime.now(), waterOrder.getStartDateTime()).getSeconds();
        Long delayInSec = waterOrder.getFlowDuration().getSeconds();
        System.out.println("Water Order [" + waterOrder.getId() + "] scheduled from : "
                + waterOrder.getOrderStatus() + " status at " + LocalDateTime.now().toString()
                +  " = " + waterOrder.getStartDateTime().toString()
                + " and will end at " + waterOrder.getStartDateTime().plusSeconds(delayInSec).toString()
                + " --- in next seconds " + delayInSec + ".");

        ScheduledFuture<TaskCompletePublisher> futureOrder = executor.schedule(waterOrderScheduleTask, delayInSec, TimeUnit.SECONDS);
        scheduledOrderList.put( waterOrder.getId(), futureOrder);
    }

    public boolean cancelTask(WaterOrder waterOrder){
        if(!isInQueue(waterOrder)){
            System.out.println("Water Order [" + waterOrder.getId() + "] is not in queue. Please investigate.");
            return false;
        }

        ScheduledFuture<TaskCompletePublisher> futureOrder = scheduledOrderList.get(waterOrder.getId());
        if(futureOrder.isDone() == false)
        {
            System.out.println("====Cancelling the task====");
        }
        boolean cancelReturn = futureOrder.cancel(true);
        if(!cancelReturn)
        {
            System.out.println("====Cancelling the task failed. It might has already been delivered====");
            return false;
        }
        applicationEventPublisher.publishEvent(new WaterOrderCancelTaskEvent(this, waterOrder));
        removeTaskFromQueue(waterOrder);
        return true;

    }

    public void removeTaskFromQueue(WaterOrder waterOrder){
        scheduledOrderList.remove(waterOrder.getId());
    }
}

