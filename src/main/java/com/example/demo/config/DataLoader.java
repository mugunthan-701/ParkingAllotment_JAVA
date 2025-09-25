package com.example.demo.config;

import com.example.demo.model.Floor;
import com.example.demo.model.Slot;
import com.example.demo.model.VehicleType;
import com.example.demo.repository.FloorRepository;
import com.example.demo.repository.SlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final FloorRepository floorRepository;
    private final SlotRepository slotRepository;

    public DataLoader(FloorRepository floorRepository, SlotRepository slotRepository) {
        this.floorRepository = floorRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (floorRepository.count() == 0) {
            Floor floor1 = new Floor(1);
            floorRepository.save(floor1);

            slotRepository.save(new Slot(1, floor1, VehicleType.CAR));
            slotRepository.save(new Slot(2, floor1, VehicleType.CAR));
            slotRepository.save(new Slot(3, floor1, VehicleType.MOTORCYCLE));
            slotRepository.save(new Slot(4, floor1, VehicleType.TRUCK));
        }
    }
}
