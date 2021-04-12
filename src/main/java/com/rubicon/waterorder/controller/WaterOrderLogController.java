package com.rubicon.waterorder.controller;


import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.repository.WaterOrderLogRepository;
import com.rubicon.waterorder.service.WaterOrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins={"http://13.211.116.66:80", "http://13.211.116.66","http://localhost:80", "http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class WaterOrderLogController {

    WaterOrderLogRepository waterOrderLogRepository;

    WaterOrderProcessor waterOrderProcessor;



    public WaterOrderLogController() {

    }

    @Autowired
    public WaterOrderLogController(WaterOrderLogRepository waterOrderLogRepository,
                                   WaterOrderProcessor waterOrderProcessor) {
        this.waterOrderLogRepository = waterOrderLogRepository;
        this.waterOrderProcessor = waterOrderProcessor;
    }

    @GetMapping("/farm/{farmId}/orderLog")
    public List<WaterOrderLog> getOrderLogs(@PathVariable Long farmId) {
        System.out.println(farmId);
        return waterOrderProcessor.findBy_farmId(farmId);
    }

    @GetMapping("/farm/{farmId}/order/{orderId}/orderLog")
    public List<WaterOrderLog> getOrderLogsByFarmId_OrderId(@PathVariable Long farmId, @PathVariable Long orderId) {
        System.out.println(farmId);
        return waterOrderProcessor.findBy_farmId_orderId(farmId, orderId);
    }

    //working
    @GetMapping("/farm/{farmId}/waterOrderLogs")
    public List<WaterOrderLog> getWaterOrderLogs(@PathVariable Long farmId) {
        System.out.println(farmId);
        return waterOrderLogRepository.findBy_farmId(farmId);
    }

    @GetMapping("/farm/{farmId}/order/{waterOrderId}/waterOrderLogs")
    public List<WaterOrderLog> getOrderLogsByFarmId_WaterOrderId(@PathVariable Long farmId, @PathVariable Long waterOrderId) {
        System.out.println(farmId);
        return waterOrderLogRepository.findBy_farmId_waterOrderId(farmId, waterOrderId);
    }

    @GetMapping("/orderLogs")
    public List<WaterOrderLog> getOrderLogs() {
        return waterOrderLogRepository.findAll();
    }



    @GetMapping("/refreshWaterOrderLogDb")
    public String refresh() {

        waterOrderLogRepository.deleteAll();
        return "Database refresh done..";

    }
}