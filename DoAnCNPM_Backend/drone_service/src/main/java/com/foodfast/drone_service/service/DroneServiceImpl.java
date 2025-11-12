package com.foodfast.drone_service.service;

import com.foodfast.drone_service.model.Drone;
import com.foodfast.drone_service.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    @Override
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    @Override
    public Optional<Drone> getDroneById(Long id) {
        return droneRepository.findById(id);
    }

    @Override
    public Drone createDrone(Drone drone) {
        drone.setStatus("AVAILABLE");
        return droneRepository.save(drone);
    }

    @Override
    public Drone updateDrone(Long id, Drone drone) {
        return droneRepository.findById(id)
                .map(existing -> {
                    existing.setName(drone.getName());
                    existing.setModel(drone.getModel());
                    existing.setBatteryLevel(drone.getBatteryLevel());
                    existing.setStatus(drone.getStatus());
                    existing.setLatitude(drone.getLatitude());
                    existing.setLongitude(drone.getLongitude());
                    return droneRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Drone not found with id " + id));
    }

    @Override
    public void deleteDrone(Long id) {
        droneRepository.deleteById(id);
    }

    @Override
    public List<Drone> getDronesByStatus(String status) {
        return droneRepository.findByStatus(status);
    }
}
