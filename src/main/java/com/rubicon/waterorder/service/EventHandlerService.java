package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.WaterOrderEndTaskEvent;
import com.rubicon.waterorder.event.WaterOrderStartTaskEvent;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventHandlerService {

    private WaterOrderRepository waterOrderRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    private SchedulerService schedulerService = SchedulerService.getInstance();

    public EventHandlerService(WaterOrderRepository waterOrderRepository,
                               ApplicationEventPublisher applicationEventPublisher) {
        this.waterOrderRepository = waterOrderRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener
    void onStartTaskComplete(WaterOrderStartTaskEvent waterOrderStartTaskEvent){
        WaterOrder waterOrderProcessed = waterOrderStartTaskEvent.getWaterOrder();
        WaterOrder waterOrderReferenced = waterOrderRepository.findById(waterOrderProcessed.getId()).get();

        if( waterOrderProcessed.getOrderStatus().toString().equals(Status.Requested.toString())
        && waterOrderReferenced.getOrderStatus().toString().equals(Status.Requested.toString()) ){
            waterOrderReferenced.setOrderStatus(Status.Started);

            waterOrderRepository.save(waterOrderReferenced);
            System.out.println("Start Complete");
            schedulerService.setApplicationEventPublisher(this.applicationEventPublisher);
            schedulerService.scheduleEndTask(waterOrderReferenced);
        }
    }

    @EventListener
    void onEndTaskComplete(WaterOrderEndTaskEvent waterOrderEndTaskEvent){
        WaterOrder waterOrderProcessed = waterOrderEndTaskEvent.getWaterOrder();
        WaterOrder waterOrderReferenced = waterOrderRepository.findById(waterOrderProcessed.getId()).get();

        if( waterOrderProcessed.getOrderStatus().toString().equals(Status.Started.toString())
                && waterOrderReferenced.getOrderStatus().toString().equals(Status.Started.toString()) ){
            waterOrderReferenced.setOrderStatus(Status.Delivered);

            waterOrderRepository.save(waterOrderReferenced);
            System.out.println("End Complete");
        }
    }
}
