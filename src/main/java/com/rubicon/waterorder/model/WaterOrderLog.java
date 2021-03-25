package com.rubicon.waterorder.model;

import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.parse;

@Entity
public class WaterOrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //@JoinColumn(name = "WATER_ORDER_ID", referencedColumnName = "ID")
    private WaterOrder waterOrder;

    @Enumerated(EnumType.STRING)
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

    public WaterOrderLog(Long id, WaterOrder waterOrder, Status orderStatus, String createdDateTime) {
        this.id = id;
        this.waterOrder = waterOrder;
        this.orderStatus = orderStatus;
        this.createdDateTime = parse(createdDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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
        System.out.println("Log toString ***************** -> " + id);
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
