package com.rubicon.waterorder.controller;

import com.rubicon.waterorder.event.TaskCompleteEvent;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import com.rubicon.waterorder.service.SchedulerService;
import com.rubicon.waterorder.service.WaterOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;

@RestController
public class WaterOrderController {


    WaterOrderService waterOrderService;


    WaterOrderRepository waterOrderRepository;

    ApplicationEventPublisher publisher;

    SchedulerService schedulerService = SchedulerService.getInstance();

    @Autowired
    public WaterOrderController(WaterOrderService waterOrderService,
                                WaterOrderRepository waterOrderRepository,
                                ApplicationEventPublisher publisher) {
        this.waterOrderService = waterOrderService;
        this.waterOrderRepository = waterOrderRepository;
        this.publisher = publisher;
    }

    @GetMapping("farm/{farmId}/order")
    //public WaterOrder HelloWorld() {
    public WaterOrder HelloWorld(@PathVariable Long farmId) {

        System.out.println(LocalDateTime.parse("2021-03-19T18:30:00"));

        LocalDateTime dt = LocalDateTime.parse("2021-03-19T10:30:00");
        LocalDateTime dtt = dt;
        //WaterOrder wo = new WaterOrder(1L, farmId, now(), 10800L, Status.Requested);
        WaterOrder wo1 = new WaterOrder(1L, farmId, now().plusSeconds(10) , 30L, Status.Requested);
        WaterOrder wo2 = new WaterOrder(2L, farmId, now().plusSeconds(122), 5L, Status.Requested);
        WaterOrder wo3 = new WaterOrder(3L, farmId, now().plusSeconds(200), 10L, Status.Requested);

        System.out.println(wo1.getFlowDuration().getSeconds());

        waterOrderService.isOverlappingInSameDay(wo1, 0);

        if(!waterOrderService.isOverlappingInSameDay(wo1, 0)){

        }

        System.out.println(waterOrderService.isOverlappingInSameDay(wo1, 0));
        System.out.println(waterOrderService.isOverlappingInSameDay(wo2, 0));
        System.out.println(waterOrderService.isOverlappingInSameDay(wo3, 0));

        //TaskCompleteEvent taskCompleteEvent = new TaskCompleteEvent(this, wo1);
        //wo1.setApplicationEventPublisher(publisher);
        System.out.println(publisher);
        schedulerService.setApplicationEventPublisher(publisher);
        schedulerService.scheduleStartTask(wo1);

        System.out.println("dd");
        //waterOrderService.method1(wo1);
        //waterOrderService.method1(wo2);
        //waterOrderService.method1(wo3);



        waterOrderRepository.save(wo1);


        return wo1;



    }

}
