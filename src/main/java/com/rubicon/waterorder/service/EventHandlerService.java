package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.WaterOrderCancelTaskEvent;
import com.rubicon.waterorder.event.WaterOrderEndTaskEvent;
import com.rubicon.waterorder.event.WaterOrderStartTaskEvent;
import com.rubicon.waterorder.mapper.WaterOrderLogMapper;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.repository.WaterOrderLogRepository;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class EventHandlerService {

    private WaterOrderRepository waterOrderRepository;

    private WaterOrderLogRepository waterOrderLogRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    private WaterOrderLogMapper waterOrderLogMapper;

    private SchedulerService schedulerService = SchedulerService.getInstance();

    public EventHandlerService() {
    }

    @Autowired
    public EventHandlerService(WaterOrderRepository waterOrderRepository,
                               WaterOrderLogRepository waterOrderLogRepository,
                               ApplicationEventPublisher applicationEventPublisher,
                               WaterOrderLogMapper waterOrderLogMapper) {
        this.waterOrderRepository = waterOrderRepository;
        this.waterOrderLogRepository = waterOrderLogRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.waterOrderLogMapper = waterOrderLogMapper;
    }

    @EventListener
    void onStartTaskComplete(WaterOrderStartTaskEvent waterOrderStartTaskEvent){

        WaterOrder waterOrderProcessed = waterOrderStartTaskEvent.getWaterOrder();
        WaterOrder waterOrderReferenced = waterOrderRepository.findById(waterOrderProcessed.getId()).get();

        if( waterOrderProcessed.getOrderStatus().toString().equals(Status.Requested.toString())
        && waterOrderReferenced.getOrderStatus().toString().equals(Status.Requested.toString()) ){
            waterOrderReferenced.setOrderStatus(Status.Started);

            waterOrderRepository.save(waterOrderReferenced);

            WaterOrderLog waterOrderLogToCreate = waterOrderLogMapper.constructWaterOrderLog(waterOrderReferenced);
            waterOrderLogRepository.save(waterOrderLogToCreate);

            schedulerService.setApplicationEventPublisher(this.applicationEventPublisher);
            schedulerService.removeTaskFromQueue(waterOrderProcessed);
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

            WaterOrderLog waterOrderLogToCreate = waterOrderLogMapper.constructWaterOrderLog(waterOrderReferenced);
            waterOrderLogRepository.save(waterOrderLogToCreate);

            schedulerService.setApplicationEventPublisher(this.applicationEventPublisher);
            schedulerService.removeTaskFromQueue(waterOrderProcessed);

        }
    }

    @EventListener
    void onCancelTaskComplete(WaterOrderCancelTaskEvent waterOrderCancelTaskEvent){
        WaterOrder waterOrderProcessed = waterOrderCancelTaskEvent.getWaterOrder();
        WaterOrder waterOrderReferenced = waterOrderRepository.findById(waterOrderProcessed.getId()).get();
        String status = waterOrderReferenced.getOrderStatus().toString();

        if( status.equals(Status.Cancelled.toString()) || status.equals(Status.Delivered.toString()) ) {
            System.out.println("The order has been delivered or cancelled");
        }

        waterOrderReferenced.setOrderStatus(Status.Cancelled);

        waterOrderRepository.save(waterOrderReferenced);


        WaterOrderLog waterOrderLogToCreate = waterOrderLogMapper.constructWaterOrderLog(waterOrderReferenced);
        waterOrderLogRepository.save(waterOrderLogToCreate);

    }
}
