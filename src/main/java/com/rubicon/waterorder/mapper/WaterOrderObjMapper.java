package com.rubicon.waterorder.mapper;

import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;
import com.rubicon.waterorder.model.WaterOrderData;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class WaterOrderObjMapper {

    public WaterOrderObjMapper() {
    }

    public WaterOrderData constructWaterOrderData(WaterOrder waterOrder){

        WaterOrderData waterOrderData = new WaterOrderData();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        waterOrderData.setId(waterOrder.getId());
        waterOrderData.setFarmId(waterOrder.getFarmId());
        waterOrderData.setStartDateTime(waterOrder.getStartDateTime().format(formatter));
        waterOrderData.setFlowDuration(waterOrder.getFlowDuration().toString());
        waterOrderData.setOrderStatus(waterOrder.getOrderStatus().name());

        return waterOrderData;
    }
}
