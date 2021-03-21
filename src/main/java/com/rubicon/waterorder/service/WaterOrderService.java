package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.TaskCompleteEvent;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderTask;
import com.rubicon.waterorder.repository.WaterOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class WaterOrderService {

    @Autowired
    WaterOrderRepository waterOrderRepo;

    @Autowired
    SchedulerService schedulerService = SchedulerService.getInstance();

    public void method1(WaterOrder waterOrder){

        schedulerService.scheduleStartTask(waterOrder);
        System.out.println("m1 called");

    }

    public String method2(WaterOrder waterOrder){
        System.out.println("m2 called");
        //schedulerService.scheduleStopTask(waterOrder);
        return "hello";
    }

    public boolean isOverlappingInSameDay(WaterOrder waterOrder, int daysToConsider) {

        LocalDateTime startDateTimeConsidered = waterOrder.getStartDateTime()
                .toLocalDate()
                .atStartOfDay()
                .minusDays(daysToConsider);

        LocalDateTime endDateTimeConsidered = waterOrder.getStartDateTime()
                .plus(waterOrder.getFlowDuration())
                .toLocalDate()
                .atStartOfDay()
                .plusHours(23)
                .plusMinutes(59)
                .plusSeconds(59)
                .minusDays(daysToConsider);


        List<WaterOrder> waterOrderList = new ArrayList<>();
        List<WaterOrder> waterOrderListBefore = new ArrayList<>();
        List<WaterOrder> waterOrderListAfter = new ArrayList<>();

        System.out.println("hh" + waterOrderList.size() + "hh");

        waterOrderList = waterOrderRepo.findAllByStartDateTimeBetweenOrderByStartDateTimeDesc(
                startDateTimeConsidered,
                endDateTimeConsidered);


        System.out.println(startDateTimeConsidered.toString());
        System.out.println(endDateTimeConsidered.toString());
        System.out.println(waterOrderList.toString());


        waterOrderListBefore = waterOrderRepo.findAllByStartDateTimeBetweenOrderByStartDateTimeDesc(
                startDateTimeConsidered,
                waterOrder.getStartDateTime());

        if (waterOrderListBefore.size() >= 1) {
            WaterOrder waterOrderConsidered = waterOrderListBefore.get(0);
            boolean isOverlapped =  isOverlapping(waterOrder.getStartDateTime(),
                    waterOrder.getStartDateTime().plus(waterOrder.getFlowDuration()),
                    waterOrderConsidered.getStartDateTime(),
                    waterOrderConsidered.getStartDateTime().plus(waterOrderConsidered.getFlowDuration())
            );
            if(isOverlapped){
                System.out.println("overlapped");
                return true;
            }
        }

        waterOrderListAfter = waterOrderRepo.findAllByStartDateTimeBetweenOrderByStartDateTimeAsc(
                waterOrder.getStartDateTime(),
                endDateTimeConsidered);

        if(waterOrderListAfter.size() >= 1) {
            WaterOrder waterOrderConsidered = waterOrderListAfter.get(0);
            boolean isOverlapped =  isOverlapping(waterOrder.getStartDateTime(),
                    waterOrder.getStartDateTime().plus(waterOrder.getFlowDuration()),
                    waterOrderConsidered.getStartDateTime(),
                    waterOrderConsidered.getStartDateTime().plus(waterOrderConsidered.getFlowDuration())
            );

            if(isOverlapped){
                System.out.println("overlapped");
                return true;
            }
        }

        return false;
    }

    public boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);

    }
}