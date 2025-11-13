package com.foodfast.drone_service.scheduler;

import com.foodfast.drone_service.model.Drone;
import com.foodfast.drone_service.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DroneScheduler {

    private final DroneRepository droneRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 2000) // mỗi 2s
    public void updateDeliveringDrones() {
        List<Drone> list = droneRepository.findByStatus("DELIVERING");
        for (Drone d : list) {
            // mô phỏng cập nhật GPS nhỏ
            d.setLatitude(round(d.getLatitude() + (Math.random()-0.5) * 0.0005, 6));
            d.setLongitude(round(d.getLongitude() + (Math.random()-0.5) * 0.0005, 6));
            droneRepository.save(d);
            messagingTemplate.convertAndSend("/topic/drones", d); // realtime update
        }
    }

    private double round(double v, int decimals) {
        double pow = Math.pow(10, decimals);
        return Math.round(v * pow) / pow;
    }
}
