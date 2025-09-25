package com.example.demo.dto;

import com.example.demo.model.VehicleType;

public class SlotDto {
    private Long id;
    private Integer slotNumber;
    private Long floorId;
    private VehicleType vehicleType;

    public SlotDto(Long id, Integer slotNumber, Long floorId, VehicleType vehicleType) {
        this.id = id;
        this.slotNumber = slotNumber;
        this.floorId = floorId;
        this.vehicleType = vehicleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
