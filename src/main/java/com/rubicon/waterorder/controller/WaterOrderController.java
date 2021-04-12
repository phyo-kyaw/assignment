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

@CrossOrigin(origins={"http://13.211.116.66:80", "http://13.211.116.66","http://localhost:80", "http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class WaterOrderController {

    WaterOrderRepository waterOrderRepository;

    WaterOrderProcessor waterOrderProcessor;

    WaterOrderTest waterOrderTest;


    public WaterOrderController() {

    }

    @Autowired
    public WaterOrderController(WaterOrderRepository waterOrderRepository,
                                WaterOrderTest waterOrderTest,
                                WaterOrderProcessor waterOrderProcessor) {
        this.waterOrderRepository = waterOrderRepository;
        this.waterOrderTest = waterOrderTest;
        this.waterOrderProcessor = waterOrderProcessor;
    }


    @PostMapping("/farm/{farmId}/order")
    public ResponseEntity<Void> createOrder(
            @PathVariable Long farmId,
            @RequestBody WaterOrderData waterOrderData) {

        boolean isSuccess = waterOrderProcessor.createOrder(farmId, waterOrderData);

        if (isSuccess) {
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/farm/{farmId}/order/{id}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long farmId,
            @PathVariable Long id,
            @RequestBody WaterOrderData waterOrderData) {
        System.out.println("1 ");
        boolean isSuccess = waterOrderProcessor.cancelOrder(farmId, waterOrderData);
        System.out.println("2 ");
        if (isSuccess) {
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/farm/{farmId}/order/{orderId}")
    public List<WaterOrder> getOrderByIdAndFarmId(@PathVariable Long orderId, @PathVariable Long farmId) {

        return waterOrderRepository.findByIdAndFarmId(orderId, farmId);
    }

    @GetMapping("/farm/{farmId}/order")
    public List<WaterOrder> getOrderByFarmId(@PathVariable Long farmId) {

        return waterOrderRepository.findByFarmId(farmId);
    }


    @GetMapping("/orders")
    public List<WaterOrder> getOrders() {
        return waterOrderRepository.findAll();
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

    @GetMapping("/refreshWaterOrderDb")
    public String refresh() {

        waterOrderRepository.deleteAll();

        return "Database refresh done..";


    }
}