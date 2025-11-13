package com.foodfast.drone_service.scheduler;

import com.foodfast.drone_service.model.Drone;
import com.foodfast.drone_service.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DroneScheduler {

    private final DroneRepository droneRepository;

    @Scheduled(fixedRate = 2000)
    public void updateDroneLocations() {
        for (Drone drone : droneRepository.findAll()) {
            if ("DELIVERING".equals(drone.getStatus())) {
                drone.setLatitude(drone.getLatitude() + Math.random() * 0.0001);
                drone.setLongitude(drone.getLongitude() + Math.random() * 0.0001);
                droneRepository.save(drone);
            }
        }
    }
}
