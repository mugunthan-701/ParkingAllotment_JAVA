package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    private Long slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String vehicleNumber;
    private BigDecimal cost;

    public ReservationDto(Long id, Long slotId, LocalDateTime startTime, LocalDateTime endTime, String vehicleNumber, BigDecimal cost) {
        this.id = id;
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.vehicleNumber = vehicleNumber;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
