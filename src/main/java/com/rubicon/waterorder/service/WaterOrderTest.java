package com.rubicon.waterorder.service;

import com.rubicon.waterorder.mapper.WaterOrderMapper;
import com.rubicon.waterorder.mapper.WaterOrderObjMapper;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
@Slf4j
public class WaterOrderTest {

    WaterOrderService waterOrderService;

    WaterOrderRepository waterOrderRepository;

    ApplicationEventPublisher publisher;

    WaterOrderProcessor waterOrderProcessor;

    SchedulerService schedulerService = SchedulerService.getInstance();

    public WaterOrderTest() {

    }

    @Autowired
    public WaterOrderTest(WaterOrderService waterOrderService,
                          WaterOrderRepository waterOrderRepository,
                          ApplicationEventPublisher publisher,
                          WaterOrderProcessor waterOrderProcessor){
        this.waterOrderService = waterOrderService;
        this.waterOrderRepository = waterOrderRepository;
        this.publisher = publisher;
        this.waterOrderProcessor = waterOrderProcessor;

    }

    public void test(Long farmId){



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        WaterOrderData wod1 = new WaterOrderData( farmId, now().plusSeconds(5).format(formatter) , "PT5S", "Requested");
        WaterOrderData wod2 = new WaterOrderData( farmId, now().plusSeconds(15).format(formatter), "PT5S", "Requested" );
        WaterOrderData wod3 = new WaterOrderData( farmId, now().plusSeconds(8).format(formatter), "PT5S", "Requested");
        WaterOrderData wod4 = new WaterOrderData( farmId, now().plusSeconds(18).format(formatter), "PT5S", "Requested");
        WaterOrderData wod5 = new WaterOrderData( farmId, now().minusSeconds(25).format(formatter), "PT10S", "Requested");

        log.info("********** TESTING ****************");

        log.info("In TEST- 1st Order ** Created? -->" + waterOrderProcessor.createOrder(farmId, wod1) + " " + wod1.toString());
        log.info("In TEST- 2nd Order ** Created? -->" + waterOrderProcessor.createOrder(farmId, wod2) + " " + wod2.toString());
        log.info("In TEST- 3rd Order ** Created? -->" + waterOrderProcessor.createOrder(farmId, wod3) + " " + wod3.toString());
        log.info("In TEST- 4th Order ** Created? -->" + waterOrderProcessor.createOrder(farmId, wod4) + " " + wod4.toString());
        log.info("In TEST- 5th Order ** Created? -->" + waterOrderProcessor.createOrder(farmId, wod5) + " " + wod5.toString());

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        List<WaterOrder> waterOrderList = waterOrderRepository.findTop2ByOrderByIdDesc();

       int index =0;
        if(waterOrderList.size() > 0){
            try{
                Random rn = new Random();
                index = rn.nextInt(waterOrderList.size()) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        WaterOrder waterOrder = waterOrderList.get(index);
        WaterOrderObjMapper waterOrderObjMapper = new WaterOrderObjMapper();
        WaterOrderData wod6 = waterOrderObjMapper.constructWaterOrderData(waterOrder);
        wod6.setOrderStatus(Status.Cancelled.name());
        log.info("In TEST- OBTAINED ******* Water Order Id - [ " + wod6.getId() + " ] to cancel.");

        try{
            Random rn = new Random();
            int answer = rn.nextInt(30000) + 1;
            log.info("In TEST- WAITING  ******* " + answer + " millisec (random) to cancel.");
            sleep(answer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("In TEST- CANCELLING ******* Water Order Id - [ " + wod6.getId() + " ]");
        waterOrderProcessor.cancelOrder(farmId, wod6);

        WaterOrderMapper waterOrderMapper = new WaterOrderMapper();

        log.info("In TEST- CANCELLING AGAIN in Scheduler -- must fail ******* Water Order Id - [ " + wod6.getId() + " ]");
        schedulerService.setApplicationEventPublisher(publisher);
        schedulerService.cancelTask(waterOrder);

    }
}