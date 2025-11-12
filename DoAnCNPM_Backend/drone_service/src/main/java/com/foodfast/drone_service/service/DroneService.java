package com.foodfast.drone_service.service;

import com.foodfast.drone_service.model.Drone;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneService {

    private final List<Drone> drones = new ArrayList<>();

    public DroneService() {
        // Khởi tạo demo drones
        drones.add(new Drone(1L, "Drone A", "idle"));
        drones.add(new Drone(2L, "Drone B", "flying"));
    }

    public List<Drone> getAllDrones() {
        return drones;
    }

    public Drone getDroneById(Long id) {
        return drones.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public void addDrone(Drone drone) {
        drones.add(drone);
    }

    public boolean deleteDrone(Long id) {
        return drones.removeIf(d -> d.getId().equals(id));
    }
}
