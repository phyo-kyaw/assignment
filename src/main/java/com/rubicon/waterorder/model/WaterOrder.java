package com.rubicon.waterorder.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Entity
public class WaterOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long farmId;

    private LocalDateTime startDateTime;

    private Duration flowDuration;

    @Enumerated(EnumType.STRING)
    private Status orderStatus;

    @OneToMany(mappedBy = "waterOrder")
    private List<WaterOrderLog> waterOrderLogs;


    public WaterOrder() {
    }


    public WaterOrder(Long id, Long farmId, LocalDateTime startDateTime, Long flowDurationInSec, Status orderStatus) {
        this.id = id;
        this.farmId = farmId;
        this.startDateTime = startDateTime;
        this.flowDuration = Duration.ofSeconds(flowDurationInSec);
        this.orderStatus = orderStatus;
    }

    public WaterOrder(Long id, Long farmId, String startDateTime, Long flowDurationInSec, Status orderStatus) {
        this.id = id;
        this.farmId = farmId;
        this.startDateTime = LocalDateTime.parse(startDateTime);
        this.flowDuration = Duration.ofSeconds(flowDurationInSec);
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getFarmId() {
        return farmId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public Duration getFlowDuration() {
        return flowDuration;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setFlowDuration(Duration flowDuration) {
        this.flowDuration = flowDuration;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = startDateTime.format(formatter);

        return "WaterOrder{" +
                "id=" + id +
                ", farmId=" + farmId +
                ", startDateTime=" + formatDateTime +
                ", flowDurationInSec=" + flowDuration.getSeconds() +
                ", orderStatus=" + orderStatus +
                '}';
    }


}
