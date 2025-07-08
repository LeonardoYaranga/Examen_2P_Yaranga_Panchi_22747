package ec.edu.espe.sensordatacollector.controller;

import ec.edu.espe.sensordatacollector.dto.SensorReadingDto;
import ec.edu.espe.sensordatacollector.model.SensorReading;
import ec.edu.espe.sensordatacollector.service.SensorReadingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor-readings")
@RequiredArgsConstructor
public class SensorReadingController {
    private static final Logger logger = LoggerFactory.getLogger(SensorReadingController.class);

    private final SensorReadingService sensorReadingService;

    @PostMapping
    public ResponseEntity<SensorReading> createSensorReading(@RequestBody SensorReadingDto dto) {
        try {
            logger.info("Received sensor reading: {}", dto);
            SensorReading reading = sensorReadingService.saveSensorReading(dto);
            logger.info("Sensor reading created: {}", reading);
            return ResponseEntity.ok(reading);
        } catch (Exception e) {
            logger.error("Error creating sensor reading: {}", dto, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<List<SensorReading>> getSensorReadings(@PathVariable String sensorId) {
        try {
            logger.info("Fetching readings for sensorId: {}", sensorId);
            List<SensorReading> readings = sensorReadingService.getReadingsBySensorId(sensorId);
            if (readings.isEmpty()) {
                logger.info("No readings found for sensorId: {}", sensorId);
                return ResponseEntity.noContent().build();
            }
            logger.info("Found {} readings for sensorId: {}", readings.size(), sensorId);
            return ResponseEntity.ok(readings);
        } catch (Exception e) {
            logger.error("Error fetching readings for sensorId: {}", sensorId, e);
            return ResponseEntity.status(500).build();
        }
    }
}