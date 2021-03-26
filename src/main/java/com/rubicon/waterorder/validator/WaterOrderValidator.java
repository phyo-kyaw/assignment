package com.rubicon.waterorder.validator;

import com.rubicon.waterorder.model.WaterOrderData;
import com.rubicon.waterorder.service.WaterOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class WaterOrderValidator {

    private WaterOrderService waterOrderService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final int daysToConsider = 0;

    public WaterOrderValidator() {
    }

    @Autowired
    public WaterOrderValidator(WaterOrderService waterOrderService) {
        this.waterOrderService = waterOrderService;
    }



    public boolean isValidDate(String dateTime) {
        try{
            LocalDateTime.parse(dateTime, formatter);
            return true;
        }
        catch(Exception ex)
        {
            log.error("Water Order Request - please check the start date (valid date).");
            return false;
        }
    }

    public boolean isFutureDate(String dateTime){

        try{
            LocalDateTime date = LocalDateTime.parse(dateTime, formatter);
            if( LocalDateTime.now().isBefore(date) ){
                return true;
            }
            else{
                log.error("Water Order Request - please check the start date (no past date?).");
                return false;
            }
        }
        catch(Exception ex)
        {
            log.error("Water Order Request - please check the start date (valid date?).");
            return false;
        }

    }

    public boolean isOrderOverlap(WaterOrderData waterOrderData){
        return waterOrderService.isOverlappingInDay(waterOrderData, daysToConsider);
    }

    public  boolean isDeliveredOrCancelled(WaterOrderData waterOrderData){
        return waterOrderService.isDeliveredOrCancelled(waterOrderData.getId());
    }
}
