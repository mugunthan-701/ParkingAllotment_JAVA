package com.example.demo.controller;

import com.example.demo.dto.CreateSlotRequest;
import com.example.demo.dto.SlotDto;
import com.example.demo.service.SlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/slots")
@Tag(name = "Slots", description = "APIs for managing parking slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping
    @Operation(summary = "Create a new parking slot for a floor")
    public ResponseEntity<SlotDto> createSlot(@Valid @RequestBody CreateSlotRequest request) {
        SlotDto createdSlot = slotService.createSlot(request);
        return new ResponseEntity<>(createdSlot, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get a list of all parking slots")
    public ResponseEntity<List<SlotDto>> getAllSlots() {
        List<SlotDto> slots = slotService.getAllSlots();
        return ResponseEntity.ok(slots);
    }
}
