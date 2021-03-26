package com.rubicon.waterorder.service;

import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WaterOrderService {

    WaterOrderRepository waterOrderRepo;

    @Autowired
    public WaterOrderService(WaterOrderRepository waterOrderRepo) {
        this.waterOrderRepo = waterOrderRepo;
    }

    public boolean isDeliveredOrCancelled(Long id){
        WaterOrder waterOrderReferenced = waterOrderRepo.findById(id).get();
        String status = waterOrderReferenced.getOrderStatus().toString();
        if( status.equals(Status.Cancelled.toString()) || status.equals(Status.Delivered.toString()) ) {
            return true;
        }
        return false;
    }

    public boolean isOverlappingInDay(WaterOrderData waterOrderData, int daysToConsider) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.parse(waterOrderData.getStartDateTime(),formatter);
        LocalDateTime end = start.plus(Duration.parse(waterOrderData.getFlowDuration()));

        LocalDateTime startDateTimeConsidered = start
                .toLocalDate()
                .atStartOfDay()
                .minusDays(daysToConsider);

        LocalDateTime endDateTimeConsidered = end
                .toLocalDate()
                .atStartOfDay()
                .plusHours(23)
                .plusMinutes(59)
                .plusSeconds(59)
                .plusDays(daysToConsider);


        List<WaterOrder> waterOrderListBefore = new ArrayList<>();
        List<WaterOrder> waterOrderListAfter = new ArrayList<>();

        waterOrderListBefore = waterOrderRepo
                .findByFarmIdAndByStartDateTimeBetweenOrderByStartDateTimeDesc(waterOrderData.getFarmId(), startDateTimeConsidered, end);

        if (waterOrderListBefore.size() >= 1) {
            WaterOrder waterOrderConsidered = waterOrderListBefore.get(0);
            if (checkOverlap(start, end, waterOrderConsidered)) return true;
        }

        waterOrderListAfter = waterOrderRepo.
                findByFarmIdAndByStartDateTimeBetweenOrderByStartDateTimeAsc(waterOrderData.getFarmId(), start, endDateTimeConsidered);

        if(waterOrderListAfter.size() >= 1) {
            WaterOrder waterOrderConsidered = waterOrderListAfter.get(0);

            if (checkOverlap(start, end, waterOrderConsidered)) return true;
        }

        return false;
    }

    private boolean checkOverlap(LocalDateTime start, LocalDateTime end, WaterOrder waterOrderConsidered) {

        LocalDateTime start2 = waterOrderConsidered.getStartDateTime();
        LocalDateTime end2 = waterOrderConsidered.getStartDateTime()
                .plus(waterOrderConsidered.getFlowDuration());

        boolean isOverlapped = isOverlapping(start, end, start2, end2);

        if (isOverlapped) {
            log.error("Water Order Id : [" + waterOrderConsidered.getId() + "] is overlapped with existing order.");
            return true;
        }
        return false;
    }

    public boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);

    }

}