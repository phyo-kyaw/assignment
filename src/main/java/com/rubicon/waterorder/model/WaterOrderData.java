package com.rubicon.waterorder.model;

public class WaterOrderData {

    private Long id;

    private Long farmId;

    private String startDateTime;

    private Long flowDuration;

    private  Status OrderStatus;


    public WaterOrderData() {
    }

    public WaterOrderData(Long farmId, String startDateTime, Long flowDuration) {
        this.farmId = farmId;
        this.startDateTime = startDateTime;
        this.flowDuration = flowDuration;
    }

    public WaterOrderData(Long farmId, String startDateTime, Long flowDuration, Status orderStatus) {
        this.farmId = farmId;
        this.startDateTime = startDateTime;
        this.flowDuration = flowDuration;
        OrderStatus = orderStatus;
    }

    public WaterOrderData(Long id, Long farmId, String startDateTime, Long flowDuration, Status orderStatus) {
        this.id = id;
        this.farmId = farmId;
        this.startDateTime = startDateTime;
        this.flowDuration = flowDuration;
        OrderStatus = orderStatus;
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

    public Long getFlowDuration() {
        return flowDuration;
    }

    public Status getOrderStatus() {
        return OrderStatus;
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

    public void setFlowDuration(Long flowDuration) {
        this.flowDuration = flowDuration;
    }

    public void setOrderStatus(Status orderStatus) {
        OrderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "WaterOrderData{" +
                "id=" + id +
                ", farmId=" + farmId +
                ", startDateTime='" + startDateTime + '\'' +
                ", flowDuration=" + flowDuration +
                ", OrderStatus=" + OrderStatus +
                '}';
    }
}
