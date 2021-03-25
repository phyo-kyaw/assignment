package com.rubicon.waterorder.model;

public class WaterOrderData {


    private Long id;

    private Long farmId;

    private String startDateTime;

    private String flowDuration;

    private  String orderStatus;


    public WaterOrderData() {
    }

    public WaterOrderData(Long farmId, String startDateTime, String flowDuration, String orderStatus) {
        this.farmId = farmId;
        this.startDateTime = startDateTime;
        this.flowDuration = flowDuration;
        this.orderStatus = orderStatus;
    }

    public WaterOrderData(Long id, Long farmId, String startDateTime, String flowDuration, String orderStatus) {
        this.id = id;
        this.farmId = farmId;
        this.startDateTime = startDateTime;
        this.flowDuration = flowDuration;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getFarmId() {
        return farmId;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getFlowDuration() {
        return flowDuration;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setFlowDuration(String flowDuration) {
        this.flowDuration = flowDuration;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "WaterOrderData{" +
                "id=" + id +
                ", farmId=" + farmId +
                ", startDateTime='" + startDateTime + '\'' +
                ", flowDuration=" + flowDuration +
                ", OrderStatus=" + orderStatus +
                '}';
    }
}
