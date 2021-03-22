package com.rubicon.waterorder.model;

import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Service
public class WaterOrderLog {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private WaterOrder waterOrder;

    private Status orderStatus;

    private LocalDateTime createdDateTime;

    public WaterOrderLog() {
    }

    public WaterOrderLog(Long id, WaterOrder waterOrder, Status orderStatus, LocalDateTime createdDateTime) {
        this.id = id;
        this.waterOrder = waterOrder;
        this.orderStatus = orderStatus;
        this.createdDateTime = createdDateTime;
    }

    public Long getId() {
        return id;
    }

    public WaterOrder getWaterOrder() {
        return waterOrder;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWaterOrder(WaterOrder waterOrder) {
        this.waterOrder = waterOrder;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = createdDateTime.format(formatter);

        return "WaterOrderLog{" +
                "id=" + id +
                ", waterOrderId=" + waterOrder.getId().toString() +
                ", farmId=" + waterOrder.getFarmId().toString() +
                ", orderStatus=" + orderStatus.toString() +
                ", createdDateTime=" + formatDateTime +
                '}';
    }
}
