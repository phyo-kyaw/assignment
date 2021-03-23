package com.rubicon.waterorder.service;

import com.rubicon.waterorder.mapper.WaterOrderLogMapper;
import com.rubicon.waterorder.mapper.WaterOrderMapper;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.repository.WaterOrderLogRepository;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import com.rubicon.waterorder.validator.WaterOrderValidator;
import org.springframework.context.ApplicationEventPublisher;

import java.time.format.DateTimeFormatter;
import java.util.Random;

import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;

public class WaterOrderTest {


    WaterOrderService waterOrderService;

    WaterOrderRepository waterOrderRepository;

    WaterOrderLogRepository waterOrderLogRepository;

    ApplicationEventPublisher publisher;

    WaterOrderMapper waterOrderMapper;

    WaterOrderValidator waterOrderValidator;

    WaterOrderLogMapper waterOrderLogMapper;

    WaterOrderProcessor waterOrderProcessor;

    SchedulerService schedulerService = SchedulerService.getInstance();

    public void test(Long farmId){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //String formatDateTime = startDateTime.format(formatter);

        WaterOrderData wod1 = new WaterOrderData( farmId, now().plusSeconds(10).format(formatter) , 10L, Status.Requested);
        WaterOrderData wod2 = new WaterOrderData( farmId, now().plusSeconds(30).format(formatter), 10L, Status.Requested );
        WaterOrderData wod3 = new WaterOrderData( farmId, now().plusSeconds(15).format(formatter), 10L, Status.Requested);
        WaterOrderData wod4 = new WaterOrderData( farmId, now().plusSeconds(25).format(formatter), 10L, Status.Requested);
        WaterOrderData wod5 = new WaterOrderData( farmId, now().minusSeconds(25).format(formatter), 10L, Status.Requested);

        System.out.println("******************");

        System.out.println("1 ******* " + waterOrderProcessor.createOrder(farmId, wod1));
        System.out.println("2 ******* " + waterOrderProcessor.createOrder(farmId, wod2));
        System.out.println("3 ******* " + waterOrderProcessor.createOrder(farmId, wod3));
        System.out.println("4 ******* " + waterOrderProcessor.createOrder(farmId, wod4));
        System.out.println("5 ******* " + waterOrderProcessor.createOrder(farmId, wod5));

        System.out.println("1 ****************** " + waterOrderService.isOverlappingInDay(wod1, 0));
        System.out.println("2 ******************" + waterOrderService.isOverlappingInDay(wod2, 0));
        System.out.println("3 ******************" + waterOrderService.isOverlappingInDay(wod3, 0));
        System.out.println("4 ******************" + waterOrderService.isOverlappingInDay(wod4, 0));
        System.out.println("5 ******************" + waterOrderService.isOverlappingInDay(wod5, 0));

        try{
            Random rn = new Random();
            int answer = rn.nextInt(15000) + 1;
            sleep(answer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wod1.setId(1L);
        System.out.println("6 ******* " + waterOrderProcessor.cancelOrder(farmId, wod1));

        WaterOrder waterOrderToCreate = waterOrderMapper.constructWaterOrder(wod1);
        WaterOrder waterOrderCreated = waterOrderRepository.save(waterOrderToCreate);

        schedulerService.setApplicationEventPublisher(publisher);
        schedulerService.cancelTask(waterOrderToCreate);


    }
}
