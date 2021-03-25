package com.rubicon.waterorder.validator;

import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.model.WaterOrderLog;
import com.rubicon.waterorder.service.WaterOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaterOrderValidatorTest {

    @Mock
    WaterOrderService waterOrderService;

    @InjectMocks
    WaterOrderValidator waterOrderValidator;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        List<WaterOrder> waterOrders = new ArrayList<>();
        WaterOrder waterOrder = new WaterOrder();
        waterOrders.add(waterOrder);

        List<WaterOrderLog> waterOrderLogs = new ArrayList<>();
        WaterOrderLog waterOrderLog = new WaterOrderLog();
        waterOrderLogs.add(waterOrderLog);

        //when(waterOrderRepository.findByIdAndFarmId(anyLong(), anyLong())).thenReturn(waterOrders);

        mockMvc = MockMvcBuilders.standaloneSetup(waterOrderValidator).build();
    }

    @Test
    void isValidDate_check() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String validDate = now().plusSeconds(10).format(formatter);
        String notValidDate = "2021-22-01 23:23:51";
        String futureDate = now().minusSeconds(10).format(formatter);

        assertTrue(waterOrderValidator.isValidDate(validDate));
        assertFalse(waterOrderValidator.isValidDate(notValidDate));

    }

    @Test
    void isFutureDate_check() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String validDate = now().plusSeconds(10).format(formatter);
        String notFutureDate = now().minusSeconds(10).format(formatter);

        assertTrue(waterOrderValidator.isFutureDate(validDate));
        assertFalse(waterOrderValidator.isFutureDate(notFutureDate));

    }

    @Test
    void isOrderOverlap_check() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData waterOrderData = new WaterOrderData( 112L, now().plusSeconds(10).format(formatter) , "PT10S", "Requested");

        when(waterOrderService.isOverlappingInDay(waterOrderData, 0)).thenReturn(true);

        assertTrue(waterOrderValidator.isOrderOverlap(waterOrderData));

        when(waterOrderService.isOverlappingInDay(waterOrderData, 0)).thenReturn(false);

        assertFalse(waterOrderValidator.isOrderOverlap(waterOrderData));

    }

    @Test
    void isDeliveredOrCancelled_check() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        WaterOrderData waterOrderData = new WaterOrderData( 112L, now().plusSeconds(10).format(formatter) , "PT10S", "Requested");

        when(waterOrderService.isDeliveredOrCancelled(waterOrderData.getId())).thenReturn(true);

        assertTrue(waterOrderValidator.isDeliveredOrCancelled(waterOrderData));

        when(waterOrderService.isDeliveredOrCancelled(waterOrderData.getId())).thenReturn(false);

        assertFalse(waterOrderValidator.isDeliveredOrCancelled(waterOrderData));
    }
}