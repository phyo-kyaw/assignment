package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.TaskCompletePublisher;
import com.rubicon.waterorder.event.WaterOrderCancelTaskEvent;
import com.rubicon.waterorder.model.WaterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
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


    public void scheduleStartTask(WaterOrder waterOrder){

        if(isInQueue(waterOrder)){
            log.error("Water Order [ " + waterOrder.getId() + " ] is in queue. Please investigate.");
            return;
        }

        TaskCompletePublisher taskCompletePublisher = new TaskCompletePublisher(waterOrder);
        taskCompletePublisher.setApplicationEventPublisher(this.applicationEventPublisher);
        ScheduleTask waterOrderScheduleTask = new ScheduleTask(taskCompletePublisher);
        Long delayInSec = Duration.between(LocalDateTime.now(), waterOrder.getStartDateTime()).getSeconds();
        log.info("Water Order [ " + waterOrder.getId() + " ] scheduled from : "
                + waterOrder.getOrderStatus() + " status at (machine date) "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
            log.error("Water Order [ " + waterOrder.getId() + " ] is in queue. Please investigate.");
            return;
        }

        TaskCompletePublisher taskCompletePublisher = new TaskCompletePublisher(waterOrder);
        taskCompletePublisher.setApplicationEventPublisher(this.applicationEventPublisher);
        ScheduleTask waterOrderScheduleTask = new ScheduleTask(taskCompletePublisher);

        Long delayInSec = waterOrder.getFlowDuration().getSeconds();
        log.info("Water Order [ " + waterOrder.getId() + " ] scheduled from : "
                + waterOrder.getOrderStatus() + " status at (machine date) "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                +  " = " + waterOrder.getStartDateTime().toString() + " (scheduled date) "
                + " and will end at " + waterOrder.getStartDateTime().plusSeconds(delayInSec).toString()
                + " --- in next seconds " + delayInSec + ".");

        ScheduledFuture<TaskCompletePublisher> futureOrder = executor.schedule(waterOrderScheduleTask, delayInSec, TimeUnit.SECONDS);
        scheduledOrderList.put( waterOrder.getId(), futureOrder);
    }

    public synchronized boolean cancelTask(WaterOrder waterOrder){
        if(!isInQueue(waterOrder)){
            log.error("Water Order [ " + waterOrder.getId() + " ] is not in queue. Please investigate.");
            return false;
        }

        ScheduledFuture<TaskCompletePublisher> futureOrder = scheduledOrderList.get(waterOrder.getId());
        if(futureOrder.isDone() == false)
        {
            log.debug("Cancelling the task.");
        }
        boolean cancelReturn = futureOrder.cancel(true);
        if(!cancelReturn)
        {
            log.warn("Cancelling Water Order Id [ " + waterOrder.getId() + " ] failed. It might has already been delivered.");
            return false;
        }
        applicationEventPublisher.publishEvent(new WaterOrderCancelTaskEvent(this, waterOrder));
        removeTaskFromQueue(waterOrder);
        log.info("Water Order Id [ " + waterOrder.getId() + " ] has been cancelled at "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +  ".");
        return true;

    }

    public void removeTaskFromQueue(WaterOrder waterOrder){
        if(isInQueue(waterOrder))
            scheduledOrderList.remove(waterOrder.getId());
        else
            log.warn("Water Order Id [ " + waterOrder.getId() + " ] is not in queue.");
    }
}

