package com.rubicon.waterorder.service;

import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import jdk.jshell.Snippet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.Duration.parse;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WaterOrderServiceTest {

    @Mock
    WaterOrderRepository waterOrderRepository;

    @InjectMocks
    WaterOrderService waterOrderService;

    @BeforeEach
    void setup(){

    }

    @Test
    void isDeliveredOrCancelled() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        WaterOrderData wod1 = new WaterOrderData(111L, 222L, now().plusSeconds(10).format(formatter) , "PT10S", "Delivered");
        WaterOrderData wod2 = new WaterOrderData( 112L, 222L, now().plusSeconds(30).format(formatter), "PT10S", "Cancelled" );
        WaterOrderData wod3 = new WaterOrderData( 113L, 222L, now().plusSeconds(15).format(formatter), "PT10S", "Requested");

        WaterOrder wo1 = new WaterOrder(
                wod1.getId(),
                wod1.getFarmId(),
                LocalDateTime.parse(wod1.getStartDateTime(), formatter) ,
                parse(wod1.getFlowDuration()),
                Status.valueOf(wod1.getOrderStatus()));

        WaterOrder wo2 = new WaterOrder(
                wod2.getId(),
                wod2.getFarmId(),
                LocalDateTime.parse(wod2.getStartDateTime(), formatter) ,
                parse(wod2.getFlowDuration()),
                Status.valueOf(wod2.getOrderStatus()));

        WaterOrder wo3 = new WaterOrder(
                wod3.getId(),
                wod3.getFarmId(),
                LocalDateTime.parse(wod3.getStartDateTime(), formatter) ,
                parse(wod3.getFlowDuration()),
                Status.valueOf(wod3.getOrderStatus()));

        given(waterOrderRepository.
                findById(wo1.getId()))
                .willReturn(Optional.of(wo1));

        given(waterOrderRepository.
                findById(wo2.getId()))
                .willReturn(Optional.of(wo2));

        given(waterOrderRepository.
                findById(wo3.getId()))
                .willReturn(Optional.of(wo3));

        assertTrue(waterOrderService.isDeliveredOrCancelled(wod1.getId()));
        assertTrue(waterOrderService.isDeliveredOrCancelled(wod2.getId()));
        assertFalse(waterOrderService.isDeliveredOrCancelled(wod3.getId()));

    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    void isOverlappingInDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //existing data in Repo
        WaterOrderData wod1 = new WaterOrderData(111L, 222L, now().plusSeconds(10).format(formatter) , "PT10S", "Requested");
        WaterOrderData wod2 = new WaterOrderData( 112L, 222L, now().plusSeconds(30).format(formatter), "PT10S", "Requested" );
        //overlapped with wod1
        WaterOrderData wod3 = new WaterOrderData( 113L, 222L, now().plusSeconds(15).format(formatter), "PT10S", "Requested");
        //overlapped with wod2
        WaterOrderData wod4 = new WaterOrderData( 114L, 222L, now().plusSeconds(25).format(formatter), "PT10S", "Requested");
        //no overlap
        WaterOrderData wod5 = new WaterOrderData( 115L, 222L, now().plusSeconds(25).format(formatter), "PT10S", "Requested");

        WaterOrder wo1 = new WaterOrder(
                wod1.getId(),
                wod1.getFarmId(),
                LocalDateTime.parse(wod1.getStartDateTime(), formatter) ,
                parse(wod1.getFlowDuration()),
                Status.valueOf(wod1.getOrderStatus()));

        WaterOrder wo2 = new WaterOrder(
                wod2.getId(),
                wod2.getFarmId(),
                LocalDateTime.parse(wod2.getStartDateTime(), formatter) ,
                parse(wod2.getFlowDuration()),
                Status.valueOf(wod2.getOrderStatus()));

        WaterOrder wo3 = new WaterOrder(
                wod3.getId(),
                wod3.getFarmId(),
                LocalDateTime.parse(wod3.getStartDateTime(), formatter) ,
                parse(wod3.getFlowDuration()),
                Status.valueOf(wod3.getOrderStatus()));

        WaterOrder wo4 = new WaterOrder(
                wod4.getId(),
                wod4.getFarmId(),
                LocalDateTime.parse(wod4.getStartDateTime(), formatter) ,
                parse(wod4.getFlowDuration()),
                Status.valueOf(wod4.getOrderStatus()));

        WaterOrder wo5 = new WaterOrder(
                wod5.getId(),
                wod5.getFarmId(),
                LocalDateTime.parse(wod5.getStartDateTime(), formatter) ,
                parse(wod5.getFlowDuration()),
                Status.valueOf(wod5.getOrderStatus()));


        List<WaterOrder> beforeList = new ArrayList<>();
        beforeList.add(wo1);
        beforeList.add(new WaterOrder());

        List<WaterOrder> afterList = new ArrayList<>();
        afterList.add(wo2);
        afterList.add(new WaterOrder());

        given(waterOrderRepository.
                findByFarmIdAndByStartDateTimeBetweenOrderByStartDateTimeDesc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(beforeList);

        given(waterOrderRepository.
                findByFarmIdAndByStartDateTimeBetweenOrderByStartDateTimeAsc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(afterList);

        //overlapped with wod1
        assertTrue(waterOrderService.isOverlappingInDay(wod3, 0));
        //overlapped with wod2
        assertTrue(waterOrderService.isOverlappingInDay(wod4, 0));
        //no overlap
        assertFalse(waterOrderService.isOverlappingInDay(wod5, 0));

    }
}