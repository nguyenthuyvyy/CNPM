package com.foodfast.drone_service.controller;

import com.foodfast.drone_service.model.Drone;
import com.foodfast.drone_service.service.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drones")
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    // GET /api/drones
    @GetMapping
    public List<Drone> getAllDrones() {
        return droneService.getAllDrones();
    }

    // GET /api/drones/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Drone> getDrone(@PathVariable Long id) {
        Drone drone = droneService.getDroneById(id);
        if (drone == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(drone);
    }

    // POST /api/drones
    @PostMapping
    public Drone addDrone(@RequestBody Drone drone) {
        droneService.addDrone(drone);
        return drone;
    }

    // DELETE /api/drones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrone(@PathVariable Long id) {
        boolean removed = droneService.deleteDrone(id);
        if (!removed) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    // Health check endpoint
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
