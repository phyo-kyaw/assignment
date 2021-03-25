package com.rubicon.waterorder.model;


import org.mockito.ArgumentMatcher;

public class WaterOrderDataWithID implements ArgumentMatcher<WaterOrderData> {

    private WaterOrderData  waterOrderData;

    public WaterOrderDataWithID(WaterOrderData  waterOrderData_) {
        System.out.println("Argument : " + waterOrderData_.getId());
        this.waterOrderData = waterOrderData_;
    }

    @Override
    public boolean matches(WaterOrderData  waterOrderData_) {
        System.out.println("************* matches fun  : " + waterOrderData_.toString());
        System.out.println("************* matches fun  : " + this.waterOrderData.toString());
        System.out.println(this.waterOrderData.getId().longValue() == waterOrderData_.getId().longValue());
        return ( this.waterOrderData.getId().longValue() == waterOrderData_.getId().longValue() );
    }
}
