package com.rubicon.waterorder.controller;

import com.rubicon.waterorder.mapper.WaterOrderMapper;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import com.rubicon.waterorder.service.SchedulerService;
import com.rubicon.waterorder.service.WaterOrderService;
import com.rubicon.waterorder.validator.WaterOrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@RestController
public class WaterOrderController {


    WaterOrderService waterOrderService;

    WaterOrderRepository waterOrderRepository;

    ApplicationEventPublisher publisher;

    WaterOrderMapper waterOrderMapper;

    WaterOrderValidator waterOrderValidator;

    SchedulerService schedulerService = SchedulerService.getInstance();

    @Autowired
    public WaterOrderController(WaterOrderService waterOrderService,
                                WaterOrderRepository waterOrderRepository,
                                ApplicationEventPublisher publisher,
                                WaterOrderMapper waterOrderMapper,
                                WaterOrderValidator waterOrderValidator) {
        this.waterOrderService = waterOrderService;
        this.waterOrderRepository = waterOrderRepository;
        this.publisher = publisher;
        this.waterOrderMapper = waterOrderMapper;
        this.waterOrderValidator = waterOrderValidator;
    }




    @GetMapping("/order")
    public List<WaterOrder> getOrders() {
        return waterOrderRepository.findAll();
    }

    @GetMapping("/order/{orderId}")
    public Optional<WaterOrder> getOrderById(@PathVariable Long orderId) {

        return waterOrderRepository.findById(orderId);
    }

    @PostMapping("/farm/{farmId}/order")
    public ResponseEntity<Void> createOrder(
            @PathVariable Long farmId,
            @RequestBody WaterOrderData waterOrderData){
        //System.out.println(waterOrderData.toString());

        if(waterOrderValidator.isValidDate(waterOrderData.getStartDateTime())){
            return ResponseEntity.badRequest().build();
        }
        if(waterOrderValidator.isFutureDate(waterOrderData.getStartDateTime())){
            return ResponseEntity.badRequest().build();
        }
        if(waterOrderValidator.isOrderOverlap(waterOrderData)){
            return ResponseEntity.badRequest().build();
        }


        WaterOrder waterOrder = new WaterOrder();
        WaterOrder waterOrderToCreate = waterOrderMapper.constructWaterOrder(waterOrderData, waterOrder);
        WaterOrder waterOrderCreated = waterOrderRepository.save(waterOrder);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(waterOrderCreated.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/farm/{farmId}/order/{orderId}")
    public List<WaterOrder> getOrders(@PathVariable Long orderId, @PathVariable Long farmId) {

        return waterOrderRepository.findByIdAndFarmId(orderId, farmId);
    }

    @GetMapping("farm/{farmId}/orders/1")
    public WaterOrder HelloWorld(@PathVariable Long farmId) {

        System.out.println(LocalDateTime.parse("2021-03-19T18:30:00"));

        LocalDateTime dt = LocalDateTime.parse("2021-03-19T10:30:00");
        LocalDateTime dtt = dt;
        //WaterOrder wo = new WaterOrder(1L, farmId, now(), 10800L, Status.Requested);
        WaterOrder wo1 = new WaterOrder(1L, farmId, now().plusSeconds(180) , 300L, Status.Requested);
        WaterOrder wo2 = new WaterOrder(2L, farmId, now().plusSeconds(660), 120L, Status.Requested);
        WaterOrder wo3 = new WaterOrder(3L, farmId, now().plusSeconds(540), 200L, Status.Requested);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //String formatDateTime = startDateTime.format(formatter);

        WaterOrderData wod1 = new WaterOrderData( farmId, now().plusSeconds(180).format(formatter) , 300L);
        WaterOrderData wod2 = new WaterOrderData( farmId, now().plusSeconds(660).format(formatter), 120L);
        WaterOrderData wod3 = new WaterOrderData( farmId, now().plusSeconds(540).format(formatter), 200L);
        //System.out.println(wo1.getFlowDuration().getSeconds());

        waterOrderService.isOverlappingInDay(wod1, 0);
        System.out.println("******************");

        System.out.println("******************" + waterOrderService.isOverlappingInDay(wod1, 0));
        System.out.println("******************" + waterOrderService.isOverlappingInDay(wod2, 0));
        System.out.println("******************" + waterOrderService.isOverlappingInDay(wod3, 0));

        //TaskCompleteEvent taskCompleteEvent = new TaskCompleteEvent(this, wo1);
        //wo1.setApplicationEventPublisher(publisher);
        //System.out.println(publisher);
        //schedulerService.setApplicationEventPublisher(publisher);
        //schedulerService.scheduleStartTask(wo1);

        //System.out.println("dd");
        //waterOrderService.method1(wo1);
        //waterOrderService.method1(wo2);
        //waterOrderService.method1(wo3);



        waterOrderRepository.save(wo1);
        waterOrderRepository.save(wo2);
        waterOrderRepository.save(wo3);


        return wo1;



    }

}
