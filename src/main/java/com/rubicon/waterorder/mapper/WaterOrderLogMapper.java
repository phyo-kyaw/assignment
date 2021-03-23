package com.rubicon.waterorder.mapper;

import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;


@Component
public class WaterOrderLogMapper {


    public WaterOrderLog constructWaterOrderLog(WaterOrder waterOrder){

        WaterOrderLog waterOrderLog = new WaterOrderLog();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        waterOrderLog.setWaterOrder(waterOrder);
        waterOrderLog.setOrderStatus(waterOrder.getOrderStatus());
        waterOrderLog.setCreatedDateTime(LocalDateTime.parse(now().format(formatter), formatter));

        return waterOrderLog;
    }
}
