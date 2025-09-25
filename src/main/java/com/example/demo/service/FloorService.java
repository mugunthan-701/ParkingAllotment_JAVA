package com.example.demo.service;

import com.example.demo.dto.CreateFloorRequest;
import com.example.demo.dto.FloorDto;
import com.example.demo.model.Floor;
import com.example.demo.repository.FloorRepository;
import org.springframework.stereotype.Service;

@Service
public class FloorService {

    private final FloorRepository floorRepository;

    public FloorService(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    public FloorDto createFloor(CreateFloorRequest request) {
        Floor floor = new Floor(request.getFloorNumber());
        Floor savedFloor = floorRepository.save(floor);
        return new FloorDto(savedFloor.getId(), savedFloor.getFloorNumber());
    }
}
