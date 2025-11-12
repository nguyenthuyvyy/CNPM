package com.foodfast.drone_service.service;

import com.foodfast.drone_service.model.Drone;

import java.util.List;
import java.util.Optional;

public interface DroneService {
    List<Drone> getAllDrones();
    Optional<Drone> getDroneById(Long id);
    Drone createDrone(Drone drone);
    Drone updateDrone(Long id, Drone drone);
    void deleteDrone(Long id);
    List<Drone> getDronesByStatus(String status);
}
