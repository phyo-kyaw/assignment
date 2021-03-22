package com.rubicon.waterorder.event;

import com.rubicon.waterorder.service.SchedulerService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EndTaskCompleteEventListener implements ApplicationListener<WaterOrderEndTaskEvent> {

    SchedulerService schedulerService = SchedulerService.getInstance();

    public EndTaskCompleteEventListener(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public void onApplicationEvent(WaterOrderEndTaskEvent waterOrderEndTaskEvent) {
        System.out.println("In EndTaskCompleteEventListener " + waterOrderEndTaskEvent.getClass().getName());
        System.out.println("In EndTaskCompleteEventListener " + waterOrderEndTaskEvent.getWaterOrder().getId().toString());
        schedulerService.test(waterOrderEndTaskEvent.getWaterOrder().getId().toString());
    }
}