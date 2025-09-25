package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class CreateFloorRequest {

    @NotNull(message = "Floor number cannot be null")
    private Integer floorNumber;

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}
