package com.example.demo.service;

import com.example.demo.dto.AvailabilityDto;
import com.example.demo.dto.CreateReservationRequest;
import com.example.demo.dto.ReservationDto;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Reservation;
import com.example.demo.model.Slot;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.SlotRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;

    public ReservationService(ReservationRepository reservationRepository, SlotRepository slotRepository) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public ReservationDto createReservation(CreateReservationRequest request) {
        validateReservationRequest(request);

        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + request.getSlotId()));

        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(request.getSlotId(), request.getStartTime(), request.getEndTime());
        if (!overlappingReservations.isEmpty()) {
            throw new ReservationConflictException("Slot is already reserved for the given time range.");
        }

        BigDecimal cost = calculateCost(slot, request.getStartTime(), request.getEndTime());

        Reservation reservation = new Reservation(slot, request.getStartTime(), request.getEndTime(), request.getVehicleNumber(), cost);
        
        try {
            Reservation savedReservation = reservationRepository.save(reservation);
            return toDto(savedReservation);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new ReservationConflictException("This slot was just booked by someone else. Please try a different slot or time.");
        }
    }

    @Transactional(readOnly = true)
    public ReservationDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return toDto(reservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + id);
        }
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AvailabilityDto> findAvailableSlots(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        System.out.println("ReservationService: Finding available slots between " + startTime + " and " + endTime);
        if (startTime.isAfter(endTime)) {
            throw new InvalidRequestException("Start time must be before end time.");
        }
        Page<Slot> allSlots = slotRepository.findAll(pageable);
        System.out.println("ReservationService: Found " + allSlots.getTotalElements() + " total slots.");
        List<Long> reservedSlotIds = allSlots.getContent().stream()
                .filter(slot -> !reservationRepository.findOverlappingReservations(slot.getId(), startTime, endTime).isEmpty())
                .map(Slot::getId)
                .collect(Collectors.toList());
        System.out.println("ReservationService: Found " + reservedSlotIds.size() + " reserved slots.");

        Page<AvailabilityDto> availableSlots = allSlots.map(this::toAvailabilityDto)
                .map(dto -> {
                    if (dto != null && reservedSlotIds.contains(dto.getSlotId())) {
                        return null;
                    }
                    return dto;
                });

        System.out.println("ReservationService: Returning " + availableSlots.getNumberOfElements() + " available slots.");
        return availableSlots;
    }

    private void validateReservationRequest(CreateReservationRequest request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidRequestException("Start time must be before end time.");
        }
        if (Duration.between(request.getStartTime(), request.getEndTime()).toHours() > 24) {
            throw new InvalidRequestException("Reservation duration cannot exceed 24 hours.");
        }
    }

    private BigDecimal calculateCost(Slot slot, LocalDateTime startTime, LocalDateTime endTime) {
        long hours = (long) Math.ceil(Duration.between(startTime, endTime).toMinutes() / 60.0);
        return slot.getVehicleType().getRate().multiply(BigDecimal.valueOf(hours));
    }

    private ReservationDto toDto(Reservation reservation) {
        return new ReservationDto(reservation.getId(), reservation.getSlot().getId(), reservation.getStartTime(), reservation.getEndTime(), reservation.getVehicleNumber(), reservation.getCost());
    }

    private AvailabilityDto toAvailabilityDto(Slot slot) {
        return new AvailabilityDto(slot.getId(), slot.getSlotNumber(), slot.getFloor().getId(), slot.getFloor().getFloorNumber(), slot.getVehicleType());
    }
}
