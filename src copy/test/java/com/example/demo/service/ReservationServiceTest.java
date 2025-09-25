package com.example.demo.service;

import com.example.demo.dto.CreateReservationRequest;
import com.example.demo.dto.ReservationDto;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Floor;
import com.example.demo.model.Reservation;
import com.example.demo.model.Slot;
import com.example.demo.model.VehicleType;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.SlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void testCreateReservation() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setSlotId(1L);
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));
        request.setVehicleNumber("KA01AB1234");

        Floor floor = new Floor(1);
        floor.setId(1L);

        Slot slot = new Slot(101, floor, VehicleType.FOUR_WHEELER);
        slot.setId(1L);

        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(reservationRepository.findOverlappingReservations(any(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        ReservationDto result = reservationService.createReservation(request);

        assertEquals(1L, result.getSlotId());
        assertEquals("KA01AB1234", result.getVehicleNumber());
        assertEquals(new BigDecimal("30.0"), result.getCost());
    }

    @Test
    void testCreateReservation_SlotNotFound() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setSlotId(1L);
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));

        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(request));
    }

    @Test
    void testCreateReservation_Conflict() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setSlotId(1L);
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));

        Floor floor = new Floor(1);
        floor.setId(1L);

        Slot slot = new Slot(101, floor, VehicleType.FOUR_WHEELER);
        slot.setId(1L);

        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(reservationRepository.findOverlappingReservations(any(), any(), any())).thenReturn(Collections.singletonList(new Reservation()));

        assertThrows(ReservationConflictException.class, () -> reservationService.createReservation(request));
    }

    @Test
    void testCreateReservation_InvalidTimeRange() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setStartTime(LocalDateTime.now().plusHours(2));
        request.setEndTime(LocalDateTime.now().plusHours(1));

        assertThrows(InvalidRequestException.class, () -> reservationService.createReservation(request));
    }

    @Test
    void testCreateReservation_Exceeds24Hours() {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(26));

        assertThrows(InvalidRequestException.class, () -> reservationService.createReservation(request));
    }

    @Test
    void testGetReservationById() {
        Floor floor = new Floor(1);
        floor.setId(1L);

        Slot slot = new Slot(101, floor, VehicleType.FOUR_WHEELER);
        slot.setId(1L);

        Reservation reservation = new Reservation(slot, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "KA01AB1234", new BigDecimal("30.0"));
        reservation.setId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ReservationDto result = reservationService.getReservationById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testCancelReservation() {
        when(reservationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.cancelReservation(1L);

        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCancelReservation_NotFound() {
        when(reservationRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reservationService.cancelReservation(1L));
    }
}
