package com.example.demo.dto;

import com.example.demo.model.VehicleType;

public class AvailabilityDto {
    private Long slotId;
    private Integer slotNumber;
    private Long floorId;
    private Integer floorNumber;
    private VehicleType vehicleType;

    public AvailabilityDto(Long slotId, Integer slotNumber, Long floorId, Integer floorNumber, VehicleType vehicleType) {
        this.slotId = slotId;
        this.slotNumber = slotNumber;
        this.floorId = floorId;
        this.floorNumber = floorNumber;
        this.vehicleType = vehicleType;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
