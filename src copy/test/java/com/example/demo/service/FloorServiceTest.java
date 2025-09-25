package com.example.demo.service;

import com.example.demo.dto.CreateFloorRequest;
import com.example.demo.dto.FloorDto;
import com.example.demo.model.Floor;
import com.example.demo.repository.FloorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FloorServiceTest {

    @Mock
    private FloorRepository floorRepository;

    @InjectMocks
    private FloorService floorService;

    @Test
    void testCreateFloor() {
        CreateFloorRequest request = new CreateFloorRequest();
        request.setFloorNumber(1);

        Floor floor = new Floor(1);
        floor.setId(1L);

        when(floorRepository.save(any(Floor.class))).thenReturn(floor);

        FloorDto result = floorService.createFloor(request);

        assertEquals(1L, result.getId());
        assertEquals(1, result.getFloorNumber());
    }
}
