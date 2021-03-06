package com.rubicon.waterorder.mapper;

import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class WaterOrderMapper {

    public WaterOrderMapper() {
    }

    public WaterOrder constructWaterOrder(WaterOrderData waterOrderData){

        WaterOrder waterOrder = new WaterOrder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        waterOrder.setId(waterOrderData.getId());
        waterOrder.setFarmId(waterOrderData.getFarmId());
        waterOrder.setStartDateTime(LocalDateTime.parse(waterOrderData.getStartDateTime(), formatter));
        waterOrder.setFlowDuration(Duration.parse(waterOrderData.getFlowDuration()));
        waterOrder.setOrderStatus(Status.valueOf(waterOrderData.getOrderStatus()));
        return waterOrder;
    }
}
