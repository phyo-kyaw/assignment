package com.rubicon.waterorder.service;

import com.rubicon.waterorder.mapper.WaterOrderLogMapper;
import com.rubicon.waterorder.mapper.WaterOrderMapper;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.repository.WaterOrderLogRepository;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import com.rubicon.waterorder.validator.WaterOrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
public class WaterOrderProcessor {

    WaterOrderRepository waterOrderRepository;

    WaterOrderLogRepository waterOrderLogRepository;

    ApplicationEventPublisher publisher;

    WaterOrderMapper waterOrderMapper;

    WaterOrderValidator waterOrderValidator;

    WaterOrderLogMapper waterOrderLogMapper;

    SchedulerService schedulerService = SchedulerService.getInstance();

    public WaterOrderProcessor() {
    }

    @Autowired
    public WaterOrderProcessor(WaterOrderRepository waterOrderRepository,
                               WaterOrderLogRepository waterOrderLogRepository,
                               ApplicationEventPublisher publisher,
                               WaterOrderMapper waterOrderMapper,
                               WaterOrderValidator waterOrderValidator,
                               WaterOrderLogMapper waterOrderLogMapper) {
        this.waterOrderRepository = waterOrderRepository;
        this.waterOrderLogRepository = waterOrderLogRepository;
        this.publisher = publisher;
        this.waterOrderMapper = waterOrderMapper;
        this.waterOrderValidator = waterOrderValidator;
        this.waterOrderLogMapper = waterOrderLogMapper;
    }

    public boolean createOrder(Long farmId, WaterOrderData waterOrderData){

        if(!waterOrderValidator.isValidDate(waterOrderData.getStartDateTime())){
            return false;
        }

        if(!waterOrderValidator.isFutureDate(waterOrderData.getStartDateTime())){
            return false;
        }

        if(waterOrderValidator.isOrderOverlap(waterOrderData)){
            return false;
        }

        WaterOrder waterOrderToCreate = waterOrderMapper.constructWaterOrder(waterOrderData);
        WaterOrder waterOrderCreated = waterOrderRepository.save(waterOrderToCreate);

        WaterOrderLog waterOrderLogToCreate = waterOrderLogMapper.constructWaterOrderLog(waterOrderCreated);
        WaterOrderLog waterOrderLogCreated = waterOrderLogRepository.save(waterOrderLogToCreate);

        schedulerService.setApplicationEventPublisher(this.publisher);
        schedulerService.scheduleStartTask(waterOrderCreated);

        return true;
    }

    public boolean cancelOrder(Long farmId, WaterOrderData waterOrderData) {

        //requested waterOrderData must be with cancel status
        if (!waterOrderData.getOrderStatus().toString().equals(Status.Cancelled.toString())) {
            return false;
        }

        //check with db if it's been delivered or cancelled.
        if (waterOrderValidator.isDeliveryOrCancelled(waterOrderData)) {
            return false;
        }

        WaterOrder waterOrderToCancel = waterOrderMapper.constructWaterOrder(waterOrderData);

        //scheduler to cancel the task
        schedulerService.setApplicationEventPublisher(this.publisher);
        schedulerService.cancelTask(waterOrderToCancel);

        return true;
    }
}
