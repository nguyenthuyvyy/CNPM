package com.foodfast.drone_service.controller;

import com.foodfast.drone_service.model.Drone;
import com.foodfast.drone_service.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    public List<Drone> getAllDrones() {
        return droneService.getAllDrones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drone> getDroneById(@PathVariable Long id) {
        return droneService.getDroneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Drone createDrone(@RequestBody Drone drone) {
        return droneService.createDrone(drone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drone> updateDrone(@PathVariable Long id, @RequestBody Drone drone) {
        return ResponseEntity.ok(droneService.updateDrone(id, drone));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrone(@PathVariable Long id) {
        droneService.deleteDrone(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<Drone> getDronesByStatus(@PathVariable String status) {
        return droneService.getDronesByStatus(status);
    }
}
