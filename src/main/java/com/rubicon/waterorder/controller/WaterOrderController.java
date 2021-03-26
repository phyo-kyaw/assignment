package com.rubicon.waterorder.controller;

import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.repository.WaterOrderLogRepository;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import com.rubicon.waterorder.service.WaterOrderProcessor;
import com.rubicon.waterorder.service.WaterOrderTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class WaterOrderController {

    WaterOrderRepository waterOrderRepository;

    WaterOrderLogRepository waterOrderLogRepository;

    WaterOrderProcessor waterOrderProcessor;

    WaterOrderTest waterOrderTest;


    public WaterOrderController() {

    }

    @Autowired
    public WaterOrderController(WaterOrderRepository waterOrderRepository,
                                WaterOrderLogRepository waterOrderLogRepository,
                                WaterOrderTest waterOrderTest,
                                WaterOrderProcessor waterOrderProcessor) {
        this.waterOrderRepository = waterOrderRepository;
        this.waterOrderLogRepository = waterOrderLogRepository;
        this.waterOrderTest = waterOrderTest;
        this.waterOrderProcessor = waterOrderProcessor;
    }


    @PostMapping("/farm/{farmId}/order")
    public ResponseEntity<Void> createOrder(
            @PathVariable Long farmId,
            @RequestBody WaterOrderData waterOrderData){

        boolean isSuccess = waterOrderProcessor.createOrder(farmId, waterOrderData);
        //return new ResponseEntity<Void>(HttpStatus.OK);

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
    public List<WaterOrder> getOrder(@PathVariable Long orderId, @PathVariable Long farmId) {

        return waterOrderRepository.findByIdAndFarmId(orderId, farmId);
    }

    @GetMapping("/farm/{farmId}/orderLog")
    public List<WaterOrderLog> getOrderLogs(@PathVariable Long farmId) {
        System.out.println(farmId);
        return waterOrderProcessor.findBy_farmId(farmId);
    }

    //not working
/*    @GetMapping("/farm/{farmId}/waterOrderLog")
    public List<WaterOrderLog> getOrderLogs2(@PathVariable Long farmId) {
        System.out.println(farmId);
        return waterOrderLogRepository.findBy_farmId1(farmId);
    }*/

    //working
    @GetMapping("/farm/{farmId}/waterOrderLogs")
    public List<WaterOrderLog> getWaterOrderLogs(@PathVariable Long farmId) {
        System.out.println(farmId);
        return waterOrderLogRepository.findBy_farmId(farmId);
    }

/*    @GetMapping("/farm/{farmId}/waterOrder")
    public List<WaterOrder> getOrderLogs1(@PathVariable Long farmId) {
        System.out.println(farmId);
        return waterOrderRepository.findBy_farmId(farmId);
    }*/

    @GetMapping("/orders")
    public List<WaterOrder> getOrders() {
        return waterOrderRepository.findAll();
    }

    @GetMapping("/orderLogs")
    public List<WaterOrderLog> getOrderLogs() {
        return waterOrderLogRepository.findAll();
    }


    @GetMapping("/order/{orderId}")
    public WaterOrder getOrderById(@PathVariable Long orderId) {

        return waterOrderRepository.findById(orderId).get();
    }

    @GetMapping("test")
    public String test() throws InterruptedException {

        waterOrderTest.test(3333L);

        return "Test executed. please check on console log.";

    }

}
