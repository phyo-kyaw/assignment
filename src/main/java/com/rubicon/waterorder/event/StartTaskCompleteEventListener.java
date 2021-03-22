package com.rubicon.waterorder.event;

import com.rubicon.waterorder.service.SchedulerService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StartTaskCompleteEventListener implements ApplicationListener<WaterOrderStartTaskEvent> {

    SchedulerService schedulerService = SchedulerService.getInstance();

    public StartTaskCompleteEventListener(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public void onApplicationEvent(WaterOrderStartTaskEvent waterOrderStartTaskEvent) {
        System.out.println("In StartTaskCompleteEventListener " + waterOrderStartTaskEvent.getClass().getName());
        System.out.println("In StartTaskCompleteEventListener " + waterOrderStartTaskEvent.getWaterOrder().getId());
        schedulerService.test(waterOrderStartTaskEvent.getWaterOrder().getId().toString());
        //schedulerService.scheduleEndTask()
    }
}
