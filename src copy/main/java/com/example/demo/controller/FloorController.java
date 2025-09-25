package com.example.demo.controller;

import com.example.demo.dto.CreateFloorRequest;
import com.example.demo.dto.FloorDto;
import com.example.demo.service.FloorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/floors")
@Tag(name = "Floors", description = "APIs for managing parking floors")
public class FloorController {

    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    @PostMapping
    @Operation(summary = "Create a new parking floor")
    public ResponseEntity<FloorDto> createFloor(@Valid @RequestBody CreateFloorRequest request) {
        FloorDto createdFloor = floorService.createFloor(request);
        return new ResponseEntity<>(createdFloor, HttpStatus.CREATED);
    }
}
