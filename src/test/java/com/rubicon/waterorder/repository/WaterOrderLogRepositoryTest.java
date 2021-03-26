package com.rubicon.waterorder.repository;

import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.model.WaterOrderLog;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Order(3)
class WaterOrderLogRepositoryTest {

    @Autowired
    WaterOrderLogRepository waterOrderLogRepository;

    @Autowired
    WaterOrderRepository waterOrderRepository;


    @Test
    void findBy_farmId_check() throws InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        WaterOrder waterOrder = new WaterOrder();
        waterOrder = new WaterOrder( 1L, 1111L,now().plusSeconds(10).format(formatter), 10L, Status.Requested);

        WaterOrder waterOrderSaved =  waterOrderRepository.save(waterOrder);

        WaterOrderLog waterOrderLog = new WaterOrderLog();
        waterOrderLog = new WaterOrderLog(null, waterOrder, waterOrder.getOrderStatus(), parse(now().format(formatter),formatter) );

        int initNoOfOrders = waterOrderLogRepository.findBy_farmId(1111L).size();
        WaterOrderLog waterOrderLogSaved = waterOrderLogRepository.save(waterOrderLog);
        assertTrue(initNoOfOrders < waterOrderLogRepository.findBy_farmId(1111L).size());

        assertNotNull(waterOrderLogSaved);
        assertEquals(Status.Requested, waterOrderLogSaved.getOrderStatus());
        assertEquals(1111L, waterOrderLogSaved.getWaterOrder().getFarmId());


       Thread.sleep(2000);

        waterOrderSaved.setOrderStatus(Status.Started);
        waterOrderSaved =  waterOrderRepository.save(waterOrderSaved);

        assertEquals(Status.Started, waterOrderSaved.getOrderStatus());


        WaterOrderLog waterOrderLog1 = new WaterOrderLog(null, waterOrderSaved, waterOrderSaved.getOrderStatus(), parse(now().format(formatter),formatter) );

        initNoOfOrders = waterOrderLogRepository.findBy_farmId(1111L).size();
        waterOrderLogSaved = waterOrderLogRepository.save(waterOrderLog1);
        assertTrue(initNoOfOrders < waterOrderLogRepository.findBy_farmId(1111L).size());
    }
}