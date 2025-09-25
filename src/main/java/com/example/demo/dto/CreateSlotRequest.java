package com.example.demo.dto;

import com.example.demo.model.VehicleType;
import jakarta.validation.constraints.NotNull;

public class CreateSlotRequest {

    @NotNull(message = "Slot number cannot be null")
    private Integer slotNumber;

    @NotNull(message = "Floor ID cannot be null")
    private Long floorId;

    @NotNull(message = "Vehicle type cannot be null")
    private VehicleType vehicleType;

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
