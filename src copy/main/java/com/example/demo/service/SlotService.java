package com.example.demo.service;

import com.example.demo.dto.CreateSlotRequest;
import com.example.demo.dto.SlotDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Floor;
import com.example.demo.model.Slot;
import com.example.demo.repository.FloorRepository;
import com.example.demo.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final FloorRepository floorRepository;

    public SlotService(SlotRepository slotRepository, FloorRepository floorRepository) {
        this.slotRepository = slotRepository;
        this.floorRepository = floorRepository;
    }

    public SlotDto createSlot(CreateSlotRequest request) {
        Floor floor = floorRepository.findById(request.getFloorId())
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + request.getFloorId()));

        Slot slot = new Slot(request.getSlotNumber(), floor, request.getVehicleType());
        Slot savedSlot = slotRepository.save(slot);

        return toDto(savedSlot);
    }

    public List<SlotDto> getAllSlots() {
        return slotRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SlotDto toDto(Slot slot) {
        return new SlotDto(slot.getId(), slot.getSlotNumber(), slot.getFloor().getId(), slot.getVehicleType());
    }
}
