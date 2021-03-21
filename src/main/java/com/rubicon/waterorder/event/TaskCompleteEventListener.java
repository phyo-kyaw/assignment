package com.rubicon.waterorder.event;

import com.rubicon.waterorder.model.WaterOrderEvent;
import com.rubicon.waterorder.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TaskCompleteEventListener implements ApplicationListener<WaterOrderTaskEvent> {

    SchedulerService schedulerService = SchedulerService.getInstance();

    public TaskCompleteEventListener(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }


    @Override
    public void onApplicationEvent(WaterOrderTaskEvent waterOrderTaskEvent) {
        System.out.println("In SchedulerService " + waterOrderTaskEvent.getClass().getName());
        System.out.println("In SchedulerService " + waterOrderTaskEvent.getWaterOrderId());
        schedulerService.test();
    }
}
