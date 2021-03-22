package com.rubicon.waterorder.model;

public class WaterOrderData {

    private Long farmId;

    private String startDateTime;

    private Long flowDuration;


    public WaterOrderData() {
    }

    public WaterOrderData(Long farmId, String startDateTime, Long flowDuration) {
        this.farmId = farmId;
        this.startDateTime = startDateTime;
        this.flowDuration = flowDuration;
    }

    public Long getFarmId() {
        return farmId;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public Long getFlowDuration() {
        return flowDuration;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setFlowDuration(Long flowDuration) {
        this.flowDuration = flowDuration;
    }

    @Override
    public String toString() {
        return "WaterOrderData{" +
                "farmId=" + farmId +
                ", startDateTime='" + startDateTime + '\'' +
                ", flowDuration=" + flowDuration +
                '}';
    }
}
