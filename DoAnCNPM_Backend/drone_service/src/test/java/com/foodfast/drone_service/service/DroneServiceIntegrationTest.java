package com.foodfast.drone_service.service;

import com.foodfast.drone_service.model.Drone;
import com.foodfast.drone_service.repository.DroneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DroneServiceIntegrationTest {

    @Autowired
    private DroneService droneService;

    @Autowired
    private DroneRepository droneRepository;

    @Test
    @Transactional
    void testCreateDrone() {
        Drone drone = new Drone();
        drone.setName("Drone A");
        drone.setModel("M1000");
        drone.setBatteryLevel(95);

        Drone saved = droneService.createDrone(drone);

        assertNotNull(saved.getId());
        assertEquals("AVAILABLE", saved.getStatus());

        Drone found = droneRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Drone A", found.getName());
    }

    @Test
    @Transactional
    void testGetDroneById() {
        Drone drone = new Drone();
        drone.setName("Test Drone");
        drone.setStatus("AVAILABLE");

        Drone saved = droneRepository.save(drone);

        Drone found = droneService.getDroneById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("Test Drone", found.getName());
    }

    @Test
    @Transactional
    void testUpdateDrone() {
        Drone drone = new Drone();
        drone.setName("Old");
        drone.setModel("M1");
        drone.setStatus("AVAILABLE");

        Drone saved = droneRepository.save(drone);

        Drone update = new Drone();
        update.setName("New");
        update.setModel("M2");
        update.setStatus("BUSY");
        update.setBatteryLevel(80);
        update.setLatitude(10.123);
        update.setLongitude(106.123);

        Drone updated = droneService.updateDrone(saved.getId(), update);

        assertEquals("New", updated.getName());
        assertEquals("M2", updated.getModel());
        assertEquals("BUSY", updated.getStatus());
        assertEquals(80, updated.getBatteryLevel());
        assertEquals(10.123, updated.getLatitude());
    }

    @Test
    @Transactional
    void testGetDronesByStatus() {
        Drone d1 = new Drone();
        d1.setName("D1");
        d1.setStatus("AVAILABLE");

        Drone d2 = new Drone();
        d2.setName("D2");
        d2.setStatus("BUSY");

        droneRepository.saveAll(List.of(d1, d2));

        List<Drone> available = droneService.getDronesByStatus("AVAILABLE");

        assertEquals(1, available.size());
        assertEquals("D1", available.get(0).getName());
    }

    @Test
    @Transactional
    void testDeleteDrone() {
        Drone drone = new Drone();
        drone.setName("Need Delete");

        Drone saved = droneRepository.save(drone);

        droneService.deleteDrone(saved.getId());

        assertFalse(droneRepository.findById(saved.getId()).isPresent());
    }
}
