package ec.edu.espe.environment_analizer.controller;


import ec.edu.espe.environment_analizer.dto.AlertDTO;
import ec.edu.espe.environment_analizer.entity.Alert;
import ec.edu.espe.environment_analizer.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {
    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    private final AlertRepository alertRepository;

    @GetMapping
    public ResponseEntity<List<AlertDTO>> getAlerts(
            @RequestParam(value = "sensorId", required = false) String sensorId,
            @RequestParam(value = "type", required = false) String type) {
        try {
            List<Alert> alerts;
            if (sensorId != null && !sensorId.isEmpty()) {
                logger.info("Fetching alerts for sensorId: {}", sensorId);
                alerts = alertRepository.findBySensorId(sensorId);
            } else if (type != null && !type.isEmpty()) {
                logger.info("Fetching alerts for type: {}", type);
                alerts = alertRepository.findByType(type);
            } else {
                logger.info("Fetching all alerts");
                alerts = alertRepository.findAll();
            }

            List<AlertDTO> alertDtos = alerts.stream().map(this::mapToDto).collect(Collectors.toList());
            return ResponseEntity.ok(alertDtos);
        } catch (Exception e) {
            logger.error("Error fetching alerts: sensorId={}, type={}", sensorId, type, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    private AlertDTO mapToDto(Alert alert) {
        AlertDTO dto = new AlertDTO();
        dto.setAlertId(alert.getAlertId());
        dto.setType(alert.getType());
        dto.setSensorId(alert.getSensorId());
        dto.setValue(alert.getValue());
        dto.setThreshold(alert.getThreshold());
        dto.setTimestamp(alert.getTimestamp());
        return dto;
    }
}
