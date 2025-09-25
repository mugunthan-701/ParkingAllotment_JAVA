package com.example.demo.service;

import com.example.demo.dto.CreateSlotRequest;
import com.example.demo.dto.SlotDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Floor;
import com.example.demo.model.Slot;
import com.example.demo.model.VehicleType;
import com.example.demo.repository.FloorRepository;
import com.example.demo.repository.SlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SlotServiceTest {

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private FloorRepository floorRepository;

    @InjectMocks
    private SlotService slotService;

    @Test
    void testCreateSlot() {
        CreateSlotRequest request = new CreateSlotRequest();
        request.setSlotNumber(101);
        request.setFloorId(1L);
        request.setVehicleType(VehicleType.FOUR_WHEELER);

        Floor floor = new Floor(1);
        floor.setId(1L);

        Slot slot = new Slot(101, floor, VehicleType.FOUR_WHEELER);
        slot.setId(1L);

        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);

        SlotDto result = slotService.createSlot(request);

        assertEquals(1L, result.getId());
        assertEquals(101, result.getSlotNumber());
        assertEquals(1L, result.getFloorId());
        assertEquals(VehicleType.FOUR_WHEELER, result.getVehicleType());
    }

    @Test
    void testCreateSlot_FloorNotFound() {
        CreateSlotRequest request = new CreateSlotRequest();
        request.setFloorId(1L);

        when(floorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> slotService.createSlot(request));
    }
}
