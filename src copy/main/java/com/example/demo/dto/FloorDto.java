package com.example.demo.dto;

public class FloorDto {
    private Long id;
    private Integer floorNumber;

    public FloorDto(Long id, Integer floorNumber) {
        this.id = id;
        this.floorNumber = floorNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}
