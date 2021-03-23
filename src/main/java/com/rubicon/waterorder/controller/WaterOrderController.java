package com.rubicon.waterorder.controller;

import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.repository.WaterOrderLogRepository;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import com.rubicon.waterorder.service.WaterOrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class WaterOrderController {

    WaterOrderRepository waterOrderRepository;

    WaterOrderLogRepository waterOrderLogRepository;

    WaterOrderProcessor waterOrderProcessor;


    public WaterOrderController() {

    }

    @Autowired
    public WaterOrderController(WaterOrderRepository waterOrderRepository,
                                WaterOrderLogRepository waterOrderLogRepository,
                                WaterOrderProcessor waterOrderProcessor) {
        this.waterOrderRepository = waterOrderRepository;
        this.waterOrderLogRepository = waterOrderLogRepository;
        this.waterOrderProcessor = waterOrderProcessor;
    }


    @GetMapping("/farm/{farmId}/orderLog")
    public List<WaterOrderLog> getOrderLogs(Long farmId) {
        return waterOrderLogRepository.findAll();
    }

    @GetMapping("/order")
    public List<WaterOrder> getOrders() {
        return waterOrderRepository.findAll();
    }

    @GetMapping("/order/{orderId}")
    public WaterOrder getOrderById(@PathVariable Long orderId) {

        return waterOrderRepository.findById(orderId).get();
    }

    @PostMapping("/farm/{farmId}/order")
    public ResponseEntity<Void> createOrder(
            @PathVariable Long farmId,
            @RequestBody WaterOrderData waterOrderData){
        System.out.println("1 ");
        boolean isSuccess = waterOrderProcessor.createOrder(farmId, waterOrderData);
        System.out.println("2 " + isSuccess);
        if(isSuccess){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/farm/{farmId}/order/{id}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long farmId,
            @RequestBody WaterOrderData waterOrderData){
        System.out.println("1 ");
        boolean isSuccess = waterOrderProcessor.cancelOrder(farmId, waterOrderData );
        System.out.println("2 ");
        if(isSuccess){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/farm/{farmId}/order/{orderId}")
    public List<WaterOrder> getOrders(@PathVariable Long orderId, @PathVariable Long farmId) {

        return waterOrderRepository.findByIdAndFarmId(orderId, farmId);
    }

    @GetMapping("farm/{farmId}/test")
    public String test(@PathVariable Long farmId) {

        return "Test executed. please check on console log.";

    }

}
